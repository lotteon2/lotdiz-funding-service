package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductFundingRepository extends JpaRepository<ProductFunding, Long> {
  List<ProductFunding> findByFunding(Funding funding);

}
