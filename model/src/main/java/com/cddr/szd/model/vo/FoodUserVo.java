package com.cddr.szd.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * <p>
 * 
 * </p>
 *
 * @author zy
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FoodUserVo implements Serializable {



    @NotNull(groups = Update.class)
    private Integer id;
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String nameFood;

    /**
     * 食物采购时间
     */

    private LocalDate foodCreatetime;

    /**
     * 食物过期时间
     */
    private LocalDate foodExpirationTime;

    /**
     * 食物重量单位kg
     */
    private Integer foodNumberWarehouses;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    @TableLogic
    private Integer isDeleted;

//    /**
//     * 采购源是哪家公司
//     */
//    private String purchaseSource;

    /**
     * 食物种类
     */
    @Size(max = 50, message = "名字的长度不能超过50个字符")
    private String foodTypeId;

//    /**
//     * 1->没有过期 2->过期
//     */
//    private Integer foodState;

    /**
     * 如果查询的公司name为空->user_id=id,如果查询的公司name不为空->user_id=user.company_name,
     */
    private Integer userId;
    public interface Add extends Default {

    }

    public interface Update extends Default{

    }

}
