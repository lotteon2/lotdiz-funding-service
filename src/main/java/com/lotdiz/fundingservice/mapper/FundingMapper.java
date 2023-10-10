package com.lotdiz.fundingservice.mapper;

import com.lotdiz.fundingservice.dto.request.CreateFundingRequest;
import com.lotdiz.fundingservice.entity.Funding;
import com.lotdiz.fundingservice.entity.ProductFunding;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FundingMapper {
    FundingMapper INSTANCE = Mappers.getMapper(FundingMapper.class);

    @Mapping(target= "id", ignore=true)
    // Funding 타입으로 매핑.
    Funding createFundingRequestDtoToFunding(CreateFundingRequest createFundingRequest);

}
