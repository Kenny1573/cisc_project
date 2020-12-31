package org.jeecg.modules.contract.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 合同信息
 * @Author: jeecg-boot
 * @Date:   2020-10-22
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_contract")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_contract对象", description="合同信息")
@KeySequence(value = "pjplat.SEQ_BS_CONTRACT")
public class Contract implements Serializable {
    private static final long serialVersionUID = 1L;

	/**序号*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "序号")
    private java.lang.Long id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.Long conPrjId;
	/**合同名称*/
	@Excel(name = "合同名称", width = 15)
    @ApiModelProperty(value = "合同名称")
    private java.lang.String conName;

	/**合同编码*/
	@Excel(name = "合同编码", width = 15)
    @ApiModelProperty(value = "合同编码")
    private java.lang.String conNumber;
	/**甲方*/
	@Excel(name = "甲方", width = 15)
    @ApiModelProperty(value = "甲方")
    private java.lang.String conPartyA;
	/**乙方*/
    @Excel(name = "乙方", width = 15)
    @ApiModelProperty(value = "乙方")
    private java.lang.String conPartyB;
	/**丙方*/
    @Excel(name = "丙方", width = 15)
    @ApiModelProperty(value = "丙方")
    private java.lang.String conPartyC;
	/**合同金额*/
	@Excel(name = "合同金额(万元)", width = 15)
    @ApiModelProperty(value = "合同金额")
    private java.lang.String conMoney;
	/**合同签订日期*/
	@Excel(name = "合同签订日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同签订日期")
    private java.util.Date conSignDate;
	/**履行开始日期*/
	@Excel(name = "履行开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "履行开始日期")
    private java.util.Date conBeginDate;
	/**履行结束日期*/
	@Excel(name = "履行结束日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "履行结束日期")
    private java.util.Date conEndDate;
	/**合同附件*/
    @ApiModelProperty(value = "合同附件")
    private java.lang.String conAttachment;
    /**清单附件*/
    @ApiModelProperty(value = "清单附件")
    private java.lang.String conDetailList;
	/**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**updateBy*/
    @ApiModelProperty(value = "updateBy")
    private java.lang.String updateBy;
	/**updateTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "updateTime")
    private java.util.Date updateTime;
	/**delFlag*/
    @ApiModelProperty(value = "delFlag")
    private java.lang.String delFlag;

}
