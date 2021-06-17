package com.syc.blog.service.user;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.syc.blog.entity.user.UserAuth;
import com.syc.blog.mapper.user.UserAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAuthService {
    @Autowired
    UserAuthMapper userAuthMapper;
    public UserAuth selectByTypeAndIdentifier(String loginEmail, String email) {
        return userAuthMapper.selectOne(Wrappers.<UserAuth>lambdaQuery().eq(UserAuth::getIdentityType,loginEmail).eq(UserAuth::getIdentifier,email));
    }

    public int saveModifyPassword(String email, String password) {
        return 0;
    }

    @Transactional
    public int save(UserAuth userAuth) {
        return userAuthMapper.insert(userAuth);
    }

    public int update(UserAuth userAuth) {
        return userAuthMapper.updateById(userAuth);
    }
}
