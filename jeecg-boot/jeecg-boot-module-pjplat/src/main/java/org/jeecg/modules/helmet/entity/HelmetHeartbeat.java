package org.jeecg.modules.helmet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("pjplat.bs_helmet_heartbeat")
@KeySequence(value = "pjplat.SEQ_BS_HELMET_HEART_BEAT")
@AllArgsConstructor
public class HelmetHeartbeat {
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
    @ApiModelProperty(value = "设备序列号")
    private String deviceSerial;
    @ApiModelProperty(value = "通道号")
    private int channelNo;
    @ApiModelProperty(value = "消息id")
    private String  messageId;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "消息时间")
    private java.util.Date messageTime;
    @ApiModelProperty(value = "脱戴帽状态")
    private boolean helmetStatus;

    @ApiModelProperty(value = "蓝牙beaconID")
    private String beaconId;
        @ApiModelProperty(value = "经度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
    @ApiModelProperty(value = "电量")
    private int powerPercent;
    @ApiModelProperty(value = "上下班")
    private String workStatus;
    @ApiModelProperty(value = "GPRS信号强度")
    private String RssiSignalStrength;
    @ApiModelProperty(value = "静止警告状态")
    private boolean isAbnormalStationary;
    @ApiModelProperty(value = "SOS状态")
    private boolean isSos;

}
