package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.syc.blog.controller.BaseController;
import com.syc.blog.enums.ChineseBMIRangeEnum;
import com.syc.blog.enums.InternationalBMIRangeEnum;
import com.syc.blog.response.BMIRangeResponse;
import com.syc.blog.response.BMIResponse;
import com.syc.blog.utils.ResultHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/util/bmi")
public class BMIController extends BaseController {

    @RequestMapping("")
    public String mortgage(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
            , @RequestParam("bindId") Integer bindId) {
        byte type = 17;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/bmi";
    }

    /**
     * @param type 标准类型 0：国际标准 1：中国标准
     * @param height 身高：单位cm
     * @param weight 体重
     * @param unit 体重单位：0：kg 1：斤
     *  weight(kg) / (height [cm])²
     * */
    @GetMapping("/calc")
    @ResponseBody
    public String calcBmi(@RequestParam("height")BigDecimal height,
                          @RequestParam("weight")BigDecimal weight,
                          @RequestParam("type")Integer type,
                          @RequestParam("unit")Integer unit){

        double pow = Math.pow(height.doubleValue()/100, 2);
        if(unit == 1){
            weight = weight.divide(new BigDecimal(2));
        }
        BigDecimal bmi = weight.divide(new BigDecimal(pow), 1, BigDecimal.ROUND_HALF_UP);
        BMIResponse info = new BMIResponse();
        info.setValue(bmi);
        info.setDesc(getBMIDesc(type,bmi));
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(info));
    }

    /**
     * 根据bmi值和标准类型获取结果
     * @param type 标准类型 0：国际标准 1：中国标准
     * */
    private String getBMIDesc(Integer type, BigDecimal bmi) {
        if(type == 1){
           return ChineseBMIRangeEnum.getDescByValue(bmi.doubleValue());
        } else {
            return InternationalBMIRangeEnum.getDescByValue(bmi.doubleValue());
        }
    }

    @GetMapping("/range")
    @ResponseBody
    public String range(@RequestParam("type") Integer type){
        List<BMIRangeResponse> list = new ArrayList<>();
        if(type == 1){
            ChineseBMIRangeEnum[] values = ChineseBMIRangeEnum.values();
            Arrays.stream(values).forEach(v -> {
                BMIRangeResponse item = new BMIRangeResponse();
                item.setLow(BigDecimal.valueOf(v.getLow()));
                item.setHeight(BigDecimal.valueOf(v.getHigh()));
                item.setDesc(v.getDesc());
                list.add(item);
            });
        } else{
            InternationalBMIRangeEnum[] values = InternationalBMIRangeEnum.values();
            Arrays.stream(values).forEach(v -> {
                BMIRangeResponse item = new BMIRangeResponse();
                item.setLow(BigDecimal.valueOf(v.getLow()));
                item.setHeight(BigDecimal.valueOf(v.getHigh()));
                item.setDesc(v.getDesc());
                list.add(item);
            });
        }
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(list));
    }
}
