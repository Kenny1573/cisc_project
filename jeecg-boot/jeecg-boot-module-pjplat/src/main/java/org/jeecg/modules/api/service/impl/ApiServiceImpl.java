package org.jeecg.modules.api.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.jeecg.modules.api.mapper.ApiMapper;
import org.jeecg.modules.api.service.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.util.Value;

import cn.hutool.core.date.DateUtil;

@Service
public class ApiServiceImpl implements IApiService {

	@Autowired
	ApiMapper apimapper;

	

	/**
	 * 获取总的项目概况
	 */
	@Override
	public LinkedHashMap<String, Object> getAllProjectOverview(){
		//总的项目概况
		LinkedHashMap<String, Object> projectOverview = new LinkedHashMap<String, Object>();
		
		LinkedHashMap<String, Object> allProject = apimapper.selectProjectTotal();
		
		LinkedHashMap<String, Object> projectUnderConstructionTotal = apimapper.selectProjectUnderConstructionTotal();
		
		LinkedHashMap<String, Object> projectUnderConstructionMoney = apimapper.selectProjectUnderConstructionMoney();

		LinkedHashMap<String, Object> newCurrentMonthProjectCount = apimapper.selectNewCurrentMonthProjectCount();

		
		//项目总数
		projectOverview.put("projectSum",allProject.get("PROJECTNUM"));
		//项目总投资
		projectOverview.put("ProjectInvestment",Double.valueOf(String.valueOf(allProject.get("ESTIMATEMONEY")))/10000);
		//在建项目
		projectOverview.put("currentProject",projectUnderConstructionTotal.get("PROJECTNUM"));
		//在建项目总投资
		projectOverview.put("currentProjectInvestment",Double.valueOf(String.valueOf(projectUnderConstructionTotal.get("ESTIMATEMONEY")))/10000);
		//在建项目产值
		projectOverview.put("currentProjectOutputValue",Double.valueOf(String.valueOf(projectUnderConstructionMoney.get("FINISHMONEY")))/10000);
		//在建项目投资完成比
		projectOverview.put("currentProjectInvestmentCompletionRatio",Double.valueOf(String.valueOf(projectUnderConstructionMoney.get("FINISHMONEY")))/Double.valueOf(String.valueOf(projectUnderConstructionTotal.get("ESTIMATEMONEY")))*100+"%");
		//本月新立项
		projectOverview.put("currentMonthNewProject",newCurrentMonthProjectCount.get("NEWPROJECT"));
		//预警提醒
		projectOverview.put("warning",6);
		
		return projectOverview;
	}
	
	
	/**
	 * 获取所有项目
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getAllProject(){
	
		//项目简介
		List<LinkedHashMap<String, Object>> projectContent = new ArrayList<LinkedHashMap<String, Object>>();
		projectContent = apimapper.selectProjectContent(null);
		return projectContent;
	}
	
	
	public List<LinkedHashMap<String, Object>> getProjectTypeSpread(){
		return  apimapper.selectProjectTypeSpread();
	}
	
	/**
	 * 项目投资进度统计
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getProjectInvestmentWarningCensus(){
	
		//项目投资进度
		List<LinkedHashMap<String, Object>> projectInvestmentCensus = new ArrayList<LinkedHashMap<String, Object>>();
		projectInvestmentCensus = apimapper.selectProjectInvestmentWarningCensus();
		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		for(int i=0;i<projectInvestmentCensus.size();i++) {
			tjMap = new LinkedHashMap<String,Object>();
			tjMap.put("projectName",String.valueOf(projectInvestmentCensus.get(i).get("PRJ_NAME")));
			if(projectInvestmentCensus.get(i).get("TZS")==null) {
				tjMap.put("value",0);
			}else {
				double f =Double.valueOf(String.valueOf(projectInvestmentCensus.get(i).get("TZS")))/Double.valueOf(String.valueOf(projectInvestmentCensus.get(i).get("PRJ_ROUGH_ESTIMATE"))); 
				tjMap.put("value",Double.valueOf(String.format("%.2f", f))*100);
			}
			tjlist.add(tjMap);
		}
		return tjlist;
	}

	/**
	 * 获取项目概况
	 */
	@Override
	public LinkedHashMap<String, Object> getProjectOverview(Long projectID){
		
		//项目概况
		LinkedHashMap<String, Object> projectOverview = new LinkedHashMap<String, Object>();
		//正向排序的里程碑数据
		List<LinkedHashMap<String, Object>> milestoneForward = new ArrayList<LinkedHashMap<String, Object>>();
		//反向排序的里程碑数据
		List<LinkedHashMap<String, Object>> milestonebackward = new ArrayList<LinkedHashMap<String, Object>>();
		//项目简介
		List<LinkedHashMap<String, Object>> projectContent = new ArrayList<LinkedHashMap<String, Object>>();
		milestoneForward = apimapper.selectProjectMilestoneForward(projectID);
		milestonebackward = apimapper.selectProjectMilestonebackward(projectID);
		projectContent = apimapper.selectProjectContent(projectID);
		projectOverview.put("projectID",projectContent.get(0).get("ID"));
		//项目名称
		projectOverview.put("projectName",projectContent.get(0).get("PRJ_NAME"));
		//项目简介
		projectOverview.put("projectContent",projectContent.get(0).get("PRJ_BUILDING_CONTENT"));
		try {
			//施工天数
			long now = System.currentTimeMillis();
			long factBeginDate =Timestamp.valueOf(milestoneForward.get(0).get("SP_NODE_IN_FACT_BEGIN").toString()).getTime(); 
			long constDay = (long)(now-factBeginDate)/(1000*60*60*24);
			projectOverview.put("constructionDay",constDay);
			
			//预计工期
			long entTime = Timestamp.valueOf(milestonebackward.get(0).get("SP_NODE_END_TIME").toString()).getTime();
			long startTime = Timestamp.valueOf(milestoneForward.get(0).get("SP_NODE_BEGIN_TIME").toString()).getTime();
			long estiDay = (long)(entTime-startTime)/(1000*60*60*24);
			projectOverview.put("estimatedDuration",estiDay);
			
			//开工日期
			projectOverview.put("commencementDate",milestoneForward.get(0).get("SP_NODE_IN_FACT_BEGIN").toString().substring(0, 10));
			//计划完工
			projectOverview.put("planCompletionDate",milestonebackward.get(0).get("SP_NODE_END_TIME").toString().substring(0, 10));
			double f =(double)constDay/(double)estiDay*100; 
			projectOverview.put("dateProgress",Double.valueOf(String.format("%.2f", f)));
		} catch (Exception e) {
			projectOverview.put("constructionDay",0);
			projectOverview.put("estimatedDuration",0);
			projectOverview.put("commencementDate","-");
			projectOverview.put("planCompletionDate","-");
			projectOverview.put("dateProgress",0);
		}
		
		return projectOverview;
	}
	
	
	/**
	 *可视化2问题统计
	 */
	@Override
	public LinkedHashMap<String, Object> getProjectProblemCensus(){
		//项目
		List<String> projectList = new ArrayList<String>();
		//安全隐患
		List<String> safetyList = new ArrayList<String>();
		//质量问题
		List<String> qualityList = new ArrayList<String>();
		//文明施工问题
		List<String> houseKeepingList = new ArrayList<String>();
		//项目问题
		LinkedHashMap<String, Object> projectProblem = new LinkedHashMap<String, Object>();
		//每个类型的数组
		LinkedHashMap<String, Object> projectProblemType = new LinkedHashMap<String, Object>();
		List<LinkedHashMap<String, Object>> projectProblemTypeList = new ArrayList<LinkedHashMap<String, Object>>();
		
		//项目
		List<LinkedHashMap<String, Object>> projectProblemGroupByProject = new ArrayList<LinkedHashMap<String, Object>>();
	
		//不同项目不同类型问题统计
		List<LinkedHashMap<String, Object>> projectProblemCensusGroupByTypeAndProject = new ArrayList<LinkedHashMap<String, Object>>();
		
		
		projectProblemGroupByProject = apimapper.selectProjectContent(null);
		projectProblemCensusGroupByTypeAndProject = apimapper.selectProjectProblemCensus();
		//问题部位遍历
		for (int i=0 ;i<projectProblemGroupByProject.size();i++) {
			projectList.add(projectProblemGroupByProject.get(i).get("PRJ_NAME").toString());
			
			safetyList.add("0");
			qualityList.add("0");
			houseKeepingList.add("0");
			//问题统计遍历
			for (int j=0 ;j<projectProblemCensusGroupByTypeAndProject.size();j++) {
				if(projectProblemCensusGroupByTypeAndProject.get(j).get("PRJ_NAME").toString().equals(projectProblemGroupByProject.get(i).get("PRJ_NAME").toString())) {
					if(projectProblemCensusGroupByTypeAndProject.get(j).get("NAME").toString().equals("安全隐患")) {
						safetyList.set(i, projectProblemCensusGroupByTypeAndProject.get(j).get("TYPESUM").toString());
					}
					if(projectProblemCensusGroupByTypeAndProject.get(j).get("NAME").toString().equals("质量问题")) {
						qualityList.set(i, projectProblemCensusGroupByTypeAndProject.get(j).get("TYPESUM").toString());
					}
					if(projectProblemCensusGroupByTypeAndProject.get(j).get("NAME").toString().equals("文明施工问题")) {
						houseKeepingList.set(i, projectProblemCensusGroupByTypeAndProject.get(j).get("TYPESUM").toString());
					}
				}
			}
		}
		
		projectProblemType.put("problemType","安全隐患");
		projectProblemType.put("array",safetyList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblemType = new LinkedHashMap<String, Object>(); 
		projectProblemType.put("problemType","质量问题");
		projectProblemType.put("array",qualityList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblemType = new LinkedHashMap<String, Object>(); 
		projectProblemType.put("problemType","文明施工问题");
		projectProblemType.put("array",houseKeepingList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblem.put("problemList",projectProblemTypeList);
		projectProblem.put("projectList",projectList.toArray());
		return projectProblem;
	}
	
	
	/**
	 *可视化2问题统计
	 */
	@Override
	public LinkedHashMap<String, Object> getProjectProblemCensus2(Long projectID){
		//问题部位
		List<String> locationList = new ArrayList<String>();
		//安全隐患
		List<String> safetyList = new ArrayList<String>();
		//质量问题
		List<String> qualityList = new ArrayList<String>();
		//文明施工问题
		List<String> houseKeepingList = new ArrayList<String>();
		//项目问题
		LinkedHashMap<String, Object> projectProblem = new LinkedHashMap<String, Object>();
		//每个类型的数组
		LinkedHashMap<String, Object> projectProblemType = new LinkedHashMap<String, Object>();
		List<LinkedHashMap<String, Object>> projectProblemTypeList = new ArrayList<LinkedHashMap<String, Object>>();
		
		//发生问题的部位
		List<LinkedHashMap<String, Object>> projectProblemGroupByLocation = new ArrayList<LinkedHashMap<String, Object>>();
		//不同类型的问题统计
		List<LinkedHashMap<String, Object>> projectProblemCensusGroupByType = new ArrayList<LinkedHashMap<String, Object>>();
		//不同位置不同类型问题统计
		List<LinkedHashMap<String, Object>> projectProblemCensusGroupByTypeAndLocation = new ArrayList<LinkedHashMap<String, Object>>();
		
		
		projectProblemGroupByLocation = apimapper.selectProjectProblemCensusGroupByLocation(projectID);
		projectProblemCensusGroupByType = apimapper.selectProjectProblemCensusGroupByType(projectID);
		projectProblemCensusGroupByTypeAndLocation = apimapper.selectProjectProblemCensusGroupByTypeAndLocation(projectID);
		//问题部位遍历
		for (int i=0 ;i<projectProblemGroupByLocation.size();i++) {
			locationList.add(projectProblemGroupByLocation.get(i).get("PS_LOCATION").toString());
			
			safetyList.add("0");
			qualityList.add("0");
			houseKeepingList.add("0");
			//问题统计遍历
			for (int j=0 ;j<projectProblemCensusGroupByTypeAndLocation.size();j++) {
				if(projectProblemCensusGroupByTypeAndLocation.get(j).get("PS_LOCATION").toString().equals(projectProblemGroupByLocation.get(i).get("PS_LOCATION").toString())) {
					if(projectProblemCensusGroupByTypeAndLocation.get(j).get("NAME").toString().equals("安全隐患")) {
						safetyList.set(i, projectProblemCensusGroupByTypeAndLocation.get(j).get("TYPESUM").toString());
					}
					if(projectProblemCensusGroupByTypeAndLocation.get(j).get("NAME").toString().equals("质量问题")) {
						qualityList.set(i, projectProblemCensusGroupByTypeAndLocation.get(j).get("TYPESUM").toString());
					}
					if(projectProblemCensusGroupByTypeAndLocation.get(j).get("NAME").toString().equals("文明施工问题")) {
						houseKeepingList.set(i, projectProblemCensusGroupByTypeAndLocation.get(j).get("TYPESUM").toString());
					}
				}
			}
		}
		
		projectProblemType.put("problemType","安全隐患");
		projectProblemType.put("array",safetyList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblemType = new LinkedHashMap<String, Object>(); 
		projectProblemType.put("problemType","质量问题");
		projectProblemType.put("array",qualityList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblemType = new LinkedHashMap<String, Object>(); 
		projectProblemType.put("problemType","文明施工问题");
		projectProblemType.put("array",houseKeepingList);
		projectProblemTypeList.add(projectProblemType);
		
		projectProblem.put("problemList",projectProblemTypeList);
		projectProblem.put("locationList",locationList.toArray());
		projectProblem.put("totalProblem",projectProblemCensusGroupByType);
		return projectProblem;
	}
	
	/**
	 * 可视化2项目里程碑排序
	 */
	@Override
	public List<LinkedHashMap<String, Object>> getProjectMilestoneByProject(Long projectID){
		return apimapper.selectProjectMilestoneByProject(projectID);
	}
	
	
	/**
	 * 可视化2项目投资按季度分
	 */
	@Override
	public LinkedHashMap<String, Object> getProjectInvestment(Long projectID,String year){
		LinkedHashMap<String, Object> project= new LinkedHashMap<String, Object>();
		//项目投资年度实际与计划列表
		List<LinkedHashMap<String, Object>> projectInvestmentList = new ArrayList<LinkedHashMap<String, Object>>();
		projectInvestmentList = apimapper.selectProjectInvestment(projectID, year);
		//计划投资
		double planInvestment = 0L;
		//实际投资
		double actualInvestment = 0L;
		Double[] firstQuarter = {0.0,0.0,0.0,0.0};
		Double[] secondQuarter = {0.0,0.0,0.0,0.0};
		Double[] thirdQuarter = {0.0,0.0,0.0,0.0};
		Double[] fourthQuarter = {0.0,0.0,0.0,0.0};
		long month = 0L;
		double plan =0;
		double actual =0;
		for(int i=0 ; i<projectInvestmentList.size(); i++) {
			if(projectInvestmentList.get(i).get("YPD_INVESTMENT")==null) {
				plan=0;
			}else {
				plan=Double.valueOf(String.valueOf(projectInvestmentList.get(i).get("YPD_INVESTMENT")));
			}
			if(projectInvestmentList.get(i).get("MR_FINISH_INVESTMENT")==null) {
				actual =0;
			}else {
				actual =Double.valueOf(String.valueOf(projectInvestmentList.get(i).get("MR_FINISH_INVESTMENT")));
			}
			planInvestment += plan;
			actualInvestment += actual;
			month = Long.valueOf(String.valueOf(projectInvestmentList.get(i).get("YPD_MONTH")));
			//System.out.println(month);
			if(month<4) {
				firstQuarter[0] += plan;
				firstQuarter[1] += actual;
			}
			if(month>3&&month<7) {
				secondQuarter[0] += plan;
				secondQuarter[1] += actual;
			}
			if(month>6&&month<10) {
				thirdQuarter[0] += plan;
				thirdQuarter[1] += actual;
			}
			if(month>9&&month<13) {
				fourthQuarter[0] += plan;
				fourthQuarter[1] += actual;
			}
		}
		firstQuarter[2] = firstQuarter[0];
	    firstQuarter[3] = firstQuarter[1];
	    secondQuarter[2] = firstQuarter[2]+secondQuarter[0];
		secondQuarter[3] = firstQuarter[3]+secondQuarter[1];
		thirdQuarter[2] = secondQuarter[2]+thirdQuarter[0];
		thirdQuarter[3] = secondQuarter[3]+thirdQuarter[1];
		fourthQuarter[2] = thirdQuarter[2]+fourthQuarter[0];
		fourthQuarter[3] = thirdQuarter[3]+fourthQuarter[1];
		
		project.put("planInvestment",planInvestment/10000);
		//实际投资
		project.put("actualInvestment",actualInvestment/10000);
		//投资进度
		double f =actualInvestment/planInvestment*100; 
//		DecimalFormat df = new DecimalFormat("#0.00");
		project.put("investmentProgress",Double.valueOf(String.format("%.2f", f)));
		
		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> tzMap = new LinkedHashMap<String,Object>();
		tzMap.put("QuarterName", year+"第一季度");
		//计划投资
		tzMap.put("planInvestment",firstQuarter[0]/10000);
		//实际投资
		tzMap.put("actualInvestment",firstQuarter[1]/10000);
		tzMap.put("planTotal",firstQuarter[2]/10000);
		tzMap.put("actualTotal",firstQuarter[3]/10000);
		tjlist.add(tzMap);
		
		tzMap = new LinkedHashMap<String,Object>();
		tzMap.put("QuarterName", year+"第二季度");
		//计划投资
		tzMap.put("planInvestment",secondQuarter[0]/10000);
		//实际投资
		tzMap.put("actualInvestment",secondQuarter[1]/10000);
		tzMap.put("planTotal",secondQuarter[2]/10000);
		tzMap.put("actualTotal",secondQuarter[3]/10000);
		tjlist.add(tzMap);
		
		tzMap = new LinkedHashMap<String,Object>();
		tzMap.put("QuarterName", year+"第三季度");
		//计划投资
		tzMap.put("planInvestment",thirdQuarter[0]/10000);
		//实际投资
		tzMap.put("actualInvestment",thirdQuarter[1]/10000);
		tzMap.put("planTotal",thirdQuarter[2]/10000);
		tzMap.put("actualTotal",thirdQuarter[3]/10000);
		tjlist.add(tzMap);
		
		tzMap = new LinkedHashMap<String,Object>();
		tzMap.put("QuarterName", year+"第四季度");
		tzMap.put("planInvestment",fourthQuarter[0]/10000);
		//实际投资
		tzMap.put("actualInvestment",fourthQuarter[1]/10000);
		tzMap.put("planTotal",fourthQuarter[2]/10000);
		tzMap.put("actualTotal",fourthQuarter[3]/10000);
		tjlist.add(tzMap);
		//投资分析
		project.put("investmentAnalysis",tjlist);
		return project;
	}

	
//	/**
//	 * 实时天气
//	 */
//	@Override
//	public LinkedHashMap<String, Object> getWeather(String areaNum, String cityName) {
//		LinkedHashMap<String, Object> weather = apimapper.getRealTimeWeather(areaNum, cityName);
//		return weather;
//	}
	
	public List<LinkedHashMap<String, Object>> getHelmetViewInfo(@Param("prj_id") String prj_id){
		return  apimapper.selectHelmetViewInfo(prj_id);
	}
	
	public List<LinkedHashMap<String, Object>> getVideoViewInfo(@Param("prj_id") String prj_id){
		return  apimapper.selectVideoViewInfo(prj_id);
	}
	
	
}
