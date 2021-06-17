package com.syc.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum InternationalBMIRangeEnum {
    SKELETONIZE(0,16.4,"极瘦"),
    SKIN(16.5,18.4,"偏瘦"),
    NORMAL(18.5,24.9,"正常"),
    WEIGHT(25.0,29.9,"过重"),
    FAT_1(30.0,34.9,"一类肥胖"),
    FAT_2(35.0,39.0,"二类肥胖"),
    FAT_3(40.0,100,"三类肥胖");

    @Getter
    private double low;

    @Getter
    private double high;

    @Getter
    private String desc;


    public static String getDescByValue(double value) {
       if(value <= SKELETONIZE.high && value >= SKELETONIZE.low){
            return SKELETONIZE.desc;
       } else if (value <= SKIN.high && value >= SKIN.low){
           return SKIN.desc;
       } else if (value <= NORMAL.high && value >= NORMAL.low){
           return NORMAL.desc;
       } else if (value <= WEIGHT.high && value >= WEIGHT.low){
           return WEIGHT.desc;
       } else if (value <= FAT_1.high && value >= FAT_1.low){
           return FAT_1.desc;
       } else if (value <= FAT_2.high && value >= FAT_2.low){
           return FAT_2.desc;
       }  else if (value <= FAT_3.high && value >= FAT_3.low){
           return FAT_3.desc;
       }
        return "未查出结果";
    }
}
