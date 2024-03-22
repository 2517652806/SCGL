package com.cddr.szd.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cddr.szd.common.Permission;
import com.cddr.szd.enums.ExpiredStatusEnum;
import com.cddr.szd.enums.UserType;
import com.cddr.szd.mapper.FoodCompanyMapper;
import com.cddr.szd.mapper.FoodTypeMapper;
import com.cddr.szd.mapper.UserMapper;
import com.cddr.szd.model.*;
import com.cddr.szd.model.dto.Analysis;
import com.cddr.szd.service.FoodService;
import com.cddr.szd.enums.BizCodeEnum;
import com.cddr.szd.enums.PermissionEnum;
import com.cddr.szd.exception.BizException;
import com.cddr.szd.helper.JWTHelper;
import com.cddr.szd.login.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodCompanyMapper foodCompanyMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FoodTypeMapper foodTypeMapper;
    @Override
    public void purchaseFood(FoodVo foodVo) {
        Permission.check(UserType.EMPLOYEE.getCode());
        User user = permissionVerify(PermissionEnum.ADD_PERMISSION.getCode());
        FoodCompany foodCompany = new FoodCompany();
        BeanUtils.copyProperties(foodVo, foodCompany);
        foodCompany.setCompanyName(user.getCompanyName());
        int insert = foodCompanyMapper.insert(foodCompany);
        if (insert != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public List<FoodType> getAllFoodType() {
        Permission.check(UserType.EMPLOYEE.getCode());
        String token = ThreadLocalUtil.get();
        String companyName = JWTHelper.getCompanyName(token);
        if (companyName == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        return foodTypeMapper.selectList(new LambdaQueryWrapper<FoodType>().eq(FoodType::getCompanyName,companyName));
    }

    @Override
    public void updateFood(FoodVo foodVo) {
        Permission.check(UserType.EMPLOYEE.getCode());
        User user = permissionVerify(PermissionEnum.UPDATE_PERMISSION.getCode());
        FoodCompany foodCompany = foodCompanyMapper.selectById(foodVo.getId());
        if (foodCompany == null|| !foodCompany.getCompanyName().equals(user.getCompanyName())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        BeanUtils.copyProperties(foodVo, foodCompany);
        if (!foodCompany.getExpirationTime().isBefore(LocalDate.now())){
            foodCompany.setExpiredImpression(ExpiredStatusEnum.NOT_EXPIRED.getCode());
        }
        int i = foodCompanyMapper.updateById(foodCompany);
        if (i != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public void deleteFood(Integer id) {
        Permission.check(UserType.EMPLOYEE.getCode());
        User user = permissionVerify(PermissionEnum.DELETE_PERMISSION.getCode());
        FoodCompany foodCompany = foodCompanyMapper.selectById(id);
        if (foodCompany == null || !foodCompany.getCompanyName().equals(user.getCompanyName())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        int i = foodCompanyMapper.deleteById(id);
        if (i != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public void expiredFood(Integer id) {
        Permission.check(UserType.EMPLOYEE.getCode());
        User user = permissionVerify(PermissionEnum.SUBMIT_EXPIRED_ITEM_PERMISSION.getCode());
        FoodCompany foodCompany = foodCompanyMapper.selectById(id);
        if (foodCompany == null || !foodCompany.getCompanyName().equals(user.getCompanyName())){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        foodCompany.setExpiredImpression(ExpiredStatusEnum.EXPIRED.getCode());
        int i = foodCompanyMapper.updateById(foodCompany);
        if (i != 1){
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
    }

    @Override
    public IPage<FoodCompany> getFoodList(SearchFood searchFood) {
        Permission.check(UserType.EMPLOYEE.getCode());
        String token = ThreadLocalUtil.get();
        if (token == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        String companyName = JWTHelper.getCompanyName(token);
        if (companyName == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        LambdaQueryWrapper<FoodCompany> foodCompanyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询自己企业下过期未提交的食材
        foodCompanyLambdaQueryWrapper.eq(FoodCompany::getCompanyName, companyName)
                .eq(FoodCompany::getExpiredImpression, ExpiredStatusEnum.NOT_EXPIRED.getCode());
        if (searchFood != null && StringUtils.hasLength(searchFood.getFoodName())){
            foodCompanyLambdaQueryWrapper.like(FoodCompany::getName, searchFood.getFoodName());
        }
        if (searchFood != null && searchFood.getFoodTypeId()!=null){
            foodCompanyLambdaQueryWrapper.eq(FoodCompany::getTypeId, searchFood.getFoodTypeId());
        }
        if (searchFood != null && StringUtils.hasLength(searchFood.getSourceName())){
            foodCompanyLambdaQueryWrapper.like(FoodCompany::getSource, searchFood.getSourceName());
        }
        Page<FoodCompany> page = new Page<>(searchFood.getPageNum(), searchFood.getPageSize());
        Page<FoodCompany> foodCompanyPage = foodCompanyMapper.selectPage(page, foodCompanyLambdaQueryWrapper);
        foodCompanyPage.setRecords(foodCompanyPage.getRecords().stream().map(foodCompany -> {
            if (foodCompany.getExpirationTime().isBefore(LocalDate.now())){
                foodCompany.setState(ExpiredStatusEnum.NOT_EXPIRED.getCode());
            }else {
                foodCompany.setState(ExpiredStatusEnum.EXPIRED.getCode());
            }
            return foodCompany;
        }).collect(Collectors.toList()));
        return foodCompanyPage;

    }

    @Override
    public IPage<FoodCompany> getExpiredFoodList(SearchFood searchFood) {
        Permission.check(UserType.EMPLOYEE.getCode());
        String token = ThreadLocalUtil.get();
        if (token == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        String companyName = JWTHelper.getCompanyName(token);
        if (companyName == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        LambdaQueryWrapper<FoodCompany> eq = new LambdaQueryWrapper<FoodCompany>()
                .eq(FoodCompany::getCompanyName, companyName)
                .eq(FoodCompany::getExpiredImpression, ExpiredStatusEnum.EXPIRED.getCode());
        Page<FoodCompany> page = new Page<>(searchFood.getPageNum(), searchFood.getPageSize());
        return foodCompanyMapper.selectPage(page, eq);
    }

    @Override
    public ArrayList<AnalysisData> getAnalysis(Integer type) {
        Permission.check(UserType.EMPLOYEE.getCode());
        String token = ThreadLocalUtil.get();
        String companyName = JWTHelper.getCompanyName(token);
        if (type == 1){//总重量分布
            List<Analysis> analysisList = foodCompanyMapper.analysisByWeight(companyName);
            //对analysisList按照foodType分组并计算每组的value和
            Map<String, Double> collect = analysisList.stream().collect(Collectors.groupingBy(Analysis::getFoodType, Collectors.summingDouble(Analysis::getValue)));
            ArrayList<AnalysisData> analysisDataArrayList = new ArrayList<>();
            collect.forEach((k,v)->{
                AnalysisData analysisData = new AnalysisData(v, k);
                analysisDataArrayList.add(analysisData);
            });
            return analysisDataArrayList;
        }else if (type == 2){//未过期分布
            List<Analysis> analysisList = foodCompanyMapper.analysisByNotExpired(companyName);
            Map<String, Long> collect = analysisList.stream().collect(Collectors.groupingBy(Analysis::getFoodType, Collectors.counting()));
            ArrayList<AnalysisData> analysisDataArrayList = new ArrayList<>();
            collect.forEach((k,v)->{
                AnalysisData analysisData = new AnalysisData(v.doubleValue(), k);
                analysisDataArrayList.add(analysisData);
            });
            return analysisDataArrayList;
        } else if (type == 3) {//过期分布
            List<Analysis> analysisList = foodCompanyMapper.analysisByExpired(companyName);
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

    private User permissionVerify(String code) {
        String token = ThreadLocalUtil.get();
        if (token == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        Integer userId = JWTHelper.getUserId(token);
        if (userId == null || userId <= 0){
            throw new BizException(BizCodeEnum.ACCOUNT_UN_LOGIN);
        }
        User user = userMapper.selectById(userId);
        if (user == null){
            throw new BizException(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
        if (code.equals(PermissionEnum.ADD_PERMISSION.getCode())){
            if (!user.getAddPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
            }
        } else if (code.equals(PermissionEnum.UPDATE_PERMISSION.getCode())) {
            if (!user.getUpdataPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Updated);
            }
        } else if (code.equals(PermissionEnum.DELETE_PERMISSION.getCode())) {
            if (!user.getDeletePermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Deleted);
            }
        } else if (code.equals(PermissionEnum.SUBMIT_EXPIRED_ITEM_PERMISSION.getCode())) {
            if (!user.getSubmitExpiredItemPermission().equals(1)){
                throw new BizException(BizCodeEnum.No_Permissions_Are_Added);
            }
        }else {
            throw new BizException(BizCodeEnum.OPERATE_FAIL);
        }
        return user;
    }
}
