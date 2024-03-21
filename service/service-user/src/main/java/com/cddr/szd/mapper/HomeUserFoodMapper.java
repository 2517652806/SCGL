package com.cddr.szd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cddr.szd.model.FoodUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2024-03-20
 */
@Mapper
public interface HomeUserFoodMapper extends BaseMapper<FoodUser> {
//    int updateExpiredItemsStatus();
}
