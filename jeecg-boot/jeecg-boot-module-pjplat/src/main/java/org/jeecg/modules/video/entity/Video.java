package org.jeecg.modules.video.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
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
 * @Description: 监控管理
 * @Author: jeecg-boot
 * @Date:   2020-12-03
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_video")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_video对象", description="监控管理")
@KeySequence(value = "pjplat.SEQ_BS_VIDEO")

public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "pjplat.BS_PROJECT", dicText = "PRJ_NAME", dicCode = "ID")
	@Dict(dictTable = "pjplat.BS_PROJECT", dicText = "PRJ_NAME", dicCode = "ID")
    @ApiModelProperty(value = "项目ID")
    private java.lang.String bsPrjId;
	/**通道ID*/
	@Excel(name = "通道ID", width = 15)
    @ApiModelProperty(value = "通道ID")
    private java.lang.String bsVideoChannelid;
	/**通道名称*/
	@Excel(name = "通道名称", width = 15)
    @ApiModelProperty(value = "通道名称")
    private java.lang.String bsVideoChannelname;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String bsVideoLon;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String bsVideoLat;
	/**状态*/
	@Excel(name = "状态", width = 15, dicCode = "online_status")
	@Dict(dicCode = "online_status")
    @ApiModelProperty(value = "状态")
    private java.lang.String bsVideoStatus;
	/**IP地址*/
	@Excel(name = "IP地址", width = 15)
    @ApiModelProperty(value = "IP地址")
    private java.lang.String bsIp;
	/**网关*/
	@Excel(name = "网关", width = 15)
    @ApiModelProperty(value = "网关")
    private java.lang.String bsGateway;
	/**createBy*/
    @ApiModelProperty(value = "createBy")
    private java.lang.String createBy;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**updateBy*/
    @ApiModelProperty(value = "updateBy")
    private java.lang.String updateBy;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;
}
