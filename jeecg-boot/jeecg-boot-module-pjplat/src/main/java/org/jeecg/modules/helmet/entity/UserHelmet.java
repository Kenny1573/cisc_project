package org.jeecg.modules.helmet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pjplat.bs_user_helmet")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_user_helmet对象", description="人员绑定头盔")
@KeySequence(value = "pjplat.SEQ_BS_USER_HELMET")
public class UserHelmet implements Serializable {
    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;

    @ApiModelProperty(value = "人员账号")
    private java.lang.String username;

    @ApiModelProperty(value = "头盔序列号")
    private java.lang.String deviceSerial;

}
