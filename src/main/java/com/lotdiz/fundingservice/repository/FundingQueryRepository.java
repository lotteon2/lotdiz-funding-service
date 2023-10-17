package com.lotdiz.fundingservice.repository;

import java.util.List;
import java.util.Map;

public interface FundingQueryRepository {
  Map<Long, List<Long>> findFundingMemberId(List<Long> projectId);
  Map<Long, Long> findFundingTotalAmount();
}
