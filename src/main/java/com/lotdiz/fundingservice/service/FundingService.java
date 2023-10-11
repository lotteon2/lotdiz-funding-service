package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.common.ProductFundingDto;
import com.lotdiz.fundingservice.dto.request.CreateFundingRequest;
import com.lotdiz.fundingservice.dto.request.GetStockQuantityCheckExceedRequestDto;
import com.lotdiz.fundingservice.dto.request.ProductStockCheckRequest;
import com.lotdiz.fundingservice.dto.request.ProductStockUpdateRequest;
import com.lotdiz.fundingservice.dto.request.UpdateProductStockQuantityRequestDto;
import com.lotdiz.fundingservice.dto.response.GetFundingDetailResponse;
import com.lotdiz.fundingservice.dto.response.GetStockQuantityCheckExceedResponseDto;
import com.lotdiz.fundingservice.dto.response.ProductStockCheckResponse;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.FundingEntityNotFoundException;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import com.lotdiz.fundingservice.service.client.ProjectServiceClient;
import com.lotdiz.fundingservice.service.manager.FundingProductManager;
import com.lotdiz.fundingservice.utils.SuccessResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

  /**
   * 펀딩하기
   *
   * @param createFundingRequest
   * @return fundingId(Long)
   */
  @Transactional
  public Long createFunding(CreateFundingRequest createFundingRequest) {

    Long projectId = createFundingRequest.getProjectId();
    List<ProductFundingDto> productFundingDtoRequests = createFundingRequest.getProducts();
    List<ProductStockCheckRequest> productStockCheckRequests =
        productFundingDtoRequests.stream()
            .map(
                productFundingDto ->
                    new ProductStockCheckRequest(
                        productFundingDto.getProductId()))
            .collect(Collectors.toList());

    GetStockQuantityCheckExceedRequestDto getStockQuantityCheckExceedRequestDto =
        new GetStockQuantityCheckExceedRequestDto(productStockCheckRequests);

    // project-service에서 재고 확인
    SuccessResponse<GetStockQuantityCheckExceedResponseDto> response =
        projectServiceClient.getStockQuantityCheckExceed(
            projectId, getStockQuantityCheckExceedRequestDto);

    List<ProductStockCheckResponse> productStockCheckResponseList =
        response.getData().getProductStockCheckResponses();

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
    Funding funding = createFundingRequest.toFundingEntity();
    Funding savedFunding = fundingRepository.save(funding);

    // (ProductFunding) DTO->ENTITY
    List<ProductFunding> productFundings =
        productFundingDtoRequests.stream()
            .map(productFundingDto -> productFundingDto.toEntity(savedFunding))
            .collect(Collectors.toList());
    productFundingRepository.saveAll(productFundings);

    // (SupporterWithUs) DTO -> ENTITY
    SupporterWithUs supporterWithUs = createFundingRequest.toSupporterWithUsEntity(savedFunding);
    supporterWithUsRepository.save(supporterWithUs);

    // TODO: 결제


    // project-service로 재고 update
    UpdateProductStockQuantityRequestDto updateProductStockQuantityRequestDto =
            new UpdateProductStockQuantityRequestDto(productStockUpdateRequests);

    SuccessResponse successResponse =
            projectServiceClient.updateStockQuantity(
                    updateProductStockQuantityRequestDto);

    return savedFunding.getFundingId();
  }

  public GetFundingDetailResponse getFundingDetailResponse(Long fundingId) {
    Funding findFunding =
        fundingRepository.findById(fundingId).orElseThrow(FundingEntityNotFoundException::new);

    List<ProductFunding> findProductFundings = productFundingRepository.findByFunding(findFunding);
    return GetFundingDetailResponse.fromEntity(findFunding, findProductFundings);
  }
}
