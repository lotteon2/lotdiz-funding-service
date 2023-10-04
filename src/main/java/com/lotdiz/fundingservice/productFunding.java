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
@Table(name = "product_funding")
public class productFunding extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_funding_id", unique = true, nullable = false)
    private Long productFundingId;

    @Column(name = "product_id", unique = true, nullable = false)
    private Long productId;

    @Column(name = "product_funding_price", unique = true, nullable = false)
    private Long productFundingPrice;

    @Column(name = "product_funding_quantity", unique = true, nullable = false)
    private Long productFundingQuantity;
}
