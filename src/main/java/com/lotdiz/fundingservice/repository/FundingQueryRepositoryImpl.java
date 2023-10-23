package com.lotdiz.fundingservice.repository;

import static com.lotdiz.fundingservice.entity.QFunding.funding;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.sum;

import com.lotdiz.fundingservice.dto.MemberFundingInformationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

public class FundingQueryRepositoryImpl implements FundingQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public FundingQueryRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Map<Long, List<Long>> findFundingMemberId(List<Long> projectIds) {
    return jpaQueryFactory
        .from(funding)
        .where(funding.projectId.in(projectIds))
        .transform(groupBy(funding.projectId).as(list(funding.memberId)));
  }

  @Override
  public Map<Long, Long> findFundingTotalAmount() {
    return jpaQueryFactory
        .from(funding)
        .transform(groupBy(funding.projectId).as(sum(funding.fundingTotalAmount)));
  }

  @Override
  public Map<Long, Long> findProjectAchievementInfo(List<Long> projectIds) {
    return jpaQueryFactory
        .from(funding)
        .where(funding.projectId.in(projectIds))
        .transform(groupBy(funding.projectId).as(sum(funding.fundingTotalAmount)));
  }

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
