package com.cddr.szd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cddr.szd.model.FoodType;
import com.cddr.szd.model.dto.Analysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FoodTypeMapper extends BaseMapper<FoodType> {
}
