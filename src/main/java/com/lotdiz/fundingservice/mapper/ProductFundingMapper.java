package com.lotdiz.fundingservice.mapper;

import com.lotdiz.fundingservice.dto.common.ProductFundingDTO;
import com.lotdiz.fundingservice.entity.ProductFunding;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductFundingMapper {
    ProductFundingMapper INSTANCE = Mappers.getMapper(ProductFundingMapper.class);

    // ProductFunding 타입
    ProductFunding createFundingRequestDtoToProductFunding(
            ProductFundingDTO productFundingDTO);


}
