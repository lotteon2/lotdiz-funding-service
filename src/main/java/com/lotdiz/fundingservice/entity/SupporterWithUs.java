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
@Table(name = "supporter_with_us")
public class SupporterWithUs extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supporter_with_us_id")
  private Long supporterWithUsId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "funding_id")
  private Funding funding;

  @Column(name = "supporter_with_us_is_name_public", nullable = false)
  private Boolean supporterWithUsIsNamePublic;

  @Column(name = "supporter_with_us_is_amount_public", nullable = false)
  private Boolean supporterWithUsIsAmountPublic;
}
