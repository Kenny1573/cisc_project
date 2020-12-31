package org.jeecg.modules.problemsheet.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 检查整改单
 * @Author: jeecg-boot
 * @Date:   2020-11-04
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_problem_sheet")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_problem_sheet对象", description="检查整改单")
public class BsProblemSheet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "pjplat.bs_project", dicText = "prj_name", dicCode = "id")
	@Dict(dictTable = "pjplat.bs_project", dicText = "prj_name", dicCode = "id")
    @ApiModelProperty(value = "项目ID")
    private java.lang.Integer psPrjId;
	/**施工单位（企业资源库）*/
	@Excel(name = "施工单位（企业资源库）", width = 15, dictTable = "pjplat.bs_company", dicText = "com_name", dicCode = "id")
	@Dict(dictTable = "pjplat.bs_company", dicText = "com_name", dicCode = "id")
    @ApiModelProperty(value = "施工单位（企业资源库）")
    private java.lang.Integer psConstructionUnit;
	/**监理单位（企业资源库）*/
	@Excel(name = "监理单位（企业资源库）", width = 15, dictTable = "pjplat.bs_company", dicText = "com_name", dicCode = "id")
	@Dict(dictTable = "pjplat.bs_company", dicText = "com_name", dicCode = "id")
    @ApiModelProperty(value = "监理单位（企业资源库）")
    private java.lang.Integer psSupervisingUnit;
	/**问题部位*/
	@Excel(name = "问题部位", width = 15)
    @ApiModelProperty(value = "问题部位")
    private java.lang.String psLocation;
	/**问题分类*/
	@Excel(name = "问题分类", width = 15, dictTable = "htplat.sys_category", dicText = "name", dicCode = "code")
	@Dict(dictTable = "htplat.sys_category", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "问题分类")
    private java.lang.String psType;
	/**隐患等级*/
	@Excel(name = "隐患等级", width = 15, dicCode = "hazard_level")
	@Dict(dicCode = "hazard_level")
    @ApiModelProperty(value = "隐患等级")
    private java.lang.String psLevel;
	/**隐患描述*/
	@Excel(name = "隐患描述", width = 15)
    @ApiModelProperty(value = "隐患描述")
    private java.lang.String psDescription;
	/**整改意见*/
	@Excel(name = "整改意见", width = 15, dicCode = "deal_advice")
	@Dict(dicCode = "deal_advice")
    @ApiModelProperty(value = "整改意见")
    private java.lang.String psAdvice;
	/**流程状态*/
	@Excel(name = "流程状态", width = 15)
    @ApiModelProperty(value = "流程状态")
    private java.lang.String actStatus;
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
	/**PSTIME*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "问题发现时间")
    private java.util.Date psTime;
	
	/**整改状态**/
	@Transient
	private transient java.lang.String zgzt;
	
	/**问题分类描述**/
	@Transient
	private transient java.lang.String wtms;
	
}
