package org.jeecg.modules.helmet.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

@Data
@ApiModel(value="安全帽数据库对象", description="安全数据库帽对象")
@TableName("pjplat.bs_helmet_info")
@KeySequence(value = "pjplat.SEQ_BS_HELMET_INFO")

public class HelmetInfo {


    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
    @ApiModelProperty(value = "设备序列号")
    private String deviceSerial;
    @Dict(dicCode = "online_status")
    @ApiModelProperty(value = "在线状态; 0-不在线，1-在线")
    private int status;
    @ApiModelProperty(value = "佩戴状态")
    @Dict(dicCode = "helmet_status")
    private int wearStatus=0;
    @ApiModelProperty(value = "电池电量")
    private Integer batteryStatus;

    private Integer hitSensitivity;
    @ApiModelProperty(value = "撞击灵敏度：default:0   0:不灵敏，2：正常 4：灵敏")
    private Integer hitSensitivityMax;
    @ApiModelProperty(value = "撞击灵敏度：default:0   0:不灵敏，2：正常 4：灵敏")
    private Integer hitSensitivityMin;

    @ApiModelProperty(value = "")
    private Integer takeoffReportInterval;
    @ApiModelProperty(value = "")
    private Integer takeoffReportIntervalMax;
    @ApiModelProperty(value = "")
    private Integer takeoffReportIntervalMin;

    @ApiModelProperty(value = "脱戴帽报告频率")
    private Integer wearReportInterval;
    @ApiModelProperty(value = "脱戴帽报告频率")
    private Integer wearReportIntervalMax;
    @ApiModelProperty(value = "脱戴帽报告频率")
    private Integer wearReportIntervalMin;


    @ApiModelProperty(value = "GPS定位频率")
    private Integer GpsReportInterval;
    @ApiModelProperty(value = "GPS定位频率")
    private Integer GpsReportIntervalMax;
    @ApiModelProperty(value = "GPS定位频率")
    private Integer GpsReportIntervalMin;


    @ApiModelProperty(value = "脱戴帽消抖时间")
    private Integer eliminatingJitterTime;
    @ApiModelProperty(value = "脱戴帽消抖时间")
    private Integer eliminatingJitterTimeMax;
    @ApiModelProperty(value = "脱戴帽消抖时间")
    private Integer eliminatingJitterTimeMin;

    private Integer abnormalRestTime;
    @ApiModelProperty(value = "静止告警时间 默认300s")
    private Integer abnormalRestTimeMax;
    @ApiModelProperty(value = "静止告警时间 默认300s")
    private Integer abnormalRestTimeMin;


    @ApiModelProperty(value = "经度")
    private String lon;


    @ApiModelProperty(value = "纬度")
    private String lat;


}
