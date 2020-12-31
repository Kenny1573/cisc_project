package org.jeecg.modules.yearplandetail.entity;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 月度计划
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_yearplan_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_yearplan_detail对象", description="月度计划")
@KeySequence(value = "pjplat.SEQ_BS_YEARPLAN_DETAIL")
public class YearPlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
	/**年度计划ID*/
	@Excel(name = "年度计划ID", width = 15)
    @ApiModelProperty(value = "年度计划ID")
    private java.lang.Long ypdYpId;
	/**月份*/
	@Excel(name = "月份", width = 15, dicCode = "month")
	@Dict(dicCode = "month")
    @ApiModelProperty(value = "月份")
    private java.lang.String ypdMonth;
	/**月计划投资数*/
	@Excel(name = "月计划投资数(万元)", width = 15)
    @ApiModelProperty(value = "月计划投资数")
    private java.lang.String ypdInvestment;
	/**月计划财务数*/
    //@Excel(name = "月计划财务数(万元)", width = 15)
    @ApiModelProperty(value = "月计划财务数")
    private java.lang.String ypdFinance;
    /**月计划形象进度*/
    @Excel(name = "月计划形象进度", width = 15)
    @ApiModelProperty(value = "月计划形象进度")
    private java.lang.String ypdPlan;

    public YearPlanDetail() {
    }

    public YearPlanDetail(Long id, Long ypdYpId, String ypdMonth, String ypdInvestment, String ypdFinance,String ypdPlan) {
        this.id = id;
        this.ypdYpId = ypdYpId;
        this.ypdMonth = ypdMonth;
        this.ypdInvestment = ypdInvestment;
        this.ypdFinance = ypdFinance;
        this.ypdPlan  =ypdPlan;

    }

    public YearPlanDetail(Long id, String ypdMonth, String ypdInvestment, String ypdFinance,String ypdPlan) {
        this.id = id;
        this.ypdMonth = ypdMonth;
        this.ypdInvestment = ypdInvestment;
        this.ypdFinance = ypdFinance;
        this.ypdPlan  =ypdPlan;
    }
}
