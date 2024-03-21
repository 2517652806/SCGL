package com.cddr.szd.common;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Permissions;

public class Permission {
    public Boolean check(int choice){//0为管理员端，1为企业用户端，2为家庭用户


        DecodedJWT o = ThreadLocalUtil.get();

        Integer type = o.getClaim("type").asInt();
        Integer id = o.getClaim("id").asInt();

        if(choice == 0){
            if (type.equals(UserType.ADMIN.getCode())){
                throw new BizException(BizCodeEnum.Wrong_Role);
            }
            return true;
        }
        else if (choice == 1){
            if (type.equals(UserType.EMPLOYEE.getCode())){
                throw new BizException(BizCodeEnum.Wrong_Role);
            }
            return true;
        } else if (choice == 2) {
            if (type.equals(UserType.USER.getCode())){
                throw new BizException(BizCodeEnum.Wrong_Role);
            }
            return true;
        }
        else {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }


    }



}
