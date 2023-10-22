package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.request.CreateDeliveryRequestDto;
import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.request.KakaoPayApproveRequestDto;
import com.lotdiz.fundingservice.dto.request.MemberPointUpdateRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductFundingRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.fundingservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.fundingservice.exception.PaymentServiceOutOfServiceException;
import com.lotdiz.fundingservice.exception.ProjectServiceOutOfServiceException;
import com.lotdiz.fundingservice.messagequeue.kafka.DeliveryProducer;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import com.lotdiz.fundingservice.service.client.MemberServiceClient;
import com.lotdiz.fundingservice.service.client.PaymentServiceClient;
import com.lotdiz.fundingservice.service.client.ProjectServiceClient;
import com.lotdiz.fundingservice.service.manager.FundingProductManager;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FundingService {
  private final FundingRepository fundingRepository;
  private final ProductFundingRepository productFundingRepository;
  private final SupporterWithUsRepository supporterWithUsRepository;
  private final ProjectServiceClient projectServiceClient;
  private final MemberServiceClient memberServiceClient;
  private final PaymentServiceClient paymentServiceClient;
  private final FundingProductManager fundingProductManager;
  private final CircuitBreakerFactory circuitBreakerFactory;
  private final DeliveryProducer deliveryProducer;

  /**
   * 펀딩하기
   *
   * @param createFundingRequestDto
   * @return fundingId(Long)
   */
  @Transactional
  public void createFunding(CreateFundingRequestDto createFundingRequestDto, Long memberId) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // List<productId> 를 찾는다.
    List<ProductFundingRequestDto> productFundingRequestDtos =
        createFundingRequestDto.getProducts();
    List<Long> productIds =
        productFundingRequestDtos.stream()
            .map(ProductFundingRequestDto::getProductId)
            .collect(Collectors.toList());

    // 1. 재고 확인.
    List<ProductStockCheckResponse> productStockCheckResponseList =
        (List<ProductStockCheckResponse>)
            circuitBreaker.run(
                () -> projectServiceClient.getStockQuantityCheckExceed(productIds).getData(),
                throwable -> new ProjectServiceOutOfServiceException());

    List<ProductStockUpdateRequest> productStockUpdateRequests = new ArrayList<>();

    for (int i = 0; i < productFundingRequestDtos.size(); i++) {
      ProductFundingRequestDto productFundingRequestDto = productFundingRequestDtos.get(i);
      ProductStockCheckResponse productStockCheckResponse = productStockCheckResponseList.get(i);

      fundingProductManager.checkEnoughStockQuantity(
          productFundingRequestDto.getProductFundingQuantity(),
          productStockCheckResponse.getProductCurrentStockQuantity());

      ProductStockUpdateRequest productStockUpdateRequest =
          ProductStockUpdateRequest.builder()
              .productId(productFundingRequestDto.getProductId())
              .productFundingQuantity(productFundingRequestDto.getProductFundingQuantity())
              .build();

      productStockUpdateRequests.add(productStockUpdateRequest);
    }

    // 2. 재고 차감.
    circuitBreaker.run(
        () -> projectServiceClient.updateStockQuantity(productStockUpdateRequests),
        throwable -> new ProjectServiceOutOfServiceException());

    // 3. 엔티티 저장.
    // (Funding) DTO->ENTITY
    Funding funding = createFundingRequestDto.toFundingEntity(memberId);
    Funding savedFunding = fundingRepository.save(funding);

    // (ProductFunding) DTO->ENTITY
    List<ProductFunding> productFundings =
        productFundingRequestDtos.stream()
            .map(productFundingRequestDto -> productFundingRequestDto.toEntity(savedFunding))
            .collect(Collectors.toList());
    productFundingRepository.saveAll(productFundings);

    // (SupporterWithUs) DTO -> ENTITY
    SupporterWithUs supporterWithUs = createFundingRequestDto.toSupporterWithUsEntity(savedFunding);
    supporterWithUsRepository.save(supporterWithUs);

    // 4. 결제승인 요청
    KakaoPayApproveRequestDto approveRequestDto =
        KakaoPayApproveRequestDto.builder()
            .fundingId(savedFunding.getFundingId())
            .tid(createFundingRequestDto.getTid())
            .pgToken(createFundingRequestDto.getPgToken())
            .partnerOrderId(createFundingRequestDto.getPartnerOrderId())
            .partnerUserId(createFundingRequestDto.getPartnerUserId())
            .build();
    circuitBreaker.run(
        () -> paymentServiceClient.payApprove(approveRequestDto),
        throwable -> new PaymentServiceOutOfServiceException());

    // 5. 포인트 차감
    MemberPointUpdateRequestDto memberPointUpdateRequestDto =
        MemberPointUpdateRequestDto.builder()
            .memberId(memberId)
            .memberPoint(createFundingRequestDto.getFundingUsedPoint())
            .build();
    circuitBreaker.run(
        () -> memberServiceClient.usePoint(memberPointUpdateRequestDto),
        throwable -> new MemberServiceClientOutOfServiceException());

    // 6. 배송 Kafka send
    CreateDeliveryRequestDto createDeliveryRequestDto =
        CreateDeliveryRequestDto.toDto(savedFunding, createFundingRequestDto);
    deliveryProducer.sendCreateDelivery(createDeliveryRequestDto);

  }

  //  public GetFundingDetailResponseDto getFundingDetailResponse(Long fundingId) {
  //    Funding findFunding =
  //        fundingRepository.findById(fundingId).orElseThrow(FundingEntityNotFoundException::new);
  //
  //    Optional<List<ProductFunding>> findProductFundingsOpt =
  //        Optional.ofNullable(productFundingRepository.findByFunding(findFunding));
  //    List<ProductFunding> findProductFundings =
  //        findProductFundingsOpt.orElseThrow(ProductFundingEntityNotFoundException::new);
  //
  //    return GetFundingDetailResponseDto.fromEntity(findFunding, findProductFundings);
  //  }
}
