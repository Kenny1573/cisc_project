package org.jeecg.modules.monthreport.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pjplat.bs_month_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_month_report对象", description="进度月报")
@KeySequence(value = "pjplat.SEQ_BS_MONTH_REPORT")
public class MonthReport implements Serializable {

    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;


	/**月度计划ID*/
	@Excel(name = "月份", width = 15, dictTable = "pjplat.BS_YEARPLAN_DETAIL", dicText = "YPD_MONTH", dicCode = "ID")
    @ApiModelProperty(value = "月份")
    private java.lang.Long mrYpdId;
	/**总体进展情况描述*/
	@Excel(name = "总体进展情况描述", width = 15)
    @ApiModelProperty(value = "总体进展情况描述")
    private java.lang.String mrDescription;
	/**当月完成投资数*/
	@Excel(name = "当月完成投资数(万元)", width = 15)
    @ApiModelProperty(value = "当月完成投资数(万元)")
    private java.lang.String mrFinishInvestment;
    /**当月完成财务数*/
    @ApiModelProperty(value = "当月完成财务数(万元)")
    private java.lang.String mrFinishFinance;
	/**下月计划安排*/
	@Excel(name = "下月计划安排", width = 15)
    @ApiModelProperty(value = "下月计划安排")
    private java.lang.String mrNextPlan;
	/**需协调处理的问题*/
	@Excel(name = "需协调处理的问题", width = 15)
    @ApiModelProperty(value = "需协调处理的问题")
    private java.lang.String mrNeedCoordinate;

    /**是否显示在可视化*/
    @Dict(dicCode = "sure_or_not")
    @Excel(name = "是否显示在可视化", width = 15,dicCode = "sure_or_not")
    @ApiModelProperty(value = "是否显示在可视化")
    private java.lang.String mrNeedCoordinateShow;

    /**createTime*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
    /**createBy*/
    @ApiModelProperty(value = "createBy")
    private java.lang.String createBy;
    /**updateTim*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTim")
    private java.util.Date updateTime;
    /**updateBy*/

    @ApiModelProperty(value = "updateBy")
    private java.lang.String updateBy;

    public MonthReport(String mrFinishInvestment, String mrFinishFinance) {
        this.mrFinishInvestment = mrFinishInvestment;
        this.mrFinishFinance = mrFinishFinance;
    }
}
