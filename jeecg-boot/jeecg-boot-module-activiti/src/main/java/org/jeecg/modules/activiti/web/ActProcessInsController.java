package org.jeecg.modules.activiti.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.activiti.entity.*;
import org.jeecg.modules.activiti.service.Impl.ActBusinessServiceImpl;
import org.jeecg.modules.activiti.service.Impl.ActZprocessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pmc
 */
@Slf4j
@RestController
@RequestMapping("/actProcessIns")
@Transactional
public class ActProcessInsController {

    @Autowired
    private ActZprocessServiceImpl actZprocessService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActBusinessServiceImpl actBusinessService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

/*通过流程定义id获取第一个任务节点*/
    @ApiOperation(value = "通过流程定义id获取第一个任务节点")
    @RequestMapping(value = "/getFirstNode", method = RequestMethod.GET)
    public Result getFirstNode( String procDefId){
        ProcessNodeVo node = actZprocessService.getFirstNode(procDefId);
        //遍历users数组，去除当前项目中的user
        /*List<LoginUser> users = node.getUsers();
       for (LoginUser loginUser : users) {
			
		}*/
        
        return Result.ok(node);
    }
    /*获取运行中的流程实例*/
    @ApiOperation(value = "获取运行中的流程实例")
    @RequestMapping(value = "/getRunningProcess", method = RequestMethod.POST)
    public Result<Object> getRunningProcess(@RequestBody JSONObject requestJsonObj){
    	
    	String startTime = requestJsonObj.getString("startTime");
    	String endTime = requestJsonObj.getString("endTime");
    	String psType = requestJsonObj.getString("psType");
    	int nowPage = requestJsonObj.getIntValue("nowPage");
    	int perPage = requestJsonObj.getIntValue("perPage");
    	String prjId = requestJsonObj.getString("prjId");
        List<ProcessInsVo> list = new ArrayList<>();

        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery()
                .orderByProcessInstanceId().desc();
        query.orderByProcessInstanceId().desc();
        
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();
        //HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished().
        //      orderByProcessInstanceEndTime().desc();
        
        /*if(StrUtil.isNotBlank(name)){
            query.processInstanceNameLike("%"+name+"%");
        }
        //categoryId="oa";
         if(StrUtil.isNotBlank(categoryId)){
            query.processDefinitionCategory(categoryId);
        }
        if(StrUtil.isNotBlank(key)) {
            query.processDefinitionKey(key);
        }*/
        //query.processInstanceId("62501");
        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的type的pro_inst_id
        	//关联抄送人员
        	Set<String> procInstIdSet = actBusinessService.queryProcInstIds(userId, startTime, endTime, psType, prjId);
        	//查询当前用户所关联的流程实例 [80057, 85160, 72555, 72501, 85305, 97501, 72582, 62501, 80197, 72528, 85044]
        	query.processInstanceIds(procInstIdSet);
    	}
    	catch (Exception e) {
    		//e.addSuppressed(e);
    		return Result.ok(list);
    	}
        List<ProcessInstance> processInstanceList = query.listPage((nowPage-1)*perPage,perPage);//
        processInstanceList.forEach(e -> {
            list.add(new ProcessInsVo(e));
        });
        List<ComboModel> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        list.forEach(e -> {
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&& StrUtil.isNotBlank(hik.getUserId())){
                    e.setApplyer(userMap.get(hik.getUserId()));
                }
            }
            // 关联当前任务
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(e.getProcInstId()).list();
            if(taskList!=null&&taskList.size()==1){
                e.setCurrTaskName(taskList.get(0).getName());
            }else if(taskList!=null&&taskList.size()>1){
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<taskList.size()-1;i++){
                    sb.append(taskList.get(i).getName()+"、");
                }
                sb.append(taskList.get(taskList.size()-1).getName());
                e.setCurrTaskName(sb.toString());
            }
            // 关联流程表单路由
            ActZprocess actProcess = actZprocessService.getById(e.getProcDefId());
            if(actProcess!=null){
                e.setRouteName(actProcess.getRouteName());
            }
            // 关联业务表id
            ActBusiness actBusiness = actBusinessService.getById(e.getBusinessKey());
            if(actBusiness!=null){
                e.setTableId(actBusiness.getTableId());
                e.setTableName(actBusiness.getTableName());
            }
            
            //关联业务表数据
            if (StrUtil.isNotBlank(e.getTableName()) && e.getTableName().contains("BS_PROBLEM_SHEET")) {
            	System.out.println("BS_PROBLEM_SHEET:" + actBusiness.getTableId());
	            Map<String,Object> problemSheetMap = actBusinessService.getProblemSheetData(actBusiness.getTableId());
	            if (problemSheetMap != null && problemSheetMap.size() > 0) {
		            e.setPrjName(problemSheetMap.get("PRJ_NAME")+"");
		            e.setPsLocation(problemSheetMap.get("PS_LOCATION")+"");
		            e.setPsType(problemSheetMap.get("PS_TYPE")+"");
		            e.setPsLevel(problemSheetMap.get("PS_LEVEL")+"");
		            e.setPsDescription(problemSheetMap.get("PS_DESCRIPTION")+"");
		            e.setPsAdvice(problemSheetMap.get("PS_ADVICE")+"");
		            e.setPsTime(problemSheetMap.get("PS_TIME")+""); //整改单发起时间
		            e.setPsEndTime(problemSheetMap.get("PS_END_TIME")+"");//整改截止时间
		           // Object psFiles = problemSheetMap.get("prj_name");
		            List<String> problemSheetPhotoList = actBusinessService.getProblemSheetPhotoData(actBusiness.getTableId());
		            e.setPsFiles(problemSheetPhotoList);
	            }
            } else if (StrUtil.isNotBlank(e.getTableName()) && e.getTableName().contains("BS_USER")) {
            	System.out.println("BS_USER:" + actBusiness.getTableId());
            	Map<String,Object> userMap1 = actBusinessService.getUserData(actBusiness.getTableId());
            	e.setUserDepart(userMap1.get("USER_DEPART")+"");
            	e.setUserCom(userMap1.get("USER_COM")+"");
            	e.setUserPost(userMap1.get("USER_POST")+"");
            	String userPrj = "";
            	if (userMap.get("USER_PRJ") != null)
            		userPrj = (userMap.get("USER_PRJ")+"").toString(); 
            	List<String> prjNameList = new ArrayList<String>() ;
            	if (StrUtil.isNotBlank(userPrj)) {
            		String[] userPrjs = userPrj.split(","); 
            		for (int i = 0; i < userPrjs.length; i++) {
            			prjNameList.add(actBusinessService.getUserPrjData(userPrjs[i]));
					}
            	}
            	e.setUserPrjs(prjNameList);
            }
        });
        return Result.ok(list);
    	
    }
    
    
    /*通过id删除运行中的实例*/
    @RequestMapping(value = "/delInsByIds/{ids}", method=RequestMethod.GET)
    public Result<Object> delInsByIds(@PathVariable String ids,
                                     @RequestBody JSONObject josnObj){// @RequestParam(required = false, value="reason") String reason

    	String reason="";
        if(StrUtil.isBlank(reason)){
            reason = "";
        }
        for(String id : ids.split(",")){
            // 关联业务状态结束
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
            ActBusiness actBusiness = actBusinessService.getById(pi.getBusinessKey());
            actBusiness.setStatus(ActivitiConstant.STATUS_TO_APPLY);
            actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
            actBusinessService.updateById(actBusiness);
            runtimeService.deleteProcessInstance(id, ActivitiConstant.DELETE_PRE+reason);
        }
        return Result.ok("删除成功");
    }
    /*激活或挂起流程实例*/
    @RequestMapping(value = "/updateInsStatus", method = RequestMethod.POST)
    public Result<Object> updateStatus(@RequestParam String id,
                                       @RequestParam Integer status){

        if(ActivitiConstant.PROCESS_STATUS_ACTIVE.equals(status)){
            runtimeService.activateProcessInstanceById(id);
        }else if(ActivitiConstant.PROCESS_STATUS_SUSPEND.equals(status)){
            runtimeService.suspendProcessInstanceById(id);
        }

        return Result.ok("修改成功");
    }
    /*获取结束的的流程实例*/
    @ApiOperation(value = "获取结束的的流程实例")
    @RequestMapping(value = "/getFinishedProcess", method = RequestMethod.POST)
    public Result<Object> getFinishedProcess(@RequestBody JSONObject requestJsonObj){
    	String startTime = requestJsonObj.getString("startTime");
    	String endTime = requestJsonObj.getString("endTime");
    	String psType = requestJsonObj.getString("psType");
    	int nowPage = requestJsonObj.getIntValue("nowPage");
    	int perPage = requestJsonObj.getIntValue("perPage");
    	String prjId = requestJsonObj.getString("prjId");
        List<HistoricProcessInsVo> list = new ArrayList<>();

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished().
                orderByProcessInstanceEndTime().desc();
       // query.orderByProcessInstanceId().desc();
        query.orderByProcessInstanceStartTime().desc();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();

        /*if(StrUtil.isNotBlank(name)){
            query.processInstanceNameLike("%"+name+"%");
        }
        if(StrUtil.isNotBlank(categoryId)){
            query.processDefinitionCategory(categoryId);
        }
        if(StrUtil.isNotBlank(key)) {
            query.processDefinitionKey(key);
        }*/
        if(StrUtil.isNotBlank(startTime)){
            Date start = DateUtil.parse(startTime);
            query.finishedAfter(start);
        }
        if(StrUtil.isNotBlank(endTime)){
            Date end = DateUtil.parse(endTime);
            query.finishedBefore(DateUtil.endOfDay(end));
        }
        
        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的type的pro_inst_id
        	Set<String> procInstIdSet = actBusinessService.queryProcInstIds(userId, startTime, endTime, psType, prjId);
        	//查询当前用户所关联的流程实例
        	query.processInstanceIds(procInstIdSet);
    	}
    	catch (Exception e) {
    		//e.addSuppressed(e);
    		return Result.ok(list);
    	}
        
        List<HistoricProcessInstance> processInstanceList = query.listPage((nowPage-1)*perPage, perPage);
        processInstanceList.forEach(e -> {
            list.add(new HistoricProcessInsVo(e));
        });
        List<ComboModel> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        list.forEach(e -> {
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    e.setApplyer(userMap.get(hik.getUserId()));
                }
            }
            // 关联流程表单路由
            ActZprocess actProcess = actZprocessService.getById(e.getProcDefId());
            if(actProcess!=null){
                e.setRouteName(actProcess.getRouteName());
            }
            // 关联业务表id和结果
            ActBusiness actBusiness = actBusinessService.getById(e.getBusinessKey());
            if(actBusiness!=null){
                e.setTableId(actBusiness.getTableId());
                e.setTableName(actBusiness.getTableName());
                String reason = e.getDeleteReason();
                if(reason==null){
                    e.setResult(ActivitiConstant.RESULT_PASS);
                }else if(reason.contains(ActivitiConstant.CANCEL_PRE)){
                    e.setResult(ActivitiConstant.RESULT_CANCEL);
                    if(reason.length()>9){
                        e.setDeleteReason(reason.substring(9));
                    }else{
                        e.setDeleteReason("");
                    }
                }else if(ActivitiConstant.BACKED_FLAG.equals(reason)){
                    e.setResult(ActivitiConstant.RESULT_FAIL);
                    e.setDeleteReason("");
                }else if(reason.contains(ActivitiConstant.DELETE_PRE)){
                    e.setResult(ActivitiConstant.RESULT_DELETED);
                    if(reason.length()>8){
                        e.setDeleteReason(reason.substring(8));
                    }else{
                        e.setDeleteReason("");
                    }
                }else{
                    e.setResult(ActivitiConstant.RESULT_PASS);
                }
            }
          //关联业务表数据
            if (StrUtil.isNotBlank(e.getTableName()) && e.getTableName().contains("BS_PROBLEM_SHEET")) {
	            Map<String,Object> problemSheetMap = actBusinessService.getProblemSheetData(actBusiness.getTableId());
	            e.setPrjName(problemSheetMap.get("PRJ_NAME")+"");
	            e.setPsLocation(problemSheetMap.get("PS_LOCATION")+"");
	            e.setPsType(problemSheetMap.get("PS_TYPE")+"");
	            e.setPsLevel(problemSheetMap.get("PS_LEVEL")+"");
	            e.setPsDescription(problemSheetMap.get("PS_DESCRIPTION")+"");
	            e.setPsAdvice(problemSheetMap.get("PS_ADVICE")+"");
	            e.setPsTime(problemSheetMap.get("PS_TIME")+""); //整改单发起时间
	            e.setPsEndTime(problemSheetMap.get("PS_END_TIME")+"");//整改截止时间
	           // Object psFiles = problemSheetMap.get("prj_name");
	            List<String> problemSheetPhotoList = actBusinessService.getProblemSheetPhotoData(actBusiness.getTableId());
	            e.setPsFiles(problemSheetPhotoList);
            } else if (StrUtil.isNotBlank(e.getTableName()) && e.getTableName().contains("BS_USER")) {
            	Map<String,Object> userMap1 = actBusinessService.getUserData(actBusiness.getTableId());
            	e.setUserDepart(userMap1.get("USER_DEPART")+"");
            	e.setUserCom(userMap1.get("USER_COM")+"");
            	e.setUserPost(userMap1.get("USER_POST")+"");
            	String userPrj = "";
            	if (userMap.get("USER_PRJ") != null)
            		userPrj = (userMap.get("USER_PRJ")+"").toString(); 
            	List<String> prjNameList = new ArrayList<String>() ;
            	if (StrUtil.isNotBlank(userPrj)) {
            		String[] userPrjs = userPrj.split(","); 
            		for (int i = 0; i < userPrjs.length; i++) {
            			prjNameList.add(actBusinessService.getUserPrjData(userPrjs[i]));
					}
            	}
            	e.setUserPrjs(prjNameList);
            }
        });
        return Result.ok(list);
    }
    
    
    @RequestMapping(value = "/delHistoricInsByIds/{ids}")
    @ApiOperation(value = "通过id删除已结束的实例")
    public Result<Object> delHistoricInsByIds(@PathVariable String ids){

        for(String id : ids.split(",")){
            historyService.deleteHistoricProcessInstance(id);
        }
        return Result.ok("删除成功");
    }
    
    @ApiOperation(value = "首页流程统计数据") 
    @RequestMapping(value = "/getProcessNum" )
    public Result<Object> getProcessNum(){ 
    	JSONObject jsonObj = new JSONObject();
    	int todoProcessNum = todoProcessNum();
        int runningProcessNum = runningProcessNum();
        int finishedProcessNum = finishedProcessNum();
        jsonObj.put("todoProcessNum", todoProcessNum);
        jsonObj.put("runningProcessNum", runningProcessNum);
        jsonObj.put("finishedProcessNum", finishedProcessNum);
        return Result.ok(jsonObj);
    }
    
    @RequestMapping(value = "/todoListNum" )
    public int todoProcessNum( ){ 
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(userId);
       // taskService.createNativeTaskQuery().sql("");
        // 多条件搜索
        query.orderByTaskPriority().desc();
        query.orderByTaskCreateTime().desc();
        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的type的pro_inst_id
        	List<String> procInstIdList = actBusinessService.queryProcInstIdList(userId, "", "", "");
        	//查询当前用户所关联的流程实例
        	query.processInstanceIdIn(procInstIdList);
    	} catch (Exception e) {
    		//e.addSuppressed(e);
    		return 0;
    	}
        List<Task> taskList = query.list();
        return taskList.size();
    }
    
    public int runningProcessNum( ){
        List<ProcessInsVo> list = new ArrayList<>();

        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery()
                .orderByProcessInstanceId().desc();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();
        //HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished().
        //      orderByProcessInstanceEndTime().desc();
        
        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的type的pro_inst_id
        	Set<String> procInstIdSet = actBusinessService.queryProcInstIds(userId, "", "", "", "");
        	//查询当前用户所关联的流程实例
        	query.processInstanceIds(procInstIdSet);
    	}
    	catch (Exception e) {
    		//e.addSuppressed(e);
    		return 0;
    	}
        List<ProcessInstance> processInstanceList = query.list();
        processInstanceList.forEach(e -> {
            list.add(new ProcessInsVo(e));
        });
        List<ComboModel> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        list.forEach(e -> {
        	
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&& StrUtil.isNotBlank(hik.getUserId())){
                    e.setApplyer(userMap.get(hik.getUserId()));
                }
            }
        });
        return list.size();
    }
    
    /*获取结束的的流程实例*/
    public int finishedProcessNum(){

        List<HistoricProcessInsVo> list = new ArrayList<>();

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().finished().
                orderByProcessInstanceEndTime().desc();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getUsername();

        try {
        	//获取当前userId下的所有pro_inst_id
        	//获取当前userId下的所有在startTime和endTime之间的pro_inst_id
        	//根据获取到的pro_inst_id 关联对应tableId 筛选出对应的type的pro_inst_id
        	Set<String> procInstIdSet = actBusinessService.queryProcInstIds(userId, "", "", "", "");
        	//查询当前用户所关联的流程实例
        	query.processInstanceIds(procInstIdSet);
    	}
    	catch (Exception e) {
    		//e.addSuppressed(e);
    		return 0;
    	}
        List<HistoricProcessInstance> processInstanceList = query.list();
        processInstanceList.forEach(e -> {
            list.add(new HistoricProcessInsVo(e));
        });
        List<ComboModel> allUser = sysBaseAPI.queryAllUser();
        Map<String, String> userMap = allUser.stream().collect(Collectors.toMap(ComboModel::getUsername, ComboModel::getTitle));
        list.forEach(e -> {
            List<HistoricIdentityLink> identityLinks = historyService.getHistoricIdentityLinksForProcessInstance(e.getId());
            for(HistoricIdentityLink hik : identityLinks){
                // 关联发起人
                if("starter".equals(hik.getType())&&StrUtil.isNotBlank(hik.getUserId())){
                    e.setApplyer(userMap.get(hik.getUserId()));
                }
            }
        });
        return list.size();
    }

}
