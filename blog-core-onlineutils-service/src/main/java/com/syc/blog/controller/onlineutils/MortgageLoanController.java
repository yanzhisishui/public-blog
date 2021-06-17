package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.syc.blog.controller.BaseController;
import com.syc.blog.response.MortgageResponse;
import com.syc.blog.utils.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/util/mortgage/loan")
@Slf4j
public class MortgageLoanController extends BaseController {


    @RequestMapping("")
    public String mortgage(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page
            , @RequestParam("bindId") Integer bindId) {
        byte type = 16;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/mortgage_loan";
    }

    /**
     * @param loanTotal 贷款总额
     * @param type 还款方式 1：等额本息 2：等额本金
     * @param annualRate 年利率
     * @param year 还款分期（年）
     * */
    @GetMapping("/calc")
    @ResponseBody
    public String calc(@RequestParam(value = "loanTotal",required = false,defaultValue = "600000") BigDecimal loanTotal,
                       @RequestParam(value = "annualRate",required = false,defaultValue = "3.25") BigDecimal annualRate,
                       @RequestParam(value = "year",required = false,defaultValue = "30") Integer year,
                       @RequestParam(value = "type",required = false,defaultValue = "1") Integer type
    ){
       try {
           year = year * 12; //期数
           annualRate = annualRate.divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP);
           if(type == 1){
               return calcMortgageEqualInterest(loanTotal,annualRate,year);
           } else if(type ==2){
               return calcMortgageEqualCapital(loanTotal,annualRate,year);
           }
       }catch (Exception e){
           e.printStackTrace();
           log.error("计算贷款信息失败",e);
           return JSON.toJSONString(ResultHelper.wrapErrorResult(1,"计算异常"));
       }
       return null;
    }

    /**
     * 等额本金
     * */
    private String calcMortgageEqualCapital(BigDecimal loanTotal, BigDecimal annualRate, Integer year) {
        BigDecimal totalInterestAmount = BigDecimal.ZERO;
        BigDecimal originLoanTotal = BigDecimal.valueOf(loanTotal.doubleValue());
        BigDecimal capital = loanTotal.divide(new BigDecimal(year), 2, BigDecimal.ROUND_HALF_UP);//本金

        MortgageResponse info = new MortgageResponse();
        List<MortgageResponse.MortgageItem> list = new ArrayList<>();

        for(int i=1;i<=year;i++){
            BigDecimal interest = loanTotal.multiply(annualRate).divide(new BigDecimal(12),2, BigDecimal.ROUND_HALF_UP);//利息
            MortgageResponse.MortgageItem item = new MortgageResponse.MortgageItem();
            item.setIndex(i);
            item.setCapital(capital);
            item.setInterest(interest);
            item.setMonthTotal(interest.add(capital));
            totalInterestAmount = totalInterestAmount.add(interest);
            loanTotal = loanTotal.subtract(capital);
            list.add(item);
        }

        info.setInterestTotal(totalInterestAmount);
        info.setItemList(list);
        info.setTotalAmount(totalInterestAmount.add(originLoanTotal));
        return JSON.toJSONString(ResultHelper.wrapSuccessfulResult(info));
    }

    /**
     * 等额本息
     * */
    private String calcMortgageEqualInterest(BigDecimal loanTotal, BigDecimal annualRate, Integer year) {
        BigDecimal monthRate = annualRate.divide(new BigDecimal(12),10, BigDecimal.ROUND_HALF_UP);//月利率
        double pow = Math.pow(new BigDecimal("1").add(monthRate).doubleValue(), year);//（1+月利率）²
        BigDecimal step2 = new BigDecimal(String.valueOf(pow)).subtract(new BigDecimal("1"));
        BigDecimal interestAndCapital = loanTotal.multiply(monthRate).multiply(new BigDecimal(String.valueOf(pow))).divide(step2, 2, BigDecimal.ROUND_HALF_UP);//利息
        MortgageResponse info = new MortgageResponse();
        List<MortgageResponse.MortgageItem> list = new ArrayList<>();

        BigDecimal interestTotal = BigDecimal.ZERO;
        for(int i= 1;i<=year ;i++){
            BigDecimal interest = loanTotal.multiply(annualRate).divide(new BigDecimal(12),2,BigDecimal.ROUND_HALF_UP);
            MortgageResponse.MortgageItem item = new MortgageResponse.MortgageItem();
            item.setIndex(i);
            item.setInterest(interest);
            item.setCapital(interestAndCapital.subtract(interest));
            item.setMonthTotal(interestAndCapital);
            loanTotal = loanTotal.subtract(interestAndCapital.subtract(interest));
            list.add(item);
            interestTotal = interestTotal.add(interest);
        }
        info.setItemList(list);
        info.setTotalAmount(interestAndCapital.multiply(new BigDecimal(year)));
        info.setInterestTotal(interestTotal);
        ResultHelper<MortgageResponse> result = ResultHelper.wrapSuccessfulResult(info);
        return JSON.toJSONString(result);
    }

}
