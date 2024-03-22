package com.cddr.szd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cddr.szd.model.FoodCompany;
import com.cddr.szd.model.dto.Analysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FoodCompanyMapper extends BaseMapper<FoodCompany> {
    List<Analysis> analysisByWeight(@Param("companyName") String companyName);

    List<Analysis> analysisByNotExpired(String companyName);

    List<Analysis> analysisByExpired(String companyName);
}
