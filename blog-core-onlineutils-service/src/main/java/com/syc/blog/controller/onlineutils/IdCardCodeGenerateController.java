package com.syc.blog.controller.onlineutils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.controller.BaseController;
import com.syc.blog.entity.onlineutils.Provinces;
import com.syc.blog.mapper.onlineutils.ProvincesMapper;
import com.syc.blog.utils.ResultHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/util/idCardCode")
public class IdCardCodeGenerateController extends BaseController {

    @Autowired
    ProvincesMapper provincesMapper;
    public static final int[] FLAG = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1};
    public static final int MOD_NUMBER = 11;

    @RequestMapping("")
    public String idCardCode(ModelMap map, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page,
                           @RequestParam("bindId") Integer bindId){
        byte type = 11;
        getCurrentCommentsListPage(map,page,bindId,type);
        map.put("bindId",bindId);
        return "onlineutils/idcardcode";
    }

    @RequestMapping("/getDayCountByYearAndMonth")
    @ResponseBody
    public String getDayCountByYearAndMonth(@RequestParam("year") String year, @RequestParam("month") String month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date parse = sdf.parse(year + "-" + month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        return  String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    @RequestMapping("/getCitysByParentId")
    @ResponseBody
    public String getCitysByParentId(@RequestParam("provinceId") Integer provinceId){
        List<Provinces> list = provincesMapper.selectList(Wrappers.<Provinces>lambdaQuery().eq(Provinces::getParentId,provinceId).eq(Provinces::getArchive,0));
        ResultHelper result = ResultHelper.wrapSuccessfulResult(list);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getProvincesByLevel")
    @ResponseBody
    public String getProvincesByLevel(@RequestParam("level") Integer level){
        List<Provinces> list = provincesMapper.selectList(Wrappers.<Provinces>lambdaQuery().eq(Provinces::getLevel,level).eq(Provinces::getArchive,0));
        ResultHelper result = ResultHelper.wrapSuccessfulResult(list);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/generate")
    @ResponseBody
    public String generate(@RequestParam("province") Integer provinceId,
                           @RequestParam("city") Integer cityId,
                           @RequestParam("district") Integer districtId,
                           @RequestParam("year") String year,
                           @RequestParam("month") String month,
                           @RequestParam("day") String day,
                           @RequestParam("sex") Integer sex,
                           @RequestParam(value = "count",defaultValue = "5") Integer count, HttpServletRequest request){


        ResultHelper result = null ;

        //生成身份证号码
        Map<String,Object> params = new HashMap<>();
        params.put("provinceId",provinceId);
        params.put("cityId",cityId);
        params.put("districtId",districtId);
        params.put("year",year);
        params.put("month",month);
        params.put("day",day);
        params.put("sex",sex);
        params.put("count",count);

        String prefix = provincesMapper.selectDistrictPrefix(provinceId,cityId,districtId);//341122
        String birthday = year+month+day;//19960807
        String[] childrenCodeArr=provincesMapper.selectChildrenCodeById(districtId).split(",");//派出所代码
        String[] sexCode = sex == 0 ? new String[]{"0","2","4","6","8"} : new String[]{"1","3","5","7","9"};//性别可选代码

        List<String> preList = new ArrayList<>();//前17位满足的身份证
        outer:
        for (String s : childrenCodeArr) {
            for (String s1 : sexCode) {
                preList.add(prefix + birthday + s + s1);
                if (preList.size() >= count) {
                    break outer;
                }
            }
        }


        List<String> targetList = new ArrayList<>();
        for(String str : preList){ //遍历满足条件的前17位身份证
            char[] c = str.toCharArray();
            int[] tar = new int[17];
            for(int i=0;i<tar.length;i++){
                tar[i] = Integer.parseInt(String.valueOf(c[i]));
            }
            int sum = 0;
            for(int i=0 ; i < tar.length ; i++){
                sum += tar[i] * FLAG[i]; // 前17位系数的和
            }
            String endCode ="";
            //计算最后一位数字
            int mod = sum % MOD_NUMBER;
            switch (mod){
                case 0:
                    endCode = "1";
                    break;
                case 1:
                    endCode="0";
                    break;
                case 2:
                    endCode="X";
                    break;
                case 3:
                    endCode="9";
                    break;
                case 4:
                    endCode="8";
                    break;
                case 5:
                    endCode="7";
                    break;
                case 6:
                    endCode="6";
                    break;
                case 7:
                    endCode = "5";
                    break;
                case 8:
                    endCode = "4";
                    break;
                case 9:
                    endCode="3";
                    break;
                case 10:
                    endCode = "2";
                    break;
            }
            targetList.add(str+endCode);
        }
        result= ResultHelper.wrapSuccessfulResult(targetList);

       return JSON.toJSONString(result);
    }
}
