package com.lotdiz.fundingservice.repository;

import static com.lotdiz.fundingservice.entity.QFunding.funding;

import com.lotdiz.fundingservice.dto.MemberFundingInformationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class FundingQueryRepositoryImpl implements FundingQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public FundingQueryRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<MemberFundingInformationDto> findMemberFundingInfo(Long projectId) {
    return jpaQueryFactory
        .select(
            Projections.constructor(
                MemberFundingInformationDto.class,
                funding.memberId,
                funding.fundingId,
                funding.fundingTotalAmount,
                funding.createdAt.as("fundingDate")))
        .from(funding)
        .where(funding.projectId.eq(projectId))
        .fetch();
  }
}
