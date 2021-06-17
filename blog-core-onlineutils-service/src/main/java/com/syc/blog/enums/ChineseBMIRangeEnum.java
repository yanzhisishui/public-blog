package com.syc.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum ChineseBMIRangeEnum {
    SKIN(0,18.4,"偏瘦"),
    NORMAL(18.5,23.9,"正常"),
    WEIGHT(24.0,27.9,"过重"),
    FAT(28.0,100,"肥胖");

    @Getter
    private double low;

    @Getter
    private double high;

    @Getter
    private String desc;


    public static String getDescByValue(double value) {
        if (value <= SKIN.high && value >= SKIN.low){
           return SKIN.desc;
       } else if (value <= NORMAL.high && value >= NORMAL.low){
           return NORMAL.desc;
       } else if (value <= WEIGHT.high && value >= WEIGHT.low){
           return WEIGHT.desc;
       } else if (value <= FAT.high && value >= FAT.low) {
            return FAT.desc;
        }
        return "未查出结果";
    }
}
