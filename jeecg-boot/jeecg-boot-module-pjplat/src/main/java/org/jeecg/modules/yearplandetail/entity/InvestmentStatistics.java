package org.jeecg.modules.yearplandetail.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="投资统计对象", description="投资统计")
public class InvestmentStatistics {
    @ApiModelProperty(value = "月份")
    Integer month;
    @ApiModelProperty(value = "计划投资数(万元)")
    String planInvestment;
    @ApiModelProperty(value = "计划财务数(万元)")
    String planFinance;
    @ApiModelProperty(value = "实际投资数(万元)")
    String realInvestment;
    @ApiModelProperty(value = "实际财务数(万元)")
    String realFinance;
    @ApiModelProperty(value = "截至本月累计投资数(万元)")
    String totalInvestment;
    @ApiModelProperty(value = "截至本月累计财务数(万元)")
    String totalFinance;
    @ApiModelProperty(value = "月度投资占比")
    String perMonth;
    @ApiModelProperty(value = "截至当前月累计投资完成(万元)")
    String perTotal;
    @ApiModelProperty(value = "计划形象进度")
    String planProcess;
    @ApiModelProperty(value = "实际形象进度")
    String realProcess;
    

}
