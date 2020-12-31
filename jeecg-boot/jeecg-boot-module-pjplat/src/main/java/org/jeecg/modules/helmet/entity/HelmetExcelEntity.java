package org.jeecg.modules.helmet.entity;


import lombok.Data;
import lombok.ToString;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import java.io.Serializable;

@Data
@ToString
@ExcelTarget(value = "excelEntity")
public class HelmetExcelEntity implements Serializable {

    @Excel(name = "设备序列号")
    private String deviceSerial;
    @Excel(name = "验证码")
    private String validateCode;
}
