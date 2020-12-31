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
@TableName("pjplat.bs_helmet_eventinfo")
@KeySequence(value = "pjplat.SEQ_BS_HELMET_ENENT_INFO")
@Data
@AllArgsConstructor
public class HelmetEventInfo {
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
    @ApiModelProperty(value = "事件类型")
    private String eventType;
    @ApiModelProperty(value = "事件结果")
    private String eventResult;
}
