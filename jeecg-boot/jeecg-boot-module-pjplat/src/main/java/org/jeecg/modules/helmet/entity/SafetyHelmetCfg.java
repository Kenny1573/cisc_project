package org.jeecg.modules.helmet.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SafetyHelmetCfg {
    @ApiModelProperty(value = "")
    private Integer takeoffReportInterval;

    @ApiModelProperty(value = "脱戴帽报告频率")
    private Integer wearReportInterval;

    @ApiModelProperty(value = "GPS定位频率")
    private Integer GPSReportInterval;

    @ApiModelProperty(value = "撞击灵敏度：default:0   0:不灵敏，2：正常 4：灵敏")
    private Integer hitSensitivity;

    @ApiModelProperty(value = "脱戴帽消抖时间")
    private Integer eliminatingJitterTime;

    @ApiModelProperty(value = "静止告警时间 默认300s")
    private Integer abnormalRestTime;
}
