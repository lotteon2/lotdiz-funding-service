package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.dto.TotalAmountWithProjectIdDto;
import com.lotdiz.fundingservice.entity.Funding;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FundingRepository extends JpaRepository<Funding, Long>, FundingQueryRepository {
  @Query("select f from Funding f where f.projectId in :projectIds")
  List<Funding> findAllByProjectIdIsIn(@Param("projectIds") List<Long> projectIds);

  Page<Funding> findByMemberId(Long memberId, Pageable pageable);

  List<Funding> findByProjectId(Long projectId);

  @Query(
      value =
          "select new com.lotdiz.fundingservice.dto.TotalAmountWithProjectIdDto(f.projectId, sum (f.fundingTotalAmount)) "
              + "from Funding f "
              + "group by f.projectId",
      nativeQuery = true)
  List<TotalAmountWithProjectIdDto> findTotalFundingAmount();
}
