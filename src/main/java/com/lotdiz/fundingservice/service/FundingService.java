package com.lotdiz.fundingservice.service;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequest;
import com.lotdiz.fundingservice.dto.common.ProductFundingDTO;
import com.lotdiz.fundingservice.dto.response.GetFundingDetailResponse;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import com.lotdiz.fundingservice.entity.SupporterWithUs;
import com.lotdiz.fundingservice.exception.FundingEntityNotFoundException;
import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
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

  /* 펀딩하기 */
  @Transactional
  public Long createFunding(CreateFundingRequest createFundingRequest) {

    /** TODO
     * 1) 재고 확인 -> 없다면 구매 불가, 있다면 재고 차감
     * 1. productID 찾아내기.
     * 2. 해당 product의 Stock이 사용자 quantity와 비교하여 충분한지 검사.
     * 3. 재고 불충분시 404를 반환하여, 사용자에게 구매 오류가 발생하였음을 알린다.
     * *orderProductManager => FundingProductManager 만들기
     */

    // (Funding) DTO->ENTITY
    Funding funding = createFundingRequest.toFundingEntity();
    Funding savedFunding = fundingRepository.save(funding);

    // (ProductFunding) DTO->ENTITY
    List<ProductFundingDTO> productFundingDTORequests = createFundingRequest.getProducts();
    List<ProductFunding> productFundings =
        productFundingDTORequests.stream()
            .peek(productFundingDTO -> System.out.println(productFundingDTO.toString()))
            .map(productFundingDTO -> productFundingDTO.toEntity(savedFunding))
            .collect(Collectors.toList());
    productFundingRepository.saveAll(productFundings);

    // (SupporterWithUs) DTO -> ENTITY
    SupporterWithUs supporterWithUs = createFundingRequest.toSupporterWithUsEntity(savedFunding);
    supporterWithUsRepository.save(supporterWithUs);

    return savedFunding.getFundingId();
  }

  public GetFundingDetailResponse getFundingDetailResponse(Long fundingId) {
    Funding findFunding =
        fundingRepository.findById(fundingId).orElseThrow(FundingEntityNotFoundException::new);

    List<ProductFunding> findProductFundings = productFundingRepository.findByFunding(findFunding);
    return GetFundingDetailResponse.fromEntity(findFunding, findProductFundings);
  }
}
