package com.syc.blog.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BMIRangeResponse {
    private BigDecimal low;
    private BigDecimal height;
    private String desc;
}
