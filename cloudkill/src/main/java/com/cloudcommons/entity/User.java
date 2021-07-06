package com.cloudcommons.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id" ,type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private String phone;

    private String email;

    private Integer isHidden;

    @TableLogic
    private Integer isActive;

    //只添加不更新
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    //添加和更新
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
