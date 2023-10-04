package com.lotdiz.fundingservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "funding")
public class Funding extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funding_id", unique = true, nullable = false)
    private Long fundingId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "funding_supporter_email", nullable = false)
    private String fundingSupporterEmail;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "funding_status", nullable = false)
    private FundingStatus fundingStatus = FundingStatus.COMPLETED;

    @Column(name = "funding_total_amount", nullable = false)
    private Long fundingTotalAmount;

    @Column(name = "funding_is_refundable", nullable = false)
    private Boolean fundingIsRefundable;

    @Column(name = "funding_support_amount", nullable = false)
    private Long fundingSupportAmount;

    @Column(name = "funding_lotdeal_discount_amount", nullable = false)
    private Long fundingLotdealDiscountAmount;

    @Column(name = "funding_membership_discount_amount", nullable = false)
    private Long fundingMembershipDiscountAmount;

    @Column(name = "funding_used_point", nullable = false)
    private Long fundingUsedPoint;

    @Column(name = "funding_privacy_agreement", nullable = false)
    private Boolean fundingPrivacyAgreement;

}
