package com.lotdiz.fundingservice.entity;

import com.lotdiz.fundingservice.entity.common.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_funding")
public class ProductFunding extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_funding_id")
  private Long productFundingId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "funding_id")
  private Funding funding;

  @Column(name = "product_funding_price")
  private Long productFundingPrice;

  @Column(name = "product_funding_quantity")
  private Long productFundingQuantity;
}
