package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.common.ProductFundingDto;
import com.lotdiz.fundingservice.dto.request.CreateFundingRequestDto;
import com.lotdiz.fundingservice.dto.request.GetStockQuantityCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductStockCheckRequest;
import com.lotdiz.fundingservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.fundingservice.dto.request.UpdateProductStockQuantityRequestDto;
import com.lotdiz.fundingservice.dto.response.GetFundingDetailResponseDto;
import com.lotdiz.fundingservice.dto.response.GetStockQuantityCheckExceedResponseDto;
import com.lotdiz.fundingservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.FundingEntityNotFoundException;
import com.lotdiz.fundingservice.exception.ProjectServiceOutOfServiceException;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import com.lotdiz.fundingservice.service.client.ProjectServiceClient;
import com.lotdiz.fundingservice.service.manager.FundingProductManager;
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
  private final FundingProductManager fundingProductManager;
  private final CircuitBreakerFactory circuitBreakerFactory;

  /**
   * 펀딩하기
   *
   * @param createFundingRequestDto
   * @return fundingId(Long)
   */
  @Transactional
  public Long createFunding(CreateFundingRequestDto createFundingRequestDto) {
    CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");

    Long projectId = createFundingRequestDto.getProjectId();
    List<ProductFundingDto> productFundingDtoRequests = createFundingRequestDto.getProducts();
    List<ProductStockCheckRequest> productStockCheckRequests =
        productFundingDtoRequests.stream()
            .map(
                productFundingDto -> new ProductStockCheckRequest(productFundingDto.getProductId()))
            .collect(Collectors.toList());

    GetStockQuantityCheckExceedRequestDto getStockQuantityCheckExceedRequestDto =
        new GetStockQuantityCheckExceedRequestDto(productStockCheckRequests);

    GetStockQuantityCheckExceedResponseDto response =
        (GetStockQuantityCheckExceedResponseDto)
            circuitBreaker.run(
                () ->
                    projectServiceClient.getStockQuantityCheckExceed(
                        projectId, getStockQuantityCheckExceedRequestDto).getData(),
                throwable -> new ProjectServiceOutOfServiceException());

    List<ProductStockCheckResponse> productStockCheckResponseList =
              response.getProductStockCheckResponses();

    List<ProductStockUpdateRequest> productStockUpdateRequests = new ArrayList<>();

    for (int i = 0; i < productFundingDtoRequests.size(); i++) {
      ProductFundingDto productFundingDto = productFundingDtoRequests.get(i);
      ProductStockCheckResponse productStockCheckResponse = productStockCheckResponseList.get(i);

      try {
        fundingProductManager.checkEnoughStockQuantity(
            productFundingDto.getProductFundingQuantity(),
            productStockCheckResponse.getProductStockQuantity());

        ProductStockUpdateRequest productStockUpdateRequest =
            ProductStockUpdateRequest.builder()
                .productId(productFundingDto.getProductId())
                .productFundingQuantity(productFundingDto.getProductFundingQuantity())
                .build();

        productStockUpdateRequests.add(productStockUpdateRequest);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // (Funding) DTO->ENTITY
    Funding funding = createFundingRequestDto.toFundingEntity();
    Funding savedFunding = fundingRepository.save(funding);

    // (ProductFunding) DTO->ENTITY
    List<ProductFunding> productFundings =
        productFundingDtoRequests.stream()
            .map(productFundingDto -> productFundingDto.toEntity(savedFunding))
            .collect(Collectors.toList());
    productFundingRepository.saveAll(productFundings);

    // (SupporterWithUs) DTO -> ENTITY
    SupporterWithUs supporterWithUs = createFundingRequestDto.toSupporterWithUsEntity(savedFunding);
    supporterWithUsRepository.save(supporterWithUs);

    // TODO: 결제

    // project-service로 재고 update
    UpdateProductStockQuantityRequestDto updateProductStockQuantityRequestDto =
        new UpdateProductStockQuantityRequestDto(productStockUpdateRequests);

    circuitBreaker.run(
        () ->
            projectServiceClient.updateStockQuantity(updateProductStockQuantityRequestDto),
        throwable -> new ProjectServiceOutOfServiceException());

    return savedFunding.getFundingId();
  }

  public GetFundingDetailResponseDto getFundingDetailResponse(Long fundingId) {
    Funding findFunding =
        fundingRepository.findById(fundingId).orElseThrow(FundingEntityNotFoundException::new);

    List<ProductFunding> findProductFundings = productFundingRepository.findByFunding(findFunding);
    return GetFundingDetailResponseDto.fromEntity(findFunding, findProductFundings);
  }
}
