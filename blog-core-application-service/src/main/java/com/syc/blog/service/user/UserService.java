package com.syc.blog.service.user;

import com.syc.blog.entity.user.User;
import com.syc.blog.entity.user.UserAuth;
import com.syc.blog.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserAuthService userAuthService;
    public User selectById(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Transactional
    public int save(User user, UserAuth userAuth) {
        int result = userMapper.insert(user);
        if(result != 0){
            userAuth.setUserId(user.getId());
            userAuthService.save(userAuth);
            return user.getId();//
        }
        return user.getId();
    }

    public int updateUserTencentInfo(Integer userId, String avatarURL50, String nickname) {
        return 0;
    }
}
