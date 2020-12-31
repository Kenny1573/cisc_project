package org.jeecg.modules.project.vo;

import java.math.BigDecimal;
import java.util.List;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.project.entity.YearPlan;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="pjplat.bs_projectPage对象", description="项目信息")
public class ProjectPage {

	/**id*/
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "企业ID")
	private Long prjComId;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
	@ApiModelProperty(value = "项目名称")
	private String prjName;
	/**项目编号*/
	@Excel(name = "项目编号", width = 15)
	@ApiModelProperty(value = "项目编号")
	private String prjNumber;
	/**项目分类*/
	@Dict(dicCode = "project_class")
	@Excel(name = "项目分类", width = 15, dicCode = "project_class")
	@ApiModelProperty(value = "项目分类")
	private String prjType;
	/**项目阶段*/
	@Dict(dicCode = "project_process")
	@Excel(name = "项目阶段", width = 15, dicCode = "project_class")
	@ApiModelProperty(value = "项目阶段")
	private String prjStage;
	/**匡算*/
	@Excel(name = "匡算(万元)", width = 15)
	@ApiModelProperty(value = "匡算(万元)")
	private BigDecimal prjRoughEstimate;
	/**估算*/
	@Excel(name = "估算(万元)", width = 15)
	@ApiModelProperty(value = "估算(万元)")
	private BigDecimal prjEstimate;
	/**概算*/
	@Excel(name = "概算(万元)", width = 15)
	@ApiModelProperty(value = "概算(万元)")
	private BigDecimal prjBudgetEstimate;
	/**决算*/
	@Excel(name = "决算(万元)", width = 15)
	@ApiModelProperty(value = "决算(万元)")
	private BigDecimal prjFinalEstimate;
	/**建设规模*/
	@Excel(name = "建设规模", width = 15)
	@ApiModelProperty(value = "建设规模")
	private String prjBuildingSize;
	/**建设内容*/
	@Excel(name = "建设内容", width = 15)
	@ApiModelProperty(value = "建设内容")
	private String prjBuildingContent;
	/**工程地址*/
	@Excel(name = "工程地址", width = 15)
	@ApiModelProperty(value = "工程地址")
	private String prjBuildingAddress;
	/**经度*/
	@Excel(name = "经度", width = 15)
	@ApiModelProperty(value = "经度")
	private String prjLatitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
	@ApiModelProperty(value = "纬度")
	private String prjParams;
	/**工程参数*/
	@Excel(name = "工程参数", width = 15)
	@ApiModelProperty(value = "工程参数")
	private String prjLongitude;
	@ApiModelProperty(value = "项目管理人员")
	private Integer prjManager;
	/**createtime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "createtime")
	private Date createTime;
	
	@ExcelCollection(name="年度考核计划")
	@ApiModelProperty(value = "年度考核计划")
	private List<YearPlan> yearPlanList;
	@Excel(name = "前期业主代表", width = 15)
	@ApiModelProperty(value = "前期业主代表")
	private String preOwner;
	/**决算*/
	@Excel(name = "实施业主代表", width = 15)
	@ApiModelProperty(value = "实施业主代表")
	private String midOwner;
	/**建设规模*/
	@Excel(name = "总工程师办公室", width = 15)
	@ApiModelProperty(value = "总工程师办公室")
	private String office;
	/**建设内容*/
	@Excel(name = "计划部", width = 15)
	@ApiModelProperty(value = "计划部")
	private String plan;
	/**工程地址*/
	@Excel(name = "道桥工程部", width = 15)
	@ApiModelProperty(value = "道桥工程部")
	private String bridge;
	/**经度*/
	@Excel(name = "建筑工程部", width = 15)
	@ApiModelProperty(value = "建筑工程部")
	private String build;
	/**纬度*/
	@Excel(name = "质量安全监督部", width = 15)
	@ApiModelProperty(value = "质量安全监督部")
	private String quality;
	/**工程参数*/
	@Excel(name = "财务审计部", width = 15)
	@ApiModelProperty(value = "财务审计部")
	private String finance;
	/**项目管理人员*/
	@Excel(name = "跟踪审计(外)", width = 15)
	private Integer other;

	@ExcelCollection(name="施工单位信息")
	@ApiModelProperty(value = "施工单位信息")
	private List<Company> companyList1;

	@ExcelCollection(name="监管单位信息")
	@ApiModelProperty(value = "监管单位信息")
	private List<Company> companyList2;
}
