package com.yang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.reggie.common.BaseContext;
import com.yang.reggie.common.R;
import com.yang.reggie.entity.User;
import com.yang.reggie.service.UserService;
import com.yang.reggie.utils.SMSUtils;
import com.yang.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author yang97
 * @Date 2023/5/18 15:30
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码短信
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone =user.getPhone();
        log.info("手机号为{}",phone);
        if(StringUtils.isNotEmpty(phone)){
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("生成的验证码为code={}",code);
            //调用阿里云API发送短信
//            SMSUtils.sendMessage("快速学习登录验证","SMS_460756000",phone,code);
            //将生成的验证码保存到Session
            session.setAttribute("phone",code);
            return R.success("验证码发送成功！");
        }
        return R.error("验证码发送失败！");
    }

    /**
     * 手机端登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute("phone");
        //验证码校验
        if(codeInSession!=null && codeInSession.equals(code)){
            //验证码校验成功，判断当前用户是否为新用户
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        //
        return R.error("验证码输入错误！");
    }

    /**
     * 手机端退出
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功！");
    }


}
