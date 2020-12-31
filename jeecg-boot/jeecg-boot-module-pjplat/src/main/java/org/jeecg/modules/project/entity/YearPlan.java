package org.jeecg.modules.project.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 年度考核计划
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_year_plan")
@KeySequence(value = "pjplat.SEQ_BS_YEAR_PLAN",clazz = Long.class)
@ApiModel(value="pjplat.bs_project对象", description="项目信息")
public class YearPlan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private Long id;
	/**项目ID*/
	@ApiModelProperty(value = "项目ID")
	private Long ypPrjId;
	/**年度*/
	@Excel(name = "年度", width = 15)
	@ApiModelProperty(value = "年度")
	private String ypYear;
	/**目标投资数*/
	@Excel(name = "目标投资数(万元)", width = 15)
	@ApiModelProperty(value = "目标投资数")
	private String ypInvestment;
	/**目标财务数*/
	//@Excel(name = "目标财务数(万元)", width = 15)
	@ApiModelProperty(value = "目标财务数")
	private String ypFinance;
	/**形象进度*/
	@Excel(name = "形象进度", width = 15)
	@ApiModelProperty(value = "形象进度")
	private String ypImageProgress;

	public YearPlan(Long ypPrjId, String ypYear, String ypInvestment, String ypFinance, String ypImageProgress) {
		this.ypPrjId = ypPrjId;
		this.ypYear = ypYear;
		this.ypInvestment = ypInvestment;
		this.ypFinance = ypFinance;
		this.ypImageProgress = ypImageProgress;
	}
}
