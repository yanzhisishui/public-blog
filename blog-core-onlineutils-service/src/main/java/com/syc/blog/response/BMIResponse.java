package com.syc.blog.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BMIResponse {

    private BigDecimal value;//bmi值

    private String desc;//身体状态描述
}
