package com.cloudcommons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用户信息表
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
public class User implements Serializable {

    private static final long serialVersionUID = 2L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private Integer isHidden;

    @TableLogic
    private Integer isActive;

    //只添加不更新
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //添加和更新
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
