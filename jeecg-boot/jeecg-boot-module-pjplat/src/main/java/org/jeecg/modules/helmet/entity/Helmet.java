package org.jeecg.modules.helmet.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@Data
@ApiModel(value="安全帽对象", description="安全帽对象")

public class Helmet {



    @ApiModelProperty(value = "设备序列号")
    private String deviceSerial;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备型号")
    private String model;

    @Dict(dicCode = "online_status")
    @ApiModelProperty(value = "在线状态; 0-不在线，1-在线")
    private int status;
    @Dict(dicCode = "helmet_defence")
    @ApiModelProperty(value = "具有防护能力的设备 布撤防状态：0-睡眠,8-在家,16-外出,普通IPC布撤防状态：0-撤防,1-布防")
    private int defence;
    @Dict(dicCode = "sure_or_not_0_1")
    @ApiModelProperty(value = "是否加密：0-不加密,1-加密")
    private int isEncrypt;
    @Dict(dicCode = "alarmSoundMode")
    @ApiModelProperty(value = "告警声音模式：0-短叫,1-长叫,2-静音")
    private int alarmSoundMode;
    @Dict(dicCode = "sure_or_not_0_1")
    @ApiModelProperty(value = "设备下线是否通知：0-不通知,1-通知")
    private int offlineNotify;
    @ApiModelProperty(value = "设备大类")
    private String category;
    @ApiModelProperty(value = "网络类型")
    private String netType;
    @ApiModelProperty(value = "信号强度(%)")
    private String signal;
}
