package com.killsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value = "用户信息")
public class User implements Serializable {

    private static final long serialVersionUID = 2L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 是否隐藏(1=是；0=否)
     */
    @ApiModelProperty("是否隐藏(1=是；0=否)")
    private Integer isHidden;

    /**
     * 是否有效(1=是；0=否)
     */
    @ApiModelProperty("是否有效(1=是；0=否)")
    @TableLogic
    private Integer isActive;
    public static final Integer active=1;
    public static final Integer inactive=0;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private Integer money;
    public static final Integer defaultmoney=100;
    public static final Integer muchmoney=200;
    public static final Integer lessmoney=50;

    //只添加不更新
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //添加和更新
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
