package org.jeecg.modules.company.entity;

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
 * @Description: 单位信息
 * @Author: jeecg-boot
 * @Date:   2020-10-21
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_company")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_company对象", description="单位信息")
@KeySequence(value = "pjplat.SEQ_BS_COMPANY")
public class Company implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
    /**单位名称*/
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称")
    private java.lang.String comName;
    /**单位地址*/
    @Excel(name = "单位地址", width = 15)
    @ApiModelProperty(value = "单位地址")
    private java.lang.String comAddress;
    /**法定代表人*/
    @Excel(name = "法定代表人", width = 15)
    @ApiModelProperty(value = "法定代表人")
    private java.lang.String comLegalPerson;
    /**社会统一信用代码*/
    @Excel(name = "社会统一信用代码", width = 15)
    @ApiModelProperty(value = "社会统一信用代码")
    private java.lang.String comSocialCode;
    /**单位资质信息*/
    @Excel(name = "单位资质信息", width = 15)
    @ApiModelProperty(value = "单位资质信息")
    private java.lang.String comCertificate;
    /**单位银行信息*/
    @Excel(name = "单位银行信息", width = 15)
    @ApiModelProperty(value = "单位银行信息")
    private java.lang.String comBank;
    /**单位联系人*/
    @Excel(name = "单位联系人", width = 15)
    @ApiModelProperty(value = "单位联系人")
    private java.lang.String comContacts;
    /**单位联系电话*/
    @Excel(name = "单位联系电话", width = 15)
    @ApiModelProperty(value = "单位联系电话")
    private java.lang.String comPhone;
    /**经营范围*/
    @Excel(name = "经营范围", width = 15)
    @ApiModelProperty(value = "经营范围")
    private java.lang.String comBussinessScope;
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

    @ApiModelProperty(value = "delFlag")
    private java.lang.Boolean delFlag =false;
}
