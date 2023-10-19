package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.dto.MemberFundingInformationDto;
import java.util.List;
import java.util.Map;

public interface FundingQueryRepository {
  Map<Long, List<Long>> findFundingMemberId(List<Long> projectId);

  Map<Long, Long> findFundingTotalAmount();

  Map<Long, Long> findProjectAchievementInfo(List<Long> projectIds);

  List<MemberFundingInformationDto> findMemberFundingInfo(Long projectId);
}
