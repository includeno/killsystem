package com.cloudcommons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 秒杀成功订单表
 * </p>
 *
 * @author includeno
 * @since 2021-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ItemKillSuccess implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.INPUT) //TODO 雪花算法
    private String id;//秒杀成功生成的订单编号

    private Integer itemId;//商品id

    private Integer killId;//秒杀id

    private Integer userId;//用户id

    private Integer status;//秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}


