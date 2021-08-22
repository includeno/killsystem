package com.killsystem.entity;

/**
 * @author includeno
 * @since 2021/3/23 9:16
 */

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * <p>
 * 待秒杀商品表
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Data
//@EqualsAndHashCode(callSuper = true)
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

    @ApiModelProperty(value = "待秒杀商品实际库存")
    private Integer killtotal;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;

    @ApiModelProperty(value = "开始时间")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    //@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss ", timezone = "GMT+8")//写出数据库时格式化
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    private Date endTime;

    @ApiModelProperty(value = "是否激活")
    @TableLogic
    private Integer isActive;
    public static final Integer active=1;
    public static final Integer inactive=0;


    @ApiModelProperty(value = "创建时间")
    //@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemKill itemKill = (ItemKill) o;
        return Objects.equals(id, itemKill.id) && itemId.equals(itemKill.itemId) && total.equals(itemKill.total) && version.equals(itemKill.version) && startTime.equals(itemKill.startTime) && endTime.equals(itemKill.endTime) && isActive.equals(itemKill.isActive) && createTime.equals(itemKill.createTime) && updateTime.equals(itemKill.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, total, version, startTime, endTime, isActive, createTime, updateTime);
    }
}
