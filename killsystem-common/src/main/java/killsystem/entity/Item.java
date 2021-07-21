package killsystem.entity;

/**
 * @author includeno
 * @since 2021/3/23 9:14
 */

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
 * 商品表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "商品信息")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品代码")
    private String code;

    @ApiModelProperty(value = "商品库存")
    private Long stock;

    //采购时间
    //RESOLVED date转string问题
    @DateTimeFormat(pattern="yyyy-MM-dd")//页面写入数据库时格式化
    @ApiModelProperty(value = "采购时间")
    private Date purchaseTime;

    @Version
    @ApiModelProperty(value = "乐观锁版本号")
    private Integer version;

    @TableLogic
    @ApiModelProperty(value = "状态 1激活 0不激活")
    private Integer isActive;
    public static final Integer active=1;
    public static final Integer inactive=0;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")//写出数据库时格式化
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写出数据库时格式化
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
