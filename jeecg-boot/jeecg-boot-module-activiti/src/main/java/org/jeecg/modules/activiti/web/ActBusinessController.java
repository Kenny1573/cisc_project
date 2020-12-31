package org.jeecg.modules.activiti.web;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.activiti.entity.ActBusiness;
import org.jeecg.modules.activiti.entity.ActZprocess;
import org.jeecg.modules.activiti.entity.ActivitiConstant;
import org.jeecg.modules.activiti.entity.ProblemSheet;
import org.jeecg.modules.activiti.service.Impl.ActBusinessServiceImpl;
import org.jeecg.modules.activiti.service.Impl.ActZprocessServiceImpl;
import org.jeecgframework.codegenerate.generate.util.SimpleFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *@author PanMeiCheng
 *@date 2020-04-02
 *@version 1.0
 */
@RestController
@RequestMapping("/actBusiness")
@Slf4j
@Transactional
public class ActBusinessController {
    @Autowired
    ActBusinessServiceImpl actBusinessService;
    @Autowired
    ActZprocessServiceImpl actZprocessService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    ISysBaseAPI sysBaseAPI;
    
    @Value(value = "${jeecg.path.upload}")
	private String uploadpath;
   
    /*获取业务表单信息*/
    @RequestMapping(value = "/getForm", method = RequestMethod.GET)
    public Result getForm(HttpServletRequest request){
        /*保存业务表单数据到数据库表*/
        String tableId = request.getParameter("tableId");
        String tableName = request.getParameter("tableName");
        if (StrUtil.isBlank(tableName)){
            return Result.error("参数缺省！");
        }
        Map<String, Object> applyForm = actBusinessService.getApplyForm(tableId, tableName);
        return Result.ok(applyForm);
    }
    /*修改业务表单信息*/
    @RequestMapping(value = "/editForm", method = RequestMethod.POST)
    public Result editForm(HttpServletRequest request){
        /*保存业务表单数据到数据库表*/
        String tableId = request.getParameter("id");
        actBusinessService.saveApplyForm(tableId,request);
        return Result.ok();
    }
    /*通过id删除草稿状态申请*/
    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    public Result delByIds(String ids){

        for(String id : ids.split(",")){
            ActBusiness actBusiness = actBusinessService.getById(id);
            if(actBusiness.getStatus()!=ActivitiConstant.STATUS_TO_APPLY){
                return Result.error("删除失败, 仅能删除草稿状态的申请");
            }
            // 删除关联业务表
            actBusinessService.deleteBusiness(actBusiness.getTableName(), actBusiness.getTableId());
            actBusinessService.removeById(id);
        }
        return Result.ok("删除成功");
    }
    /*添加申请草稿状态*/
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public Result add(HttpServletRequest request){
    	
        String procDefId = request.getParameter("procDefId");
        String procDeTitle = request.getParameter("procDeTitle");
        String tableName = request.getParameter("tableName");
        /*保存业务表单数据到数据库表*/
        String tableId = IdUtil.simpleUUID();
        actBusinessService.saveApplyForm(tableId,request);
        // 保存至我的申请业务
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = sysUser.getUsername();
        ActBusiness actBusiness = new ActBusiness();
        actBusiness.setUserId(username);
        actBusiness.setTableId(tableId);
        actBusiness.setProcDefId(procDefId);
        String title = request.getParameter(ActivitiConstant.titleKey);
        if (StrUtil.isNotBlank(title)){
            actBusiness.setTitle(title);
        }else {
            actBusiness.setTitle(procDeTitle);
        }
        actBusiness.setTableName(tableName);
        actBusinessService.save(actBusiness);
        return Result.ok();
    }
    /*提交申请 启动流程*/
    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    public Result apply(@RequestBody ActBusiness act){

        ActBusiness actBusiness = actBusinessService.getById(act.getId());
        if(actBusiness==null){
            return Result.error("actBusiness表中该id不存在");
        }
        String tableId = actBusiness.getTableId();
        String tableName = actBusiness.getTableName();
        act.setTableId(tableId);
        Map<String, Object> busiData = actBusinessService.getBaseMapper().getBusiData(tableId, tableName);

        if (MapUtil.isNotEmpty(busiData)&&busiData.get(ActivitiConstant.titleKey)!=null){
            //如果表单里有 标题  更新一下
            actBusiness.setTitle(busiData.get(ActivitiConstant.titleKey)+"");
        }
        String processInstanceId = actZprocessService.startProcess(act);
        actBusiness.setProcInstId(processInstanceId);
        actBusiness.setStatus(ActivitiConstant.STATUS_DEALING);
        actBusiness.setResult(ActivitiConstant.RESULT_DEALING);
        actBusiness.setApplyTime(new Date());
        actBusinessService.updateById(actBusiness);
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"启动");
        return Result.ok("操作成功");
    }
    
    
    @RequestMapping(value="/addAndApply", method = RequestMethod.POST)
    public Result addAndApply(HttpServletRequest request) {
    	try {
    		String procDefId = request.getParameter("procDefId");
            String procDeTitle = request.getParameter("procDeTitle");
            String tableName = request.getParameter("tableName");
            String assignees = request.getParameter("assignees");
            String senders = request.getParameter("senders");
            
            ProblemSheet problemSheet = new ProblemSheet();
            
            String processType = request.getParameter("type");//流程类型
            /*Long psPrjId = jsonObj.getLong("psPrjId");
            String psLocation = jsonObj.getString("psLocation");
            String psType = jsonObj.getString("psType");
            String psLevel = jsonObj.getString("psLevel");
            String psDescription = jsonObj.getString("psDescription");
            String psAdvice = jsonObj.getString("psAdvice");
            Date  psEndTime = null; 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
            if (jsonObj.getString("psEndTime") != null ) {
            	psEndTime = sdf.parse(jsonObj.getString("psEndTime"));
            	// problemSheet.setPsEndTime(psEndTime);	
            }
            problemSheet.setPsEndTime("2020-11-03 13:23:00");	
            String psConstructionUnit = actBusinessService.getPsUnit(psPrjId, "1");
            String psSupervisingUnit = actBusinessService.getPsUnit(psPrjId, "2");
            psConstructionUnit="11111";
            psSupervisingUnit="22222";
            
            problemSheet.setPsPrjId(psPrjId);
            problemSheet.setPsLocation(psLocation);
            problemSheet.setPsType(psType);
            problemSheet.setPsLevel(psLevel);
            problemSheet.setPsDescription(psDescription);
            problemSheet.setPsAdvice(psAdvice);
          
            problemSheet.setPsConstructionUnit(Long.parseLong(psConstructionUnit));
            problemSheet.setPsSupervisingUnit(Long.parseLong(psSupervisingUnit));*/
            // 保存至我的申请业务
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            String username = sysUser.getUsername();
            //String username = request.getParameter("USERNAME");
            problemSheet.setCreateBy(username);
            
            //保存业务表单数据到数据库表
            //String tableId = IdUtil.simpleUUID();
            //actBusinessService.saveApplyForm(tableId,request);
            String tableId = actBusinessService.saveApplyForms(request, processType);
            //保存整改单图片
            if (processType.contains("problem")) {
            	
            	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        		//MultipartFile file = multipartRequest.getFile("psFiles");// 获取上传文件对象
        		List<MultipartFile> files =multipartRequest.getFiles("psFiles");
        		if (files != null && files.size() > 0) {
        			for (MultipartFile multipartFile : files) {
        				String filePath  = uploadLocal(multipartFile, "problem/fq", tableId+"_"+files.indexOf(multipartFile));
        				actBusinessService.saveApplyFormsPhoto(tableId, filePath);
					}
        		}
        		
            }
            ActBusiness actBusiness = new ActBusiness();
            actBusiness.setAssignees(assignees);
            actBusiness.setUserId(username);
            actBusiness.setTableId(tableId+"");
            actBusiness.setProcDefId(procDefId);
            String title = request.getParameter(ActivitiConstant.titleKey);
            if (StrUtil.isNotBlank(title)){
                actBusiness.setTitle(title);
            }else {
                actBusiness.setTitle(procDeTitle);
            }
            actBusiness.setTableName(tableName);
            actBusinessService.save(actBusiness);
            
            String processInstanceId = actZprocessService.startProcess(actBusiness);
            actBusiness.setProcInstId(processInstanceId);
            actBusiness.setStatus(ActivitiConstant.STATUS_DEALING);
            actBusiness.setResult(ActivitiConstant.RESULT_DEALING);
            actBusiness.setApplyTime(new Date());
            actBusinessService.updateById(actBusiness);
            //修改业务表的流程字段
            actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"启动");
            if (processType.contains("problem")) {
            	//抄送人处理
                actBusinessService.saveApplyFormsSender(tableId, processInstanceId, senders);
            }
    	} catch(Exception e) {
    		e.printStackTrace();
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
			
			fileName = tableId+"_"+ System.currentTimeMillis()+ ".jpg";
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
	
    /*撤回申请*/
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Result<Object> cancel(@RequestBody JSONObject jsonObj){  /*@RequestParam(required = false) String id,
                                 @RequestParam(required = false) String procInstId,
                                 @RequestParam(required = false) String reason*/

        String id = jsonObj.getString("id");
        String procInstId = jsonObj.getString("procInstId");
        String reason = jsonObj.getString("reason");
    	if(StrUtil.isBlank(reason)){
            reason = "";
        }
        runtimeService.deleteProcessInstance(procInstId, "canceled-"+reason);
        ActBusiness actBusiness = actBusinessService.getById(id);
        actBusiness.setStatus(ActivitiConstant.STATUS_CANCELED);
        actBusiness.setResult(ActivitiConstant.RESULT_TO_SUBMIT);
        actBusinessService.updateById(actBusiness);
        //修改业务表的流程字段
        actBusinessService.updateBusinessStatus(actBusiness.getTableName(), actBusiness.getTableId(),"撤回");
        return Result.ok("操作成功");
    }
    /**/
    @RequestMapping(value = "/listData")
    public Result listData(ActBusiness param, HttpServletRequest request){
        LambdaQueryWrapper<ActBusiness> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ActBusiness::getCreateTime);
        if (StrUtil.isNotBlank(param.getTitle())) queryWrapper.like(ActBusiness::getTitle,param.getTitle());
        if (param.getStatus()!=null) queryWrapper.eq(ActBusiness::getStatus,param.getStatus());
        if (param.getResult()!=null) queryWrapper.eq(ActBusiness::getResult,param.getResult());
        if (StrUtil.isNotBlank(request.getParameter("createTime_begin"))) queryWrapper.ge(ActBusiness::getTitle,param.getTitle());
        if (StrUtil.isNotBlank(request.getParameter("createTime_end"))) queryWrapper.le(ActBusiness::getTitle,param.getTitle());

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(ActBusiness::getUserId,loginUser.getUsername());
        List<ActBusiness> actBusinessList = actBusinessService.list(queryWrapper);

        actBusinessList.forEach(e -> {
            if(StrUtil.isNotBlank(e.getProcDefId())){
                ActZprocess actProcess = actZprocessService.getById(e.getProcDefId());
                e.setRouteName(actProcess.getRouteName());
                e.setProcessName(actProcess.getName());
            }
            if(ActivitiConstant.STATUS_DEALING.equals(e.getStatus())){
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
            }
        });
        return Result.ok(actBusinessList);
    }
}
