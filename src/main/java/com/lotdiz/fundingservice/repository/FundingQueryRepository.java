package com.lotdiz.fundingservice.repository;

import com.lotdiz.fundingservice.dto.MemberFundingInformationDto;
import java.util.List;

public interface FundingQueryRepository {

    List<MemberFundingInformationDto> findMemberFundingInfo(Long projectId);
}
