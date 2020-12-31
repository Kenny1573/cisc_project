package org.jeecg.modules.kenny.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: BS_HELMET_HEARTBEAT
 * @Author: jeecg-boot
 * @Date:   2020-12-29
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_helmet_heartbeat")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_helmet_heartbeat对象", description="BS_HELMET_HEARTBEAT")
public class BsHelmetHeartbeat implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**设备序列号*/
	@Excel(name = "设备序列号", width = 15)
    @ApiModelProperty(value = "设备序列号")
    private java.lang.String deviceSerial;
	/**通道号*/
	@Excel(name = "通道号", width = 15)
    @ApiModelProperty(value = "通道号")
    private java.lang.Integer channelNo;
	/**消息id*/
	@Excel(name = "消息id", width = 15)
    @ApiModelProperty(value = "消息id")
    private java.lang.String messageId;
	/**消息时间*/
	@Excel(name = "消息时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "消息时间")
    private java.util.Date messageTime;
	/**脱戴帽状态*/
	@Excel(name = "脱戴帽状态", width = 15)
    @ApiModelProperty(value = "脱戴帽状态")
    private java.lang.Integer helmetStatus;
	/**蓝牙beaconID*/
	@Excel(name = "蓝牙beaconID", width = 15)
    @ApiModelProperty(value = "蓝牙beaconID")
    private java.lang.String beaconId;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
	/**电池电量*/
	@Excel(name = "电池电量", width = 15)
    @ApiModelProperty(value = "电池电量")
    private java.lang.Integer powerPercent;
	/**上下班*/
	@Excel(name = "上下班", width = 15)
    @ApiModelProperty(value = "上下班")
    private java.lang.String workStatus;
	/**GPS信号强度
*/
	@Excel(name = "GPS信号强度 ", width = 15)
    @ApiModelProperty(value = "GPS信号强度 ")
    private java.lang.String rssiSignalStrength;
	/**静止警告*/
	@Excel(name = "静止警告", width = 15)
    @ApiModelProperty(value = "静止警告")
    private java.lang.Integer isAbnormalStationary;
	/**SOS状态*/
	@Excel(name = "SOS状态", width = 15)
    @ApiModelProperty(value = "SOS状态")
    private java.lang.Integer isSos;
}
