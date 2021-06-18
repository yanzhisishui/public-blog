package com.syc.blog.mapper.onlineutils;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syc.blog.entity.onlineutils.Provinces;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProvincesMapper extends BaseMapper<Provinces> {
    @Select(" select REPLACE(GROUP_CONCAT(t.code),',','') from ( " +
            "        SELECT * FROM `provinces`  where id = #{provinceId} " +
            "        union " +
            "        SELECT * FROM `provinces`  where id = #{cityId} " +
            "        union " +
            "        SELECT * FROM `provinces`  where id = #{districtId} " +
            "        ) t")
    String selectDistrictPrefix(@Param("provinceId") Integer provinceId, @Param("cityId")Integer cityId, @Param("districtId") Integer districtId);

    @Select("select children_code from provinces where id = #{districtId} and archive = 0")
    String selectChildrenCodeById(@Param("districtId") Integer districtId);
}
