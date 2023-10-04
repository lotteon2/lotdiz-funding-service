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
@Table(name = "supporter_with_us")
public class SupporterWithUs extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supporter_with_us_id", unique = true, nullable = false)
    private Long supporterWithUsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id")
    private Long fundingId;

    @Column(name = "supporter_with_us_is_name_public", nullable = false)
    private Boolean supporterWithUsIsNamePublic;

    @Column(name = "supporter_with_us_is_amount_public", nullable = false)
    private Boolean supporterWithUsIsAmountPublic;
}
