package org.jeecg.modules.activiti.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.entity.*;
import org.jeecg.modules.activiti.service.Impl.ActBusinessServiceImpl;
import org.jeecg.modules.activiti.service.Impl.ActZprocessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pmc
 */
@Slf4j
@RestController
@RequestMapping("/actTask")
@Transactional
public class ActTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ActZprocessServiceImpl actZprocessService;

    @Autowired
    private ActBusinessServiceImpl actBusinessService;
    @Autowired
    ISysBaseAPI sysBaseAPI;
    
    @Value(value = "${jeecg.path.upload}")
	private String uploadpath;

    /*代办列表*/
    @RequestMapping(value = "/todoList" )
    public Result<Object> todoList(@RequestParam String name,String categoryId,  Integer priority, HttpServletRequest request){
        List<TaskVo> list = new ArrayList<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
       // taskService.createNativeTaskQuery().sql("");
        // 多条件搜索
        query.orderByTaskPriority().desc();
        query.orderByTaskCreateTime().desc();
        if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        //query.taskId("47522");
        String createTime_begin = request.getParameter("createTime_begin");
        String createTime_end = request.getParameter("createTime_end");
        if(StrUtil.isNotBlank(createTime_begin)&&StrUtil.isNotBlank(createTime_end)){
            Date start = DateUtil.parse(createTime_begin);
            Date end = DateUtil.parse(createTime_end);
            query.taskCreatedAfter(start);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }

        List<Task> taskList = query.list();

        // 转换vo
        taskList.forEach(e -> {
            TaskVo tv = new TaskVo(e);

            // 关联委托人
            if(StrUtil.isNotBlank(tv.getOwner())){
                String realname = sysBaseAPI.getUserByName(tv.getOwner()).getRealname();
                tv.setOwner(realname);
            }
            List<IdentityLink> identityLinks = runtimeService.getIdentityLinksForProcessInstance(tv.getProcInstId());
            for(IdentityLink ik : identityLinks){
                // 关联发起人
                if("starter".equals(ik.getType())&&StrUtil.isNotBlank(ik.getUserId())){
                    tv.setApplyer(sysBaseAPI.getUserByName(ik.getUserId()).getRealname());
                }
            }
            // 关联流程信息
            ActZprocess actProcess = actZprocessService.getById(tv.getProcDefId());
            if(actProcess!=null){
                tv.setProcessName(actProcess.getName());
                tv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(tv.getProcInstId()).singleResult();
            tv.setBusinessKey(pi.getBusinessKey());
            ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
            if(actBusiness!=null){
                tv.setTableId(actBusiness.getTableId());
                tv.setTableName(actBusiness.getTableName());
            }
          
            //关联业务表数据
            
            list.add(tv);
        });
        return Result.ok(list);
    }
    
    /*代办列表*/
    @ApiOperation(value = "代办列表App") 
    @RequestMapping(value = "/todoListApp", method = RequestMethod.POST)
    public Result<Object> todoListApp(@RequestBody JSONObject requestJsonObj){ 
    	String startTime = requestJsonObj.getString("startTime");
    	String endTime = requestJsonObj.getString("endTime");
    	String psType = requestJsonObj.getString("psType");
    	int nowPage = requestJsonObj.getIntValue("nowPage");
    	int perPage = requestJsonObj.getIntValue("perPage");
    	
        List<TaskVo> list = new ArrayList<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();
        //String userId = "zhagnxiao";
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
       // taskService.createNativeTaskQuery().sql("");
        // 多条件搜索
        //query.orderByTaskPriority().desc();
        query.orderByTaskCreateTime().desc();
       /* if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        //query.taskId("47522");
        String createTime_begin = request.getParameter("createTime_begin");
        String createTime_end = request.getParameter("createTime_end");
        if(StrUtil.isNotBlank(createTime_begin)&&StrUtil.isNotBlank(createTime_end)){
            Date start = DateUtil.parse(createTime_begin);
            Date end = DateUtil.parse(createTime_end);
            query.taskCreatedAfter(start);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }*/

        if(StrUtil.isNotBlank(startTime)){
            Date start = DateUtil.parse(startTime);
            query.taskCreatedAfter(start);
        }
        if(StrUtil.isNotBlank(endTime)){
            Date end = DateUtil.parse(endTime);
            query.taskCreatedBefore(DateUtil.endOfDay(end));
        }
        
        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的typcom_ide的pro_inst_id
        	List<String> procInstIdList = actBusinessService.queryProcInstIdList(userId, "", "", psType);
        	//查询当前用户所关联的流程实例
        	query.processInstanceIdIn(procInstIdList);
    	}
    	catch (Exception e) {
    		//e.addSuppressed(e);
    		return Result.ok(list);
    	}
        
        List<Task> taskList = query.listPage((nowPage-1)*perPage, perPage);

        // 转换vo
        taskList.forEach(e -> {
            TaskVo tv = new TaskVo(e);

            // 关联委托人
            if(StrUtil.isNotBlank(tv.getOwner())){
                String realname = sysBaseAPI.getUserByName(tv.getOwner()).getRealname();
                tv.setOwner(realname);
            }
            List<IdentityLink> identityLinks = runtimeService.getIdentityLinksForProcessInstance(tv.getProcInstId());
            for(IdentityLink ik : identityLinks){
                // 关联发起人
                if("starter".equals(ik.getType())&&StrUtil.isNotBlank(ik.getUserId())){
                    tv.setApplyer(sysBaseAPI.getUserByName(ik.getUserId()).getRealname());
                }
            }
            // 关联流程信息
            ActZprocess actProcess = actZprocessService.getById(tv.getProcDefId());
            if(actProcess!=null){
                tv.setProcessName(actProcess.getName());
                tv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(tv.getProcInstId()).singleResult();
            tv.setBusinessKey(pi.getBusinessKey());
            ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
            if(actBusiness!=null){
                tv.setTableId(actBusiness.getTableId());
                tv.setTableName(actBusiness.getTableName());
            }
            
            //关联业务表数据
            //if (StrUtil.isNotBlank(tv.getCategory()) && tv.getCategory().contains("problem")) {
            if (StrUtil.isNotBlank(tv.getTableName()) && tv.getTableName().contains("BS_PROBLEM_SHEET")) {
            	// 手动设置category的值，用于app判断流程类型
            	tv.setCategory("problem");
            	
	            Map<String,Object> problemSheetMap = actBusinessService.getProblemSheetData(actBusiness.getTableId());
	            tv.setPrjName(problemSheetMap.get("PRJ_NAME")+"");
	            tv.setPsLocation(problemSheetMap.get("PS_LOCATION")+"");
	            tv.setPsType(problemSheetMap.get("PS_TYPE")+"");
	            tv.setPsLevel(problemSheetMap.get("PS_LEVEL")+"");
	            tv.setPsDescription(problemSheetMap.get("PS_DESCRIPTION")+"");
	            tv.setPsAdvice(problemSheetMap.get("PS_ADVICE")+"");
	            tv.setPsTime(problemSheetMap.get("PS_TIME")+""); //整改单发起时间
	            tv.setPsEndTime(problemSheetMap.get("PS_END_TIME")+"");//整改截止时间
	           // Object psFiles = problemSheetMap.get("prj_name");
	            List<String> problemSheetPhotoList = actBusinessService.getProblemSheetPhotoData(actBusiness.getTableId());
	            tv.setPsFiles(problemSheetPhotoList);
            } else if (StrUtil.isNotBlank(tv.getTableName()) && tv.getTableName().contains("BS_USER")) {
            	// 手动设置category的值，用于app判断流程类型
            	tv.setCategory("user");
            	
            	Map<String,Object> userMap = actBusinessService.getUserData(actBusiness.getTableId());
            	tv.setUserDepart(userMap.get("USER_DEPART")+"");
            	tv.setUserCom(userMap.get("USER_COM")+"");
            	tv.setUserPost(userMap.get("USER_POST")+"");
            	String userPrj = "";
            	if (userMap.get("USER_PRJ") != null)
            		userPrj = (userMap.get("USER_PRJ")+"").toString(); 
            	//userPrj.replace("null", "");
            	List<String> prjNameList = new ArrayList<String>();
            	if (!"null".equals(userPrj) && StrUtil.isNotBlank(userPrj) ) {
            		String[] userPrjs = userPrj.split(","); 
            		for (int i = 0; i < userPrjs.length; i++) {
            			prjNameList.add(actBusinessService.getUserPrjData(userPrjs[i]));
					}
            	}
            	tv.setUserPrjs(prjNameList);
            }
            list.add(tv);
        });
        return Result.ok(list);
    }
    
    
    /*获取可返回的节点82554 */
    @ApiOperation(value = "activiti接口-获取可返回的节点", notes = "activiti接口-获取可返回的节点")
    @RequestMapping(value = "/getBackList/{procInstId}", method = RequestMethod.GET)
    public Result<Object> getBackList(@PathVariable String procInstId){

        List<HistoricTaskVo> list = new ArrayList<>();
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId)
                .finished().list();

        taskInstanceList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            list.add(htv);
        });

        // 去重
        LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
        List<HistoricTaskVo> newList = new ArrayList<>();
        list.forEach(e->{
            if(set.add(e.getName())){
                newList.add(e);
            }
        });

        return Result.ok(newList);
    }
    /*任务节点审批 驳回至发起人*/
    @RequestMapping(value = "/back", method = RequestMethod.GET)
    public Result<Object> back(@ApiParam("任务id") @RequestParam String id,
                               @ApiParam("流程实例id") @RequestParam String procInstId,
                               @ApiParam("意见评论") @RequestParam(required = false) String comment,
                               @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){


        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        // 删除流程实例
        runtimeService.deleteProcessInstance(procInstId, "backed");
        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
        actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
        actBusiness.setResult(ActivitiConstant.RESULT_FAIL);
        actBusinessService.updateById(actBusiness);
        // 异步发消息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        actZprocessService.sendMessage(sysUser,sysBaseAPI.getUserByName(actBusiness.getUserId()),ActivitiConstant.MESSAGE_BACK_CONTENT,
                String.format("您的 【%s】 申请已被驳回！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, sysUser.getUsername(), id, procInstId);
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"驳回");
        return Result.ok("操作成功");
    }
    /*任务节点审批 驳回至发起人*/
    @RequestMapping(value = "/backApp", method = RequestMethod.POST)
    public Result<Object> backApp(HttpServletRequest request){

    	String category = request.getParameter("category"); //任务类型   user|problem
    	String id = request.getParameter("id");  //任务id  taskId
    	String procInstId = request.getParameter("procInstId");   //流程实例id
    	String procDefId = request.getParameter("procDefId");   //流程定义id
    	String backTaskKey = request.getParameter("backTaskKey");   //驳回指定节点key
    	String assignees = request.getParameter("assignees");  //下个节点审批人
    	String comment = request.getParameter("comment"); //意见评论
    	Integer priority = Integer.parseInt(request.getParameter("priority"));  //优先级
    	Boolean sendMessage = Boolean.parseBoolean(request.getParameter("sendMessage")); //是否发送站内消息
    	Boolean sendSms = Boolean.parseBoolean(request.getParameter("sendSms"));  //是否发送短信通知
    	Boolean sendEmail = Boolean.parseBoolean(request.getParameter("sendEmail"));  //是否发送邮件通知

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        // 删除流程实例
        runtimeService.deleteProcessInstance(procInstId, "backed");
        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
        actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
        actBusiness.setResult(ActivitiConstant.RESULT_FAIL);
        actBusinessService.updateById(actBusiness);
        // 异步发消息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        actZprocessService.sendMessage(sysUser,sysBaseAPI.getUserByName(actBusiness.getUserId()),ActivitiConstant.MESSAGE_BACK_CONTENT,
                String.format("您的 【%s】 申请已被驳回！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, sysUser.getUsername(), id, procInstId);
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"驳回");
        return Result.ok("操作成功");
    }
    
    @RequestMapping(value = "/backToTaskApp", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批驳回至指定历史节点")
    public Result<Object> backToTaskApp(HttpServletRequest request){
    	
    	String category = request.getParameter("category"); //任务类型   user|problem
    	String id = request.getParameter("id");  //任务id  taskId
    	String procInstId = request.getParameter("procInstId");   //流程实例id
    	String procDefId = request.getParameter("procDefId");   //流程定义id
    	String backTaskKey = request.getParameter("backTaskKey");   //驳回指定节点key
    	String assignees = request.getParameter("assignees");  //下个节点审批人
    	String comment = request.getParameter("comment"); //意见评论
    	Integer priority = Integer.parseInt(request.getParameter("priority"));  //优先级
    	Boolean sendMessage = Boolean.parseBoolean(request.getParameter("sendMessage")); //是否发送站内消息
    	Boolean sendSms = Boolean.parseBoolean(request.getParameter("sendSms"));  //是否发送短信通知
    	Boolean sendEmail = Boolean.parseBoolean(request.getParameter("sendEmail"));  //是否发送邮件通知

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        taskService.addComment(id, procInstId, comment);
        if (StrUtil.isNotBlank(category)) {
        	if (category.contains("problem")) {
	        	//整改照片子表插入数据
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    		//MultipartFile file = multipartRequest.getFile("psFiles");// 获取上传文件对象
	    		List<MultipartFile> files =multipartRequest.getFiles("files");
	    		if (files != null && files.size() > 0) {
	    			//关联 ACT_Z_BUSINESS 查找业务表（整改单）的id
	                String tableId = actBusinessService.selectProblemSheetId(procInstId);
	    			for (MultipartFile multipartFile : files) {
	    				String filePath  = uploadLocal(multipartFile, "problem/zg", tableId);
	    				actBusinessService.saveProblemZgPhoto(tableId, id, filePath);
					}
	    		}
        	}
        }
        if  (backTaskKey.contains("发起人")) {
	        //驳回至发起人
	        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
	        // 删除流程实例
	        runtimeService.deleteProcessInstance(procInstId, "backed");
	        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
	        actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
	        actBusiness.setResult(ActivitiConstant.RESULT_FAIL);
	        actBusinessService.updateById(actBusiness);
	        // 异步发消息
	        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	        actZprocessService.sendMessage(sysUser,sysBaseAPI.getUserByName(actBusiness.getUserId()),ActivitiConstant.MESSAGE_BACK_CONTENT,
	                String.format("您的 【%s】 申请已被驳回！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
	        // 记录实际审批人员
	        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
	                ActivitiConstant.EXECUTOR_TYPE, sysUser.getUsername(), id, procInstId);
	        //修改业务表的流程字段
	        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"驳回");
        } else {
        	// 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
            // 获取历史任务的Activity
            ActivityImpl hisActivity = definition.findActivity(backTaskKey);
            // 实现跳转
            managementService.executeCommand(new JumpTask(procInstId, hisActivity.getId()));
            // 重新分配原节点审批人
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
            if(tasks!=null&&tasks.size()>0){
                tasks.forEach(e->{
                    for(String assignee:assignees.split(",")){
                        taskService.addCandidateUser(e.getId(), assignee);
                        // 异步发消息
                        actZprocessService.sendMessage(loginUser,sysBaseAPI.getUserByName(assignee),ActivitiConstant.MESSAGE_TODO_CONTENT
                        ,"您有一个任务待审批，请尽快处理！",sendMessage, sendSms, sendEmail);
                    }
                    if(priority!=null){
                        taskService.setPriority(e.getId(), priority);
                    }
                });
            }
            // 记录实际审批人员
            actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                    ActivitiConstant.EXECUTOR_TYPE, loginUser.getUsername(), id, procInstId);
        }
        return Result.ok("操作成功");
    }
    /*流程流转历史 80227*/
    @ApiOperation(value = "流程流转历史") 
    @RequestMapping(value = "/historicFlow/{id}", method = RequestMethod.GET)
    public Result<Object> historicFlow(@PathVariable String id){

        List<HistoricTaskVo> list = new ArrayList<>();

        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(id).orderByHistoricTaskInstanceEndTime().asc().list();

        // 转换vo
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            List<Assignee> assignees = new ArrayList<>();
            htv.setAssignee("");
            // 关联分配人（委托用户时显示该人）
            if(StrUtil.isNotBlank(htv.getAssignee())){
                String assignee = sysBaseAPI.getUserByName(htv.getAssignee()).getRealname();
                String owner = sysBaseAPI.getUserByName(htv.getOwner()).getRealname();
                assignees.add(new Assignee(assignee+"(受"+owner+"委托)", true));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForTask(e.getId());
            // 获取实际审批用户id
            String userId = actBusinessService.findUserIdByTypeAndTaskId(ActivitiConstant.EXECUTOR_TYPE, e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联候选用户（分配的候选用户审批人）
                if("candidate".equals(hik.getType())&& StrUtil.isNotBlank(hik.getUserId())){  //1321329181190291458
                    String username = sysBaseAPI.getUserByName(hik.getUserId()).getRealname();
                    Assignee assignee = new Assignee(username, false);
                    if(StrUtil.isNotBlank(userId)&&userId.equals(hik.getUserId())){
                        assignee.setIsExecutor(true);
                    }
                    assignees.add(assignee);
                }
            }
            htv.setAssignees(assignees);
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            list.add(htv);
        });
        return Result.ok(list);
    }
    @RequestMapping(value = "/pass", method = RequestMethod.GET)
    @ApiOperation(value = "任务节点审批通过") //files 整改图片
    public Result<Object> pass(@ApiParam("任务id") @RequestParam String id,
                               @ApiParam("流程实例id") @RequestParam String procInstId,
                               @ApiParam("下个节点审批人") @RequestParam(required = false) String assignees,
                               @ApiParam("优先级") @RequestParam(required = false) Integer priority,
                               @ApiParam("意见评论") @RequestParam(required = false) String comment,
                               @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                               @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                               @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
        //commentID取出来
        //整改照片子表插入数据
        ///。。。。。。
        
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();//35001
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
            // 未解决的委托任务 先resolve
            String oldAssignee = task.getAssignee();
            taskService.resolveTask(id);
            taskService.setAssignee(id, oldAssignee);
        }
        taskService.complete(id);
        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批中-"+task.getName());

        task.getName();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        // 判断下一个节点
        if(tasks!=null&&tasks.size()>0){
            for(Task t : tasks){
                if(StrUtil.isBlank(assignees)){
                    // 如果下个节点未分配审批人为空 取消结束流程
                    List<LoginUser> users = actZprocessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                        actBusinessService.updateById(actBusiness);
                        //修改业务表的流程字段
                        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批异常-"+task.getName()+"-审批节点未分配审批人，流程自动中断取消");

                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            // 分配了节点负责人分发给全部
                            for (LoginUser user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId());
                                // 异步发消息
                                actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            }
                            taskService.setPriority(t.getId(), task.getPriority());
                        }
                    }
                }else{
                    // 避免重复添加
                    List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                    if(list==null||list.size()==0) {

                        for(String assignee : assignees.split(",")){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // 异步发消息
                            LoginUser user = sysBaseAPI.getUserByName(assignee);
                            actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            taskService.setPriority(t.getId(), priority);
                        }
                    }
                }
            }
        } else {
            actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
            actBusiness.setResult(ActivitiConstant.RESULT_PASS);
            actBusinessService.updateById(actBusiness);
            // 异步发消息
            LoginUser user = sysBaseAPI.getUserByName(actBusiness.getUserId());
            actZprocessService.sendMessage(loginUser,user,ActivitiConstant.MESSAGE_PASS_CONTENT,
                    String.format("您的 【%s】 申请已通过！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
            //修改业务表的流程字段
            actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批通过");

        }
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, loginUser.getUsername(), id, procInstId);
        return Result.ok("操作成功");
    }
    
    @RequestMapping(value = "/passapp", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批通过") //files 整改图片
    public Result<Object> passapp(HttpServletRequest request){

    	String category = request.getParameter("category"); //任务类型   user|problem
    	String id = request.getParameter("id");  //任务id  taskId
    	String procInstId = request.getParameter("procInstId");   //流程实例id
    	String assignees = request.getParameter("assignees");  //下个节点审批人
    	String comment = request.getParameter("comment"); //意见评论
    	Integer priority = Integer.parseInt(request.getParameter("priority"));  //优先级
    	Boolean sendMessage = Boolean.parseBoolean(request.getParameter("sendMessage")); //是否发送站内消息
    	Boolean sendSms = Boolean.parseBoolean(request.getParameter("sendSms"));  //是否发送短信通知
    	Boolean sendEmail = Boolean.parseBoolean(request.getParameter("sendEmail"));  //是否发送邮件通知

    	
        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
       
        if (StrUtil.isNotBlank(category)) {
        	if (category.contains("problem")) {
	        	//整改照片子表插入数据
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    		//MultipartFile file = multipartRequest.getFile("psFiles");// 获取上传文件对象
	    		List<MultipartFile> files =multipartRequest.getFiles("psFiles");
	    		if (files != null && files.size() > 0) {
	    			//关联 ACT_Z_BUSINESS 查找业务表（整改单）的id
	                String tableId = actBusinessService.selectProblemSheetId(procInstId);
	    			for (MultipartFile multipartFile : files) {
	    				String filePath  = uploadLocal(multipartFile, "problem/zg", tableId+"_"+files.indexOf(multipartFile));
	    				actBusinessService.saveProblemZgPhoto(tableId, id, filePath);
					}
	    		}
        	} else if (category.contains("user")) {
        		//用户审批通过-进行部门和权限的同步（同步到sys_depart,sys_role）
        		 actBusinessService.syncUserDepartAndRole(procInstId);
        	}
        }
        
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();//35001
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
            // 未解决的委托任务 先resolve
            String oldAssignee = task.getAssignee();
            taskService.resolveTask(id);
            taskService.setAssignee(id, oldAssignee);
        }
        taskService.complete(id);
        //taskService.complete(arg0, arg1);
        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批中-"+task.getName());

        task.getName();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        // 判断下一个节点
        if(tasks!=null&&tasks.size()>0){
            for(Task t : tasks){
                if(StrUtil.isBlank(assignees)){
                    // 如果下个节点未分配审批人为空 取消结束流程
                    List<LoginUser> users = actZprocessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                        actBusinessService.updateById(actBusiness);
                        //修改业务表的流程字段
                        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批异常-"+task.getName()+"-审批节点未分配审批人，流程自动中断取消");

                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            // 分配了节点负责人分发给全部
                            for (LoginUser user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId());
                                // 异步发消息
                                actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            }
                            taskService.setPriority(t.getId(), task.getPriority());
                        }
                    }
                }else{
                    // 避免重复添加
                    List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                    if(list==null||list.size()==0) {

                        for(String assignee : assignees.split(",")){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // 异步发消息
                            LoginUser user = sysBaseAPI.getUserByName(assignee);
                            actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            taskService.setPriority(t.getId(), priority);
                        }
                    }
                }
            }
        } else {
            actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
            actBusiness.setResult(ActivitiConstant.RESULT_PASS);
            actBusinessService.updateById(actBusiness);
            // 异步发消息
            LoginUser user = sysBaseAPI.getUserByName(actBusiness.getUserId());
            actZprocessService.sendMessage(loginUser,user,ActivitiConstant.MESSAGE_PASS_CONTENT,
                    String.format("您的 【%s】 申请已通过！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
            //修改业务表的流程字段
            actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批通过");

        }
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, loginUser.getUsername(), id, procInstId);
        return Result.ok("操作成功");
    }
    
    @RequestMapping(value = "/notpassapp", method = RequestMethod.POST)
    @ApiOperation(value = "任务节点审批不通过") //files 整改图片
    public Result<Object> notpassapp(HttpServletRequest request){

    	String category = request.getParameter("category"); //任务类型   user|problem
    	String id = request.getParameter("id");  //任务id  taskId
    	String procInstId = request.getParameter("procInstId");   //流程实例id
    	String assignees = request.getParameter("assignees");  //下个节点审批人
    	String comment = request.getParameter("comment"); //意见评论
    	Integer priority = Integer.parseInt(request.getParameter("priority"));  //优先级
    	Boolean sendMessage = Boolean.parseBoolean(request.getParameter("sendMessage")); //是否发送站内消息
    	Boolean sendSms = Boolean.parseBoolean(request.getParameter("sendSms"));  //是否发送短信通知
    	Boolean sendEmail = Boolean.parseBoolean(request.getParameter("sendEmail"));  //是否发送邮件通知

    	
        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        taskService.addComment(id, procInstId, comment);
       
        if (StrUtil.isNotBlank(category)) {
        	if (category.contains("problem")) {
	        	//整改照片子表插入数据
	        	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    		//MultipartFile file = multipartRequest.getFile("psFiles");// 获取上传文件对象
	    		List<MultipartFile> files =multipartRequest.getFiles("files");
	    		if (files != null && files.size() > 0) {
	    			//关联 ACT_Z_BUSINESS 查找业务表（整改单）的id
	                String tableId = actBusinessService.selectProblemSheetId(procInstId);
	    			for (MultipartFile multipartFile : files) {
	    				String filePath  = uploadLocal(multipartFile, "problem/zg", tableId);
	    				actBusinessService.saveProblemZgPhoto(tableId, id, filePath);
					}
	    		}
        	}
        }
        
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();//35001
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        if(StrUtil.isNotBlank(task.getOwner())&&!("RESOLVED").equals(task.getDelegationState().toString())){
            // 未解决的委托任务 先resolve
            String oldAssignee = task.getAssignee();
            taskService.resolveTask(id);
            taskService.setAssignee(id, oldAssignee);
        }
        taskService.complete(id);
        //taskService.complete(arg0, arg1);
        ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批中-"+task.getName());

        task.getName();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        // 判断下一个节点
        if(tasks!=null&&tasks.size()>0){
            for(Task t : tasks){
                if(StrUtil.isBlank(assignees)){
                    // 如果下个节点未分配审批人为空 取消结束流程
                    List<LoginUser> users = actZprocessService.getNode(t.getTaskDefinitionKey()).getUsers();
                    if(users==null||users.size()==0){
                        runtimeService.deleteProcessInstance(procInstId, "canceled-审批节点未分配审批人，流程自动中断取消");
                        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
                        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
                        actBusinessService.updateById(actBusiness);
                        //修改业务表的流程字段
                        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批异常-"+task.getName()+"-审批节点未分配审批人，流程自动中断取消");

                        break;
                    }else{
                        // 避免重复添加
                        List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                        if(list==null||list.size()==0) {
                            // 分配了节点负责人分发给全部
                            for (LoginUser user : users) {
                                taskService.addCandidateUser(t.getId(), user.getId());
                                // 异步发消息
                                actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            }
                            taskService.setPriority(t.getId(), task.getPriority());
                        }
                    }
                }else{
                    // 避免重复添加
                    List<String> list = actBusinessService.selectIRunIdentity(t.getId(), "candidate");
                    if(list==null||list.size()==0) {

                        for(String assignee : assignees.split(",")){
                            taskService.addCandidateUser(t.getId(), assignee);
                            // 异步发消息
                            LoginUser user = sysBaseAPI.getUserByName(assignee);
                            actZprocessService.sendActMessage(loginUser,user,actBusiness,task.getName(),  sendMessage, sendSms, sendEmail);
                            taskService.setPriority(t.getId(), priority);
                        }
                    }
                }
            }
        } else {
            actBusiness.setStatus(ActivitiConstant.STATUS_FINISH);
            actBusiness.setResult(ActivitiConstant.RESULT_PASS);
            actBusinessService.updateById(actBusiness);
            // 异步发消息
            LoginUser user = sysBaseAPI.getUserByName(actBusiness.getUserId());
            //actZprocessService.sendMessage(loginUser,user,ActivitiConstant.MESSAGE_PASS_CONTENT,
           //         String.format("您的 【%s】 申请已通过！",actBusiness.getTitle()),sendMessage, sendSms, sendEmail);
            //修改业务表的流程字段
            actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"审批不通过");

        }
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, loginUser.getUsername(), id, procInstId);
        return Result.ok("操作成功");
    }
    
    @RequestMapping(value = "/delegate", method = RequestMethod.POST)
    @ApiOperation(value = "委托他人代办")
    public Result<Object> delegate(@ApiParam("任务id") @RequestParam String id,
                                   @ApiParam("委托用户id") @RequestParam String userId,
                                   @ApiParam("流程实例id") @RequestParam String procInstId,
                                   @ApiParam("意见评论") @RequestParam(required = false) String comment,
                                   @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                   @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                   @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){

        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        taskService.addComment(id, procInstId, comment);
        taskService.delegateTask(id, userId);
        taskService.setOwner(id, sysUser.getUsername());
        // 异步发消息
        actZprocessService.sendMessage(sysUser,sysBaseAPI.getUserByName(userId),ActivitiConstant.MESSAGE_DELEGATE_CONTENT,
                String.format("您有一个来自 %s 的委托需要处理！",sysUser.getRealname()),sendMessage, sendSms, sendEmail);
        return Result.ok("操作成功");
    }
    @RequestMapping(value = "/backToTask", method = RequestMethod.GET)
    @ApiOperation(value = "任务节点审批驳回至指定历史节点")
    public Result<Object> backToTask(@ApiParam("任务id") @RequestParam String id,
                                     @ApiParam("驳回指定节点key") @RequestParam String backTaskKey,
                                     @ApiParam("流程实例id") @RequestParam String procInstId,
                                     @ApiParam("流程定义id") @RequestParam String procDefId,
                                     @ApiParam("原节点审批人") @RequestParam(required = false) String assignees,
                                     @ApiParam("优先级") @RequestParam(required = false) Integer priority,
                                     @ApiParam("意见评论") @RequestParam(required = false) String comment,
                                     @ApiParam("是否发送站内消息") @RequestParam(defaultValue = "false") Boolean sendMessage,
                                     @ApiParam("是否发送短信通知") @RequestParam(defaultValue = "false") Boolean sendSms,
                                     @ApiParam("是否发送邮件通知") @RequestParam(defaultValue = "false") Boolean sendEmail){


        if(StrUtil.isBlank(comment)){
            comment = "";
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        taskService.addComment(id, procInstId, comment);
        
        // 取得流程定义
        ProcessDefinitionEntity definition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(procDefId);
        // 获取历史任务的Activity
        ActivityImpl hisActivity = definition.findActivity(backTaskKey);
        // 实现跳转
        managementService.executeCommand(new JumpTask(procInstId, hisActivity.getId()));
        // 重新分配原节点审批人
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procInstId).list();
        if(tasks!=null&&tasks.size()>0){
            tasks.forEach(e->{
                for(String assignee:assignees.split(",")){
                    taskService.addCandidateUser(e.getId(), assignee);
                    // 异步发消息
                    actZprocessService.sendMessage(loginUser,sysBaseAPI.getUserByName(assignee),ActivitiConstant.MESSAGE_TODO_CONTENT
                    ,"您有一个任务待审批，请尽快处理！",sendMessage, sendSms, sendEmail);
                }
                if(priority!=null){
                    taskService.setPriority(e.getId(), priority);
                }
            });
        }
        // 记录实际审批人员
        actBusinessService.insertHI_IDENTITYLINK(IdUtil.simpleUUID(),
                ActivitiConstant.EXECUTOR_TYPE, loginUser.getUsername(), id, procInstId);
        return Result.ok("操作成功");
    }
    
   

    public class JumpTask implements Command<ExecutionEntity> {

        private String procInstId;
        private String activityId;

        public JumpTask(String procInstId, String activityId) {
            this.procInstId = procInstId;
            this.activityId = activityId;
        }

        @Override
        public ExecutionEntity execute(CommandContext commandContext) {

            ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(procInstId);
            executionEntity.destroyScope("backed");
            ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
            ActivityImpl activity = processDefinition.findActivity(activityId);
            executionEntity.executeActivity(activity);

            return executionEntity;
        }

    }
    /*已办列表*/
    @RequestMapping(value = "/doneList")
    public Result<Object> doneList(@RequestBody JSONObject jsonObj,
                                   HttpServletRequest req){
    	String name = jsonObj.getString("name");
    	String categoryId = null;//jsonObj.getString("categoryId");
    	Integer priority = null;//jsonObj.getInteger("priority");
    	
        List<HistoricTaskVo> list = new ArrayList<>();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = loginUser.getUsername();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().or().taskCandidateUser(userId).
                taskAssignee(userId).endOr().finished();

        // 多条件搜索
        query.orderByTaskCreateTime().desc();
        if(StrUtil.isNotBlank(name)){
            query.taskNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.taskCategory(categoryId);
        }
        if(priority!=null){
            query.taskPriority(priority);
        }
        List<HistoricTaskInstance> taskList = query.list();
        // 转换vo
        List<ComboModel> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        taskList.forEach(e -> {
            HistoricTaskVo htv = new HistoricTaskVo(e);
            // 关联委托人
            if(StrUtil.isNotBlank(htv.getOwner())){
                htv.setOwner(userMap.get(htv.getOwner()));
            }
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(htv.getProcInstId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    htv.setApplyer(userMap.get(hik.getUserId()));
                }
            }
            // 关联审批意见
            List<Comment> comments = taskService.getTaskComments(htv.getId(), "comment");
            if(comments!=null&&comments.size()>0){
                htv.setComment(comments.get(0).getFullMessage());
            }
            // 关联流程信息
            ActZprocess actProcess = actZprocessService.getById(htv.getProcDefId());
            if(actProcess!=null){
                htv.setProcessName(actProcess.getName());
                htv.setRouteName(actProcess.getRouteName());
            }
            // 关联业务key
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(htv.getProcInstId()).singleResult();
            htv.setBusinessKey(hpi.getBusinessKey());
            ActBusiness actBusiness = actBusinessService.getById(hpi.getBusinessKey());
            if(actBusiness!=null){
                htv.setTableId(actBusiness.getTableId());
                htv.setTableName(actBusiness.getTableName());
            }

            list.add(htv);
        });
        return Result.ok(list);
    }
    /*删除任务历史*/
    @RequestMapping(value = "/deleteHistoric/{ids}", method = RequestMethod.POST)
    public Result<Object> deleteHistoric( @PathVariable String ids){

        for(String id : ids.split(",")){
            historyService.deleteHistoricTaskInstance(id);
        }
        return Result.ok("操作成功");
    }
    
    /**
   	 * 本地文件上传
   	 * @param mf 文件
   	 * @param bizPath  自定义路径
   	 * @return
   	 */
   	private String uploadLocal(MultipartFile mf,String bizPath, String tableId){
   		try {
   			String ctxPath = uploadpath;
   			String fileName = null;
   			File file = new File(ctxPath + File.separator + bizPath + File.separator );
   			if (!file.exists()) {
   				file.mkdirs();// 创建文件根目录
   			}
   			/*String orgName = mf.getOriginalFilename();// 获取文件名
   			orgName = CommonUtils.getFileName(orgName);
   			if(orgName.indexOf(".")!=-1){
   				fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
   			}else{
   				fileName = orgName+ "_" + System.currentTimeMillis();
   			}*/
   			
   			fileName = tableId + "_" + System.currentTimeMillis() + ".jpg";
   			String savePath = file.getPath() + File.separator + fileName;
   			File savefile = new File(savePath);
   			FileCopyUtils.copy(mf.getBytes(), savefile);
   			String dbpath = null;
   			if(oConvertUtils.isNotEmpty(bizPath)){
   				dbpath = bizPath + File.separator + fileName;
   			}else{
   				dbpath = fileName;
   			}
   			if (dbpath.contains("\\")) {
   				dbpath = dbpath.replace("\\", "/");
   			}
   			return dbpath;
   		} catch (IOException e) {
   			log.error(e.getMessage(), e);
   		}
   		return "";
   	}
   	
}
