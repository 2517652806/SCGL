package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.helper.JWTHelper;
import com.cddr.szd.login.ThreadLocalUtil;
import com.cddr.szd.mapper.FoodCompanyMapper;
import com.cddr.szd.mapper.FoodTypeMapper;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.exception.BizException;


import com.cddr.szd.model.AnalysisData;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.dto.Analysis;
import com.cddr.szd.model.vo.FoodTypeSearchVo;
import com.cddr.szd.service.FoodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodTypeServiceImpl implements FoodTypeService {
    @Autowired
    private FoodTypeMapper foodTypeMapper;
    @Autowired
    private FoodCompanyMapper foodCompany;
    @Override
    public void add(FoodType foodType){
        Permission.check(UserType.ADMIN.getCode());
        Permission.check(UserType.ADMIN.getCode());
        check(foodType);
        int insert = foodTypeMapper.insert(foodType);
        if (insert == 0){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }

    }


    public void check(FoodType foodType){

        LambdaQueryWrapper<FoodType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(FoodType::getFoodType, foodType.getFoodType())
                .eq(FoodType::getCompanyName,foodType.getCompanyName()); // 查询foodName等于指定值的记录
        if (foodTypeMapper.selectCount(lambdaQueryWrapper) > 0) {
            throw new BizException(BizCodeEnum.Failed_To_Add);
        }
    }




    @Override
    public IPage<FoodType> getFoodType(@Validated FoodTypeSearchVo foodTypeSearchVo){
        Permission.check(UserType.ADMIN.getCode());
        Page<FoodType> page = new Page<>(foodTypeSearchVo.getPageNum(), foodTypeSearchVo.getPageSize());
        if (foodTypeSearchVo!=null && foodTypeSearchVo.getTypeName()!=null){
            return foodTypeMapper.selectPage(page, new LambdaQueryWrapper<FoodType>()
                    .like(FoodType::getFoodType, foodTypeSearchVo.getTypeName())
                    .eq(FoodType::getCompanyName, JWTHelper.getCompanyName(ThreadLocalUtil.get().toString()))
            );
        }
        return foodTypeMapper.selectPage(page, new LambdaQueryWrapper<FoodType>().eq(FoodType::getCompanyName, JWTHelper.getCompanyName(ThreadLocalUtil.get().toString())));
    }
    //通过jwt传的useid，查有没有权限操作，管理员type可以操作
    @Override
    public void updateFoodType(FoodType newFoodType){
        Permission.check(UserType.ADMIN.getCode());
//        //校验数据是否合法
        FoodType foodType = foodTypeMapper.selectOne(new LambdaQueryWrapper<FoodType>()
                .eq(FoodType::getFoodType, newFoodType.getFoodType())
                .eq(FoodType::getCompanyName, JWTHelper.getCompanyName(ThreadLocalUtil.get().toString()))
        );
        if(foodType!=null&&!foodType.getId().equals(newFoodType.getId())){
            throw new BizException(BizCodeEnum.FAILED_TYPE_EXIST);
        }
        LambdaUpdateWrapper<FoodType> lambdaUpdateWrapper = Wrappers.<FoodType>lambdaUpdate()
                .eq(FoodType::getId, newFoodType.getId()) // 设置更新条件为 id 等于给定值
                .set(FoodType::getFoodType, newFoodType.getFoodType()); // 设置 name 字段的新值

        int result = foodTypeMapper.update(null, lambdaUpdateWrapper); // 执行更新操作
        if(result<=0){
            throw new BizException(BizCodeEnum.Failed_To_Update);
        }
    }
    @Override
    public void deleteFoodType(Integer id){
        Permission.check(UserType.ADMIN.getCode());
        FoodType foodType = foodTypeMapper.selectById(id);
        if (foodType == null){
            throw new BizException(100,"操作的记录不存在");
        }
        String token = ThreadLocalUtil.get();
        String companyName = JWTHelper.getCompanyName(token);
        if (!foodType.getCompanyName().equals(companyName)){
            throw new BizException(BizCodeEnum.No_Permissions_Are_Deleted);
        }
        foodTypeMapper.deleteById(id);
    }


    @Override
    public ArrayList<AnalysisData> getAnalysis(Integer type) {
        Permission.check(UserType.ADMIN.getCode());
        String token = ThreadLocalUtil.get();
        String companyName = JWTHelper.getCompanyName(token);
        if (type == 1){//总重量分布
            List<Analysis> analysisList = foodCompany.analysisByWeight(companyName);
            //对analysisList按照foodType分组并计算每组的value和
            Map<String, Double> collect = analysisList.stream().collect(Collectors.groupingBy(Analysis::getFoodType, Collectors.summingDouble(Analysis::getValue)));
            ArrayList<AnalysisData> analysisDataArrayList = new ArrayList<>();
            collect.forEach((k,v)->{
                AnalysisData analysisData = new AnalysisData(v, k);
                analysisDataArrayList.add(analysisData);
            });
            return analysisDataArrayList;
        }else if (type == 2){//未过期分布
            List<Analysis> analysisList = foodCompany.analysisByNotExpired(companyName);
            Map<String, Long> collect = analysisList.stream().collect(Collectors.groupingBy(Analysis::getFoodType, Collectors.counting()));
            ArrayList<AnalysisData> analysisDataArrayList = new ArrayList<>();
            collect.forEach((k,v)->{
                AnalysisData analysisData = new AnalysisData(v.doubleValue(), k);
                analysisDataArrayList.add(analysisData);
            });
            return analysisDataArrayList;
        } else if (type == 3) {//过期分布
            List<Analysis> analysisList = foodCompany.analysisByExpired(companyName);
            Map<String, Long> collect = analysisList.stream().collect(Collectors.groupingBy(Analysis::getFoodType, Collectors.counting()));
            ArrayList<AnalysisData> analysisDataArrayList = new ArrayList<>();
            collect.forEach((k,v)->{
                AnalysisData analysisData = new AnalysisData(v.doubleValue(), k);
                analysisDataArrayList.add(analysisData);
            });
            return analysisDataArrayList;

        }else {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }
}
