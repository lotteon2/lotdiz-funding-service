package com.lotdiz.fundingservice.entity;

import com.lotdiz.fundingservice.repository.FundingRepository;
import com.lotdiz.fundingservice.repository.ProductFundingRepository;
import com.lotdiz.fundingservice.repository.SupporterWithUsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FundingTest {

  @Autowired private EntityManager em;
  @Autowired private FundingRepository fundingRepository;
  @Autowired private ProductFundingRepository productFundingRepository;
  @Autowired private SupporterWithUsRepository supporterWithUsRepository;

  @Test
  @DisplayName("fundingCreateTest")
  void fundingCreate() {
    Funding funding =
        Funding.builder()
            .memberId(1L)
            .projectId(1L)
            .fundingSupporterEmail("abc@gmail.com")
            .fundingStatus(FundingStatus.COMPLETED)
            .fundingTotalAmount(78000L)
            .fundingIsRefundable(Boolean.FALSE)
            .fundingSupportAmount(0L)
            .fundingLotdealDiscountAmount(0L)
            .fundingMembershipDiscountAmount(1000L)
            .fundingUsedPoint(0L)
            .fundingPrivacyAgreement(Boolean.TRUE)
            .build();

    fundingRepository.save(funding); // 영속성 컨텍스트에 저장
    // DB에 저장
    em.flush();
    em.clear();

    Funding findFunding = fundingRepository.findById(funding.getFundingId()).get();
    Assertions.assertThat(findFunding.getFundingId()).isSameAs(funding.getFundingId());
  }

  @Test
  @DisplayName("productFundingCreateTest")
  void productFundingCreate() {
    Funding funding =
        Funding.builder()
            .memberId(1L)
            .projectId(1L)
            .fundingSupporterEmail("abc@gmail.com")
            .fundingStatus(FundingStatus.COMPLETED)
            .fundingTotalAmount(78000L)
            .fundingIsRefundable(Boolean.FALSE)
            .fundingSupportAmount(0L)
            .fundingLotdealDiscountAmount(0L)
            .fundingMembershipDiscountAmount(1000L)
            .fundingUsedPoint(0L)
            .fundingPrivacyAgreement(Boolean.TRUE)
            .build();

    fundingRepository.save(funding);

    ProductFunding productFunding =
        ProductFunding.builder()
            .productId(1L)
            .funding(funding)
            .productFundingPrice(9000L)
            .productFundingQuantity(2L)
            .build();

    productFundingRepository.save(productFunding);
    em.flush();
    em.clear();

    ProductFunding findProductFunding =
        productFundingRepository.findById(productFunding.getProductFundingId()).get();
    Assertions.assertThat(findProductFunding.getProductFundingId())
        .isSameAs(productFunding.getProductFundingId());
  }

  @Test
  @DisplayName("supporterWithUsCreateTest")
  void supporterWithUsCreate() {
    Funding funding =
        Funding.builder()
            .memberId(1L)
            .projectId(1L)
            .fundingSupporterEmail("abc@gmail.com")
            .fundingStatus(FundingStatus.COMPLETED)
            .fundingTotalAmount(78000L)
            .fundingIsRefundable(Boolean.FALSE)
            .fundingSupportAmount(0L)
            .fundingLotdealDiscountAmount(0L)
            .fundingMembershipDiscountAmount(1000L)
            .fundingUsedPoint(0L)
            .fundingPrivacyAgreement(Boolean.TRUE)
            .build();

    fundingRepository.save(funding);

    SupporterWithUs supporterWithUs =
        SupporterWithUs.builder()
            .funding(funding)
            .supporterWithUsIsNamePublic(true)
            .supporterWithUsIsAmountPublic(true)
            .build();

    supporterWithUsRepository.save(supporterWithUs);
    em.flush();
    em.clear();

    SupporterWithUs findSupporterWithUs =
        supporterWithUsRepository.findById(supporterWithUs.getSupporterWithUsId()).get();
    Assertions.assertThat(findSupporterWithUs.getSupporterWithUsId())
        .isSameAs(supporterWithUs.getSupporterWithUsId());
  }
}
