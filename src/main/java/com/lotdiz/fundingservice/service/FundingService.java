package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.request.CreateDeliveryRequestDto;
import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.request.KakaoPayApproveRequestDto;
import com.lotdiz.fundingservice.dto.request.MemberPointUpdateRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductFundingRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.fundingservice.dto.response.FundingAndTotalPageResponseDto;
import com.lotdiz.fundingservice.dto.response.FundingInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.PaymentInfoResponseDto;
import com.lotdiz.fundingservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.fundingservice.dto.response.ProjectAndMakerInfoResponseDto;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.FundingStatus;
import com.lotdiz.fundingservice.entity.ProductFunding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.DeliveryStatusNotFoundException;
import com.lotdiz.fundingservice.exception.MemberServiceClientOutOfServiceException;
import com.lotdiz.fundingservice.exception.PaymentInfoNotFoundException;
import com.lotdiz.fundingservice.exception.PaymentServiceOutOfServiceException;
import com.lotdiz.fundingservice.exception.ProjectAndMakerInfoNotFoundException;
import com.lotdiz.fundingservice.exception.ProjectServiceOutOfServiceException;
import com.lotdiz.fundingservice.messagequeue.kafka.DeliveryProducer;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import com.lotdiz.fundingservice.service.client.DeliveryServiceClient;
import com.lotdiz.fundingservice.service.client.MemberServiceClient;
import com.lotdiz.fundingservice.service.client.PaymentServiceClient;
import com.lotdiz.fundingservice.service.client.ProjectServiceClient;
import com.lotdiz.fundingservice.service.manager.FundingProductManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FundingService {
  private final FundingRepository fundingRepository;
  private final ProductFundingRepository productFundingRepository;
  private final SupporterWithUsRepository supporterWithUsRepository;
  private final ProjectServiceClient projectServiceClient;
  private final PaymentServiceClient paymentServiceClient;
  private final DeliveryServiceClient deliveryServiceClient;
  private final MemberServiceClient memberServiceClient;
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

  public FundingAndTotalPageResponseDto getFundingInfoListResponse(
      Long memberId, Pageable pageable) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    // 펀딩 목록. (PageRequest로 페이징 처리)
    Page<Funding> fundingsPerPage = fundingRepository.findByMemberId(memberId, pageable);
    List<Funding> fundingsByMember = fundingsPerPage.getContent();

    // 총 페이지 수
    Long totalPages = (long) fundingsPerPage.getTotalPages();

    // 프로젝트Ids (회원이 참여한 프로젝트Id목록)
    List<Long> projectIds =
        new ArrayList<>(
            fundingsByMember.stream().map(Funding::getProjectId).collect(Collectors.toSet()));

    // project-service로 정보 요청
    List<ProjectAndMakerInfoResponseDto> projectAndMakerInfoResponseDtos =
        (List<ProjectAndMakerInfoResponseDto>)
            circuitBreaker.run(
                () -> projectServiceClient.getProjectAndMakerInfo(projectIds).getData(),
                throwable -> new ProjectAndMakerInfoNotFoundException());

    // 펀딩Ids (회원이 참여한 펀딩Id 목록)
    List<Long> fundingIds =
        fundingsByMember.stream().map(Funding::getFundingId).collect(Collectors.toList());

    // payment-service로 정보 요청
    List<PaymentInfoResponseDto> paymentInfoResponseDtos =
        (List<PaymentInfoResponseDto>)
            circuitBreaker.run(
                () -> paymentServiceClient.getPaymentInfo(fundingIds).getData(),
                throwable -> new PaymentInfoNotFoundException());

    // delivery-service로 정보 요청
    List<String> deliveryStatusList =
        (List<String>)
            circuitBreaker.run(
                () -> deliveryServiceClient.getDeliveryStatus(fundingIds).getData(),
                throwable -> new DeliveryStatusNotFoundException());

    // 최종 반환 List 생성
    List<FundingInfoResponseDto> fundingInfoResponseDtos = new ArrayList<>();

    // 반환 List에 값 할당.
    for (int i = 0; i < fundingsByMember.size(); i++) {
      // 프로젝트 정보 추가
      ProjectAndMakerInfoResponseDto projectInfo = projectAndMakerInfoResponseDtos.get(i);
      FundingInfoResponseDto fundingInfoResponseDto =
          FundingInfoResponseDto.builder()
              .fundingId(fundingsByMember.get(i).getFundingId())
              .projectId(projectInfo.getProjectId())
              .projectName(projectInfo.getProjectName())
              .projectStatus(projectInfo.getProjectStatus())
              .projectImageUrl(projectInfo.getProjectImageUrl())
              .makerName(projectInfo.getMakerName())
              .build();

      // 펀딩 관련 정보 추가
      Long projectId = fundingsByMember.get(i).getProjectId();
      List<Funding> findFundingsByProjectId = fundingRepository.findByProjectId(projectId);

      Long projectParticipantCount = (long) findFundingsByProjectId.size();
      fundingInfoResponseDto.setProjectParticipantCount(projectParticipantCount);

      long totalAccumulatedFundingSupportAmount =
          findFundingsByProjectId.stream()
              .filter(funding -> funding.getFundingStatus().equals(FundingStatus.COMPLETED))
              .mapToLong(
                  funding -> funding.getFundingTotalAmount() + funding.getFundingSupportAmount())
              .sum();
      fundingInfoResponseDto.setTotalAccumulatedFundingAmount(totalAccumulatedFundingSupportAmount);
      fundingInfoResponseDto.setFundingAchievementRate(
          (totalAccumulatedFundingSupportAmount * 100 / projectInfo.getProjectTargetAmount()));
      fundingInfoResponseDto.setRemainingDays(projectInfo.getRemainingDays());
      // 결제 관련 정보 추가
      PaymentInfoResponseDto paymentInfo = paymentInfoResponseDtos.get(i);
      fundingInfoResponseDto.setTotalPaymentAmount(paymentInfo.getFundingPaymentsActualAmount());
      fundingInfoResponseDto.setPaymentDate(paymentInfo.getCreatedAt());
      // 펀딩 상태 정보 추가
      fundingInfoResponseDto.setFundingStatus(fundingsByMember.get(i).getFundingStatus().name());
      // 배송 상태 정보 추가
      fundingInfoResponseDto.setDeliveryStatus(deliveryStatusList.get(i));

      // LIST에 모두 담기
      fundingInfoResponseDtos.add(fundingInfoResponseDto);
    }

    FundingAndTotalPageResponseDto responseDto =
        FundingAndTotalPageResponseDto.builder()
            .totalPages(totalPages)
            .fundingInfoResponseDtos(fundingInfoResponseDtos)
            .build();

    return responseDto;
  }
}
