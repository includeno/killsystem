package com.cloudcommons.entity;

/**
 * @author includeno
 * @since 2021/3/23 9:16
 */

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 待秒杀商品表
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "待秒杀商品信息")
public class ItemKill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联商品ID")
    private Integer itemId;

    @ApiModelProperty(value = "待秒杀商品库存")
    private Integer total;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    //@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss ", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    private Date endTime;

    @ApiModelProperty(value = "是否激活")
    @TableLogic
    private Integer isActive;
    public static final Integer active=1;
    public static final Integer inactive=0;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
