package org.jeecg.modules.helmet.entity;

import lombok.Data;
import lombok.ToString;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;


@Data
@ToString
@ExcelTarget(value = "excelEntity")
public class HelmetExcelExport implements Serializable {

    @Excel(name = "事件类型")
    private String type;
    @Excel(name = "报警次数")
    private Long num;
}
