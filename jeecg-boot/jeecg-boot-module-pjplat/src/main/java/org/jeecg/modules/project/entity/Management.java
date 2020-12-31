package org.jeecg.modules.project.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class Management {
    @Excel(name = "前期业主代表", width = 15)
    @ApiModelProperty(value = "前期业主代表")
    private Integer preOwner;
    /**决算*/
    @Excel(name = "实施业主代表", width = 15)
    @ApiModelProperty(value = "实施业主代表")
    private Integer midOwner;
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
}
