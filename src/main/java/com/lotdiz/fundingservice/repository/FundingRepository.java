package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingRepository extends JpaRepository<Funding, Long> {}
