package org.jeecg.modules.activiti.entity;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.activiti.engine.history.HistoricTaskInstance;

import java.util.Date;
import java.util.List;

/**
 * @author pmc
 */
@Data
public class HistoricTaskVo {

    private String id;

    private String name;

    private String key;

    private String description;

    private String executionId;

    private String assignee;

    private String owner;

    private String procDefId;

    private String procInstId;

    private String applyer;

    private String category;

    private Integer priority;

    private String deleteReason;

    private String comment;

    private Long duration;

    private Long workTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;

    private String processName;

    private String routeName;

    private String businessKey;

    private String tableId;
    private String tableName;

    private List<Assignee> assignees;

    public HistoricTaskVo(HistoricTaskInstance task){
    	if (StrUtil.isNotBlank(task.getId()))
    		this.id = task.getId();
    	if (StrUtil.isNotBlank(task.getName()))
    		this.name = task.getName();
    	if (StrUtil.isNotBlank(task.getTaskDefinitionKey()))
    		this.key = task.getTaskDefinitionKey();
    	if (StrUtil.isNotBlank(task.getDescription()))
    		this.description = task.getDescription();
    	if (StrUtil.isNotBlank(task.getExecutionId()))
    		this.executionId = task.getExecutionId();
    	if (StrUtil.isNotBlank(task.getAssignee()))
    		this.assignee = task.getAssignee();
    	if (StrUtil.isNotBlank(task.getOwner()))
    		this.owner = task.getOwner();
    	if (StrUtil.isNotBlank(task.getProcessDefinitionId()))
    		this.procDefId = task.getProcessDefinitionId();
    	if (StrUtil.isNotBlank(task.getProcessInstanceId()))
    		this.procInstId = task.getProcessInstanceId();
    	if (StrUtil.isNotBlank(task.getPriority()+""))
    		this.priority = task.getPriority();
    	if (StrUtil.isNotBlank(task.getCategory()))
    		this.category = task.getCategory();
    	if (StrUtil.isNotBlank(task.getDeleteReason()))
    		this.deleteReason = getMyDeleteReason(task.getDeleteReason());
    	if (StrUtil.isNotBlank(task.getDurationInMillis()+""))
    		this.duration = task.getDurationInMillis();
    	if (StrUtil.isNotBlank(task.getWorkTimeInMillis()+""))
    		this.workTime = task.getWorkTimeInMillis();
    	if (StrUtil.isNotBlank(task.getCreateTime()+""))
    		this.createTime = task.getCreateTime();
    	if (StrUtil.isNotBlank(task.getStartTime()+""))
    		this.startTime = task.getStartTime();
    	if (StrUtil.isNotBlank(task.getEndTime()+""))
    		this.endTime = task.getEndTime();
    	if (StrUtil.isNotBlank(task.getDueDate()+""))
    		this.dueTime = task.getDueDate();
    }

    public String getMyDeleteReason(String deleteReason){

        if(StrUtil.isBlank(deleteReason)){
            return "";
        }
        if(ActivitiConstant.PASSED_FLAG.equals(deleteReason)){
            deleteReason = "审批通过";
        }else if(ActivitiConstant.BACKED_FLAG.equals(deleteReason)){
            deleteReason = "审批驳回";
        }else if(deleteReason.contains(ActivitiConstant.DELETE_PRE)){
            String reason = "";
            if(ActivitiConstant.DELETE_PRE.equals(deleteReason)){
                reason = "未填写";
            }else if(deleteReason.length()>8){
                reason = deleteReason.substring(8);
            }
            deleteReason = "删除撤回-原因"+reason;
        }else if(deleteReason.contains(ActivitiConstant.CANCEL_PRE)){
            String reason = "";
            if(ActivitiConstant.CANCEL_PRE.equals(deleteReason)){
                reason = "未填写";
            }else if(deleteReason.length()>9){
                reason = deleteReason.substring(9);
            }
            deleteReason = "发起人撤回-原因"+reason;
        }else{
            deleteReason="已删除-原因"+deleteReason;
        }
        return deleteReason;
    }
}
