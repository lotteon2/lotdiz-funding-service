package com.lotdiz.fundingservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;

public class FundingQueryRepositoryImpl implements FundingQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  public FundingQueryRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }
}
