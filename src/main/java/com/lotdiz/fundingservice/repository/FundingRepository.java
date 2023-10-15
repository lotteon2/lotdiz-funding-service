package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.entity.Funding;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FundingRepository extends JpaRepository<Funding, Long> {

  @Query("select f from Funding f where f.projectId in :projectIds")
  List<Funding> findAllByProjectIdIsIn(@Param("projectIds") List<Long> projectIds);
  Page<Funding> findByMemberId(Long memberId, Pageable pageable);
  List<Funding> findByProjectId(Long projectId);
  Funding findByFundingId(Long fundingId);
}
