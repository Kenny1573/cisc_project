package org.jeecg.modules.activiti.entity;

import java.io.File;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("pjplat.BS_PROBLEM_SHEET")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BS_PROBLEM_SHEET对象", description="问题整改单表")
@KeySequence(value="pjplat.SEQ_BS_PROBLEM_SHEET", clazz=Long.class)
public class ProblemSheet {
	private static final long serialVersionUID = 1L;

	/**自增序号*/
	@TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "自增序号")
    private java.lang.Long id;
	
	@Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.Long psPrjId;
	
	@Excel(name = "施工单位", width = 15)
    @ApiModelProperty(value = "施工单位")
    private java.lang.Long psConstructionUnit;
	
	@Excel(name = "监理单位", width = 15)
    @ApiModelProperty(value = "监理单位")
    private java.lang.Long psSupervisingUnit;
	
	@Excel(name = "问题部位", width = 15)
    @ApiModelProperty(value = "问题部位")
    private java.lang.String psLocation;
	
	@Excel(name = "问题分类", width = 15)
    @ApiModelProperty(value = "问题分类")
    private java.lang.String psType;
	
	@Excel(name = "隐患等级", width = 15)
    @ApiModelProperty(value = "隐患等级")
    private java.lang.String psLevel;
	
	@Excel(name = "隐患描述", width = 15)
    @ApiModelProperty(value = "隐患描述")
    private java.lang.String psDescription;//PS_DESCRIPTION
	
	@Excel(name = "整改意见", width = 15)
    @ApiModelProperty(value = "整改意见")
    private java.lang.String psAdvice; //PS_ADVICE
	
	@Excel(name = "整改截止时间", width = 20)
    @ApiModelProperty(value = "整改截止时间")
    private String psEndTime; 
	
	
	@Excel(name = "createBy", width = 15)
    @ApiModelProperty(value = "createBy")
	private String createBy;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	private Date createTime;
	/**delFlag*/
	@Excel(name = "delFlag", width = 15)
    @ApiModelProperty(value = "delFlag")
	private Integer delFlag;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
    @ApiModelProperty(value = "updateBy")
	private String updateBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "updateTime")
	private Date updateTime;
}
