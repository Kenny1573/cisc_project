package org.jeecg.modules.project.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_project")
@ApiModel(value="pjplat.bs_project对象", description="项目信息")
@KeySequence(value = "pjplat.SEQ_BS_PROJECT",clazz = Long.class)
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
	/**id*/
	@TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "id")
    private Long id;
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
    @ApiModelProperty(value = "匡算")
    private BigDecimal prjRoughEstimate;
	/**估算*/
    @Excel(name = "估算(万元)", width = 15)
    @ApiModelProperty(value = "估算")
    private BigDecimal prjEstimate;
	/**概算*/
    @Excel(name = "概算(万元)", width = 15)
    @ApiModelProperty(value = "概算")
    private BigDecimal prjBudgetEstimate;
	/**决算*/
    @Excel(name = "决算(万元)", width = 15)
    @ApiModelProperty(value = "决算")
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
    @Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private String prjLatitude;
	/**纬度*/
    @Excel(name = "工程参数", width = 15)
    @ApiModelProperty(value = "工程参数")
    private String prjParams;
	/**工程参数*/
    @Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private String prjLongitude;

    /**updateTime*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;
    /**createTime*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
    /**updateBy*/
    @ApiModelProperty(value = "updateBy")
    private java.lang.String updateBy;
    /**createBy*/
    @ApiModelProperty(value = "createBy")
    private java.lang.String createBy;
    /**delFlag*/
    @ApiModelProperty(value = "delFlag")
    private java.lang.Boolean delFlag =false;
}
