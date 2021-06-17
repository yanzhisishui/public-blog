package com.syc.blog.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MortgageResponse {
    private BigDecimal totalAmount;//还款总额

    private BigDecimal interestTotal;//利息总额

    private List<MortgageItem> itemList;

    @Data
    public static class MortgageItem{
        private int index;
        private BigDecimal interest;
        private BigDecimal capital;
        private BigDecimal monthTotal;
    }
}
