package com.cddr.szd.service.Impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;

import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.mapper.HomeUserMapper;
import com.cddr.szd.model.Food;
import com.cddr.szd.model.vo.FoodVo;
import com.cddr.szd.result.ResultCodeEnum;
import com.cddr.szd.service.HomeUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeUserServiceImpl implements HomeUserService {
    @Autowired
    private HomeUserMapper homeUserMapper;

    @Override
    public void addFood(FoodVo foodVo){


        //从本地线程中获取token
        DecodedJWT o = ThreadLocalUtil.get();
        //从token中获取到id和type
        int userId = o.getClaim("user_id").asInt();
        Integer type = o.getClaim("type").asInt();
        if (type.equals(UserType.USER.getCode())){
            throw new BizException(BizCodeEnum.Wrong_Role);
        }
        Food food = new Food();

        BeanUtils.copyProperties(foodVo,food);
        food.setUserId(userId);

        int insert = homeUserMapper.insert(food);
        if (insert == 0){
            throw new BizException(BizCodeEnum.Failed_To_Add);
        }

    }
}
