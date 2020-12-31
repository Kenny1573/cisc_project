package org.jeecg.modules.scheduleplan.entity;

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
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 里程碑计划
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("pjplat.bs_schedule_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_schedule_plan对象", description="里程碑计划")
@KeySequence(value = "pjplat.SEQ_BS_SCHEDULE_PLAN",clazz = Long.class)
public class SchedulePlan implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.Long spPrjId;
	/**节点编号*/
	@Excel(name = "节点编号", width = 15)
    @ApiModelProperty(value = "节点编号")
    private java.lang.String spNodeNumber;
	/**父节点ID*/
	@Excel(name = "父节点ID", width = 15)
    @ApiModelProperty(value = "父节点ID")
    private java.lang.Long spNodeParent;
	/**节点名称*/
	@Excel(name = "节点名称", width = 15)
    @ApiModelProperty(value = "节点名称")
    private java.lang.String spNodeName;
	/**节点状态*/
	@Dict(dicCode = "scheduleplan_status")
	@Excel(name = "节点状态", width = 15)
    @ApiModelProperty(value = "节点状态")
    private java.lang.String spNodeState;
	/**计划开始时间*/
    @Excel(name = "计划开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划开始时间")
    private java.util.Date spNodeBeginTime;
    /**计划完成时间*/
    @Excel(name = "计划完成时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "计划完成时间")
    private java.util.Date spNodeEndTime;
    /**实际开始时间*/
    @Excel(name = "实际开始时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际开始时间")
    private java.util.Date spNodeInFactBegin;
    /**计划完成时间*/
    @Excel(name = "实际完成时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "实际完成时间")
    private java.util.Date spNodeInFactEnd;
	/**责任人*/
	@Excel(name = "责任人", width = 15)
    @ApiModelProperty(value = "责任人")
    private java.lang.String spNodePerson;
	/**上传附件*/
	@Excel(name = "上传附件", width = 15)
    @ApiModelProperty(value = "上传附件")
    private java.lang.String spNodeAttachment;
	/**是否里程碑节点*/
	@Excel(name = "是否里程碑节点", width = 15)
    @ApiModelProperty(value = "是否里程碑节点")
    private java.lang.String spIsMilestone;
	/**createBy*/
    @ApiModelProperty(value = "createBy")
    private java.lang.String createBy;
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

    SchedulePlan(String spNodeName){
        this.spNodeName = spNodeName;

    }
}
