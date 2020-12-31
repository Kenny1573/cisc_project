package org.jeecg.modules.monthreport.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Data
public class MonthReportExport  {


    private Long id;
    @Excel(name = "年份", width = 15)
	private String year;
	/**月度计划ID*/
	@Excel(name = "月份", width = 15, dictTable = "pjplat.BS_YEARPLAN_DETAIL", dicText = "YPD_MONTH", dicCode = "ID")
    @ApiModelProperty(value = "月份")
    private Long mrYpdId;
	/**总体进展情况描述*/
	@Excel(name = "总体进展情况描述", width = 15)
    @ApiModelProperty(value = "总体进展情况描述")
    private String mrDescription;
	/**当月完成投资数*/
	@Excel(name = "当月完成投资数(万元)", width = 15)
    @ApiModelProperty(value = "当月完成投资数(万元)")
    private String mrFinishInvestment;
    /**当月完成财务数*/
    @ApiModelProperty(value = "当月完成财务数(万元)")
    private String mrFinishFinance;
	/**下月计划安排*/
	@Excel(name = "下月计划安排", width = 15)
    @ApiModelProperty(value = "下月计划安排")
    private String mrNextPlan;
	/**需协调处理的问题*/
	@Excel(name = "需协调处理的问题", width = 15)
    @ApiModelProperty(value = "需协调处理的问题")
    private String mrNeedCoordinate;

    /**是否显示在可视化*/
    @Dict(dicCode = "sure_or_not")
    @Excel(name = "是否显示在可视化", width = 15,dicCode = "sure_or_not")
    @ApiModelProperty(value = "是否显示在可视化")
    private String mrNeedCoordinateShow;


}
