package com.syc.blog.service.sms.email;

import com.syc.blog.constants.Constant;
import com.syc.blog.utils.ResultHelper;
import com.syc.blog.utils.VCodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailSmsCodeService {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public ResultHelper send(String email,String from) {
        SimpleMailMessage message=new SimpleMailMessage();
        VCodeHelper vCode = new VCodeHelper(6);
        String dynamicCode=vCode.createNumCode();//即将发送的验证码
        ResultHelper resultHelper;
        try{
            /**
             * 加上邮箱计数，防止坏蛋恶意发邮件
             * */
            Boolean flag = stringRedisTemplate.hasKey(Constant.SMS_COUNT + email);
            if(flag != null && flag){
                String s = stringRedisTemplate.opsForValue().get(Constant.SMS_COUNT + email);
                int count = Integer.parseInt(s);
                if(count >= 5){ //超过限制,不允许发送邮件
                    log.info("当前邮箱 : {} 已经超过单日允许邮件最大限度",email);
                    resultHelper= ResultHelper.wrapErrorResult(1,"发送邮件失败，邮箱已经超越单日限制最大次数5");
                    return resultHelper;
                }else{
                    stringRedisTemplate.opsForValue().set(Constant.SMS_COUNT+email,String.valueOf(count+1));
                }
            }else{
                stringRedisTemplate.opsForValue().set(Constant.SMS_COUNT+email,"1");
            }

            message.setSubject("暮色妖娆丶博客注册验证");
            message.setText("您的暮色妖娆丶博客网站动态码为\t"+dynamicCode+","+
                    "十分钟内有效,"+"如果这不是您本人的操作，请忽略");
            message.setFrom(from);
            message.setTo(email);
            mailSender.send(message);
            stringRedisTemplate.opsForValue().set(Constant.SMS_EMAIL+email,dynamicCode,10, TimeUnit.MINUTES);
            resultHelper= ResultHelper.wrapSuccessfulResult(null);
        }catch (Exception e) {
            log.error("发送邮件失败,腾讯SMTP服务出现问题，请暂时使用QQ登录",e);
            resultHelper= ResultHelper.wrapErrorResult(1,"发送邮件失败,腾讯SMTP服务出现问题，请暂时使用QQ登录");
        }
        return resultHelper;
    }
}
