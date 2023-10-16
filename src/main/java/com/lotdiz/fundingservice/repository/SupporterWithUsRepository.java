package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.entity.SupporterWithUs;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupporterWithUsRepository extends JpaRepository<SupporterWithUs, Long> {

    @Query("select s from SupporterWithUs s where s.funding.fundingId in :fundingIds")
    List<SupporterWithUs> findAllByFundingIdIsIn(@Param("fundingIds") List<Long> fundingIds);

}
