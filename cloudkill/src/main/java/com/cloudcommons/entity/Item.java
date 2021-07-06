package com.cloudcommons.entity;

/**
 * @author includeno
 * @since 2021/3/23 9:14
 */

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * <p>
 * 商品表
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private Long stock;
    //采购时间
    //RESOLVED date转string问题
    @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
    private Date purchaseTime;
    //乐观锁
    @Version
    private Integer version;

    @TableLogic
    private Integer isActive;

    @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
