package com.lotdiz.fundingservice.service.manager;

import com.lotdiz.fundingservice.exception.FundingNotRefundableException;
import com.lotdiz.fundingservice.exception.FundingProductNotEnoughStockQuantityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundingProductManager {

    public void checkEnoughStockQuantity(Long productFundingQuantity, Long stockQuantity){
        if(stockQuantity - productFundingQuantity < 0){
            throw new FundingProductNotEnoughStockQuantityException();
        }
    }

    public void checkIfRefundable(Boolean isLotdeal){
        if(isLotdeal){
            throw new FundingNotRefundableException();
        }
    }
}
