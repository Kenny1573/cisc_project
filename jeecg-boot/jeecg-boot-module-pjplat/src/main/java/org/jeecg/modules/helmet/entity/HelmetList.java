package org.jeecg.modules.helmet.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value="调用萤石获取安全帽列表返回的对象", description="调用萤石获取安全帽列表返回的对象")
public class HelmetList {

    @ApiModelProperty(value = "设备序列号")
    private String deviceSerial;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    @ApiModelProperty(value = "在线状态; 0-不在线，1-在线")
    private int status;
    @ApiModelProperty(value = "具有防护能力的设备 布撤防状态：0-睡眠,8-在家,16-外出,普通IPC布撤防状态：0-撤防,1-布防")
    private int defence;
    @ApiModelProperty(value = "设备版本")
    private String deviceVersion;
    @ApiModelProperty(value = "parentCategory")
    private String parentCategory;

    private int delFlag;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "addTime")
    private java.util.Date addTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;

}
