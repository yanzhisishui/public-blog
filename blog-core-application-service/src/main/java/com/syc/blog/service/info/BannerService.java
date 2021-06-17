package com.syc.blog.service.info;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.info.Banner;
import com.syc.blog.mapper.info.BannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    @Autowired
    BannerMapper bannerMapper;
    public List<Banner> selectList() {
        return bannerMapper.selectList(Wrappers.<Banner>lambdaQuery().eq(Banner::getArchive,0));
    }
}
