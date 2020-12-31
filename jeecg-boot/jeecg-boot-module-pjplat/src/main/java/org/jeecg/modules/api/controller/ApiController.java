package org.jeecg.modules.api.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.api.service.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.dahua.demo.basic.video.RealMonitor;
/**
 * @Description: 接口
 * @Author: lei.yin
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Api(tags = "可视化接口")
@RestController
@RequestMapping("/interface/api")
public class ApiController {

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;
	
	@Autowired
	private IApiService iapiservice;
	
	
//	/**
//	 * 
//	 * 可视化设备接入统计接口
//	 * @author lei.yin
//	 * @date 2020年10月09日 上午8:47:16
//	 * @return
//	 */
//	@AutoLog(value = "车辆事件接口")
//	@ApiOperation(value = "车辆事件接口", notes = "车辆事件接口")
//	@PostMapping(value = "/getVehicleIncident")
//	public Result<?> vehicleIncident(@RequestParam(name = "startTime", required = false) String startTime,@RequestParam(name = "endTime", required = false) String endTime) {
//		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
//		//查询全部事件
//		List<LinkedHashMap<String,Object>> sjList = iapiservice.getVehicleIncident("", startTime, endTime);
//		//查询全部报警事件
//		List<LinkedHashMap<String,Object>> ljbjList = iapiservice.getVehicleIncident("0", "", "");
//		//查询报警事件
//		List<LinkedHashMap<String,Object>> bjList = iapiservice.getVehicleIncident("0", startTime, endTime);
//		//查询预警事件
//		List<LinkedHashMap<String,Object>> yjList = iapiservice.getVehicleIncident("1", startTime, endTime);
//		//查询通知事件
//		List<LinkedHashMap<String,Object>> tzList = iapiservice.getVehicleIncident("2", startTime, endTime);
//		//事件Map
//		LinkedHashMap<String,Object> sjMap = new LinkedHashMap<String,Object>();
//		sjMap.put("sjList",sjList);
//		sjMap.put("ljbjCount",ljbjList.size());
//		sjMap.put("bjCount",bjList.size());
//		sjMap.put("yjCount",yjList.size());
//		sjMap.put("tzCount",tzList.size());
//		res.setTimestamp(System.currentTimeMillis());
//		res.setCode(200);
//		res.setMessage("Success");
//		res.setResult(sjMap);
//		return res;
//	}
	
	
	/**
	 * 
	 * 可视化项目统计接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午8:47:16
	 * @return
	 */
	@AutoLog(value = "可视化项目统计接口")
	@ApiOperation(value = "可视化项目统计接口", notes = "可视化项目统计接口")
	@PostMapping(value = "/getProjectCensus")
	public Result<?> projectCensus() {
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//项目总数
		/*tjMap.put("projectSum",20);
		//项目总投资
		tjMap.put("ProjectInvestment",555.5);
		//在建项目
		tjMap.put("currentProject",13);
		//在建项目总投资
		tjMap.put("currentProjectInvestment",22.2);
		//在建项目产值
		tjMap.put("currentProjectOutputValue",11.1);
		//在建项目投资完成比
		tjMap.put("currentProjectInvestmentCompletionRatio","55%");
		//本月新立项
		tjMap.put("currentMonthNewProject",1);
		//预警提醒
		tjMap.put("warning",6);*/
		tjMap = iapiservice.getAllProjectOverview();
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	/**
	 * 
	 * 可视化项目类型分布统计接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午8:47:16
	 * @return
	 */
	@AutoLog(value = "可视化项目类型分布统计接口")
	@ApiOperation(value = "可视化项目类型分布统计接口", notes = "可视化项目类型分布统计接口")
	@PostMapping(value = "/getProjectTypeCensus")
	public Result<?> projectTypeCensus() {
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		
		//统计Map
		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		/*LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//项目类型
		tjMap.put("type","市政");
		tjMap.put("value",2);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("type","交通");
		tjMap.put("value",1);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("type","景观绿化");
		tjMap.put("value",0);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("type","房建");
		tjMap.put("value",0);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("type","其他");
		tjMap.put("value",0);
		tjlist.add(tjMap);*/
		tjlist = iapiservice.getProjectTypeSpread();
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjlist);
		return res;
	}
	
	
	/**
	 * 
	 * 可视化项目问题统计接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化项目问题统计接口")
	@ApiOperation(value = "可视化项目问题统计接口", notes = "可视化项目问题统计接口")
	@PostMapping(value = "/getProjectProblemCensus")
	public Result<?> projectProblemCensus() {
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		
		//统计Map
		//List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		tjMap= iapiservice.getProjectProblemCensus();
		//项目总数
		/*tjMap.put("projectName","无锡市滨湖区蠡湖大道xx项目");
		tjMap.put("problemType","安全隐患");
		tjMap.put("value",1);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("projectName","无锡市滨湖区蠡湖大道xx项目");
		tjMap.put("problemType","质量问题");
		tjMap.put("value",1);
		tjlist.add(tjMap);*/
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	/**
	 * 
	 * 可视化项目投资统计预警接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化项目投资进度预警接口")
	@ApiOperation(value = "可视化项目投资进度预警接口", notes = "可视化项目投资进度预警接口")
	@PostMapping(value = "/getProjectInvestmentWarningCensus")
	public Result<?> projectInvestmentWarningCensus() {
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		
		//统计Map
		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		/*LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//项目名称和进度
		tjMap.put("projectName","无锡市滨湖区蠡湖大道xx项目");
		tjMap.put("value",20);
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("projectName","无锡市滨湖区贡湖大道xx项目");
		tjMap.put("value",60);
		tjlist.add(tjMap);*/
		tjlist = iapiservice.getProjectInvestmentWarningCensus();
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjlist);
		return res;
	}
		
	
	
	/**
	 * 
	 * 可视化单个项目简介和概况接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午8:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2所有项目查询接口")
	@ApiOperation(value = "可视化2所有项目查询接口", notes = "可视化2所有项目查询接口")
	@PostMapping(value = "/getAllProject")
	public Result<?> allProject() {
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		//统计Map
		List<LinkedHashMap<String,Object>> tjList = new ArrayList<LinkedHashMap<String,Object>>();
		tjList = iapiservice.getAllProject();

		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjList);
		return res;
	}
	
	
	
	
	/**
	 * 
	 * 可视化单个项目简介和概况接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午8:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2单个项目简介和概况接口")
	@ApiOperation(value = "可视化2单个项目简介和概况接口", notes = "可视化2单个项目简介和概况接口")
	@PostMapping(value = "/getProjectIntroduction")
	public Result<?> projectIntroduction(@RequestParam(name = "id", required = false) Long id) {
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		tjMap = iapiservice.getProjectOverview(id);
//		//项目项目id
//		tjMap.put("projectID",1);
//		//项目名称
//		tjMap.put("projectName","无锡市滨湖区蠡湖大道xx项目");
//		//项目简介
//		tjMap.put("projectContent","测试测试测试测试测试测试");
//		//施工天数
//		tjMap.put("constructionDay",100);
//		//预计工期
//		tjMap.put("estimatedDuration",500);
//		//开工日期
//		tjMap.put("commencementDate","2020-11-10");
//		//计划完工
//		tjMap.put("planCompletionDate","2022-12-12");
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	/**
	 * 
	 * 可视化项目投资接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午8:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2项目投资接口")
	@ApiOperation(value = "可视化2项目投资接口", notes = "可视化2项目投资接口")
	@PostMapping(value = "/getProjectInvestmentCensus")
	public Result<?> projectInvestmentCensus(@RequestParam(name = "id", required = false) Long id,@RequestParam(name = "year", required = false) String year) {
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		tjMap = iapiservice.getProjectInvestment(id, year);
//		//计划投资
//		tjMap.put("planInvestment",58);
//		//实际投资
//		tjMap.put("actualInvestment",22);
//		//投资进度
//		tjMap.put("investmentProgress","47%");
//		
//		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
//		LinkedHashMap<String,Object> tzMap = new LinkedHashMap<String,Object>();
//		tzMap.put("QuarterName", "2020第一季度");
//		//计划投资
//		tzMap.put("planInvestment",58);
//		//实际投资
//		tzMap.put("actualInvestment",22);
//		tjlist.add(tzMap);
//		
//		tzMap = new LinkedHashMap<String,Object>();
//		tzMap.put("QuarterName", "2020第二季度");
//		//计划投资
//		tzMap.put("planInvestment",22);
//		//实际投资
//		tzMap.put("actualInvestment",11);
//		tjlist.add(tzMap);
//		
//		tzMap = new LinkedHashMap<String,Object>();
//		tzMap.put("QuarterName", "2020第三季度");
//		//计划投资
//		tzMap.put("planInvestment",32);
//		//实际投资
//		tzMap.put("actualInvestment",22);
//		tjlist.add(tzMap);
//		
//		tzMap = new LinkedHashMap<String,Object>();
//		tzMap.put("QuarterName", "2020第四季度");
//		//计划投资
//		tzMap.put("planInvestment",11);
//		//实际投资
//		tzMap.put("actualInvestment",20);
//		tjlist.add(tzMap);
//		//投资分析
//		tjMap.put("investmentAnalysis",tjlist);
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	
	/**
	 * 
	 * 可视化项目问题统计接口2
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2项目问题统计接口2")
	@ApiOperation(value = "可视化2项目问题统计接口2", notes = "可视化2项目问题统计接口2")
	@PostMapping(value = "/getProjectProblemCensus2")
	public Result<?> projectProblemCensus2(@RequestParam(name = "id", required = false) Long id) {
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		
		//问题map
		LinkedHashMap<String,Object> wtMap = new LinkedHashMap<String,Object>();
		wtMap = iapiservice.getProjectProblemCensus2(id);
		//统计总的问题Map
		/*List<LinkedHashMap<String,Object>> prjProblemTotalList = new ArrayList<LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> prjProblemTotalMap = new LinkedHashMap<String,Object>();
		//统计分项目问题Map
		List<LinkedHashMap<String,Object>> prjList = new ArrayList<LinkedHashMap<String,Object>>();
		LinkedHashMap<String,Object> prjMap = new LinkedHashMap<String,Object>();
		
		//总问题统计饼状图
		prjProblemTotalMap.put("problemType","安全隐患");
		prjProblemTotalMap.put("value",1);
		prjProblemTotalList.add(prjProblemTotalMap);
		
		prjProblemTotalMap = new LinkedHashMap<String,Object>();
		prjProblemTotalMap.put("problemType","质量问题");
		prjProblemTotalMap.put("value",1);
		prjProblemTotalList.add(prjProblemTotalMap);
		
		prjProblemTotalMap = new LinkedHashMap<String,Object>();
		prjProblemTotalMap.put("problemType","文明施工问题");
		prjProblemTotalMap.put("value",0);
		prjProblemTotalList.add(prjProblemTotalMap);
		
		//分项目统计柱状图
		prjMap.put("problemLocation","xx桥体");
		prjMap.put("problemType","安全隐患");
		prjMap.put("value",1);
		prjList.add(prjMap);
		
		prjMap = new LinkedHashMap<String,Object>();
		prjMap.put("problemLocation","xx桥墩");
		prjMap.put("problemType","质量问题");
		prjMap.put("value",1);
		prjList.add(prjMap);
		
		wtMap.put("totalProblem",prjProblemTotalList);
		wtMap.put("prjectProblem",prjList);*/
		
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(wtMap);
		return res;
	}
	
	
	/**
	 * 
	 * 预警信息接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2预警信息接口")
	@ApiOperation(value = "可视化2预警信息接口", notes = "可视化2预警信息接口")
	@PostMapping(value = "/getProjectWarning")
	public Result<?> projectWarning(@RequestParam(name = "id", required = false) Long id) {
		
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//预警信息
		//质量安全
		tjMap.put("QualityAndSafety","存在1个安全质量问题未及时处理");
		//进度管理
		tjMap.put("projectProgress","项目里程碑节点已延期请及时处理");
		
		//投资管理
		tjMap.put("projectInvestment","2020年度投资完成率10%，请及时关注");
		//需协调问题
		tjMap.put("coordinationIssues","需尽快明确管廊实施方案并启动建设");
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	/**
	 * 
	 * 监控信息接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2监控信息接口")
	@ApiOperation(value = "可视化2监控信息接口", notes = "可视化2监控信息接口")
	@PostMapping(value = "/getProjectMonitor")
	public Result<?> projectMonitor(@RequestParam(name = "id", required = false) Long id) {
		
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//点位数
		tjMap.put("pointCount",29);
		//运行数
		tjMap.put("runningCount",28);
		//监控时长
		tjMap.put("monitorTime",1209.5);

		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
	/**
	 * 
	 * 项目里程碑接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2项目里程碑接口")
	@ApiOperation(value = "可视化2项目里程碑接口", notes = "可视化2项目里程碑接口")
	@PostMapping(value = "/getProjectMilestone")
	public Result<?> projectMilestone(@RequestParam(name = "id", required = false) Long id) {
		
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		
		//统计Map
		List<LinkedHashMap<String,Object>> tjlist = new ArrayList<LinkedHashMap<String,Object>>();
		tjlist = iapiservice.getProjectMilestoneByProject(id);
		/*LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("sequence",1);
		tjMap.put("date","2020-11-11");
		tjMap.put("nodeName","桥面板施工完毕，开始防护工程施工");
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("sequence",2);
		tjMap.put("date","2020-10-11");
		tjMap.put("nodeName","钢结构施工完毕,开始铺设桥面板");
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("sequence",3);
		tjMap.put("date","2020-02-11");
		tjMap.put("nodeName","墩柱施工完毕，开始钢结构施工");
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("sequence",4);
		tjMap.put("date","2019-02-11");
		tjMap.put("nodeName","桩基础施工完毕");
		tjlist.add(tjMap);
		
		tjMap = new LinkedHashMap<String,Object>();
		tjMap.put("sequence",5);
		tjMap.put("date","2018-12-11");
		tjMap.put("nodeName","项目启动");
		tjlist.add(tjMap);*/
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjlist);
		return res;
	}
	
	/**
	 * 
	 * 可视化2安全帽接口
	 * @author lei.yin
	 * @date 2020年11月11日 上午9:47:16
	 * @return
	 */
	@AutoLog(value = "可视化2安全帽接口")
	@ApiOperation(value = "可视化2安全帽接口", notes = "可视化2安全帽接口")
	@PostMapping(value = "/getSafetyHelmentCensus")
	public Result<?> safetyHelmentCensus(@RequestParam(name = "id", required = false) Long id) {
		
		Result<LinkedHashMap<String,Object>> res = new Result<LinkedHashMap<String,Object>>();
		//统计Map
		LinkedHashMap<String,Object> tjMap = new LinkedHashMap<String,Object>();
		//在线数
		tjMap.put("onlineCount",1150);
		//异常数
		tjMap.put("waringCount",28);
		//在线率
		tjMap.put("onlineRate","99%");

		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(tjMap);
		return res;
	}
	
	
/*	@AutoLog(value = "可视化视频播放")
	@ApiOperation(value = "可视化视频播放", notes = "可视化视频播放")
	@RequestMapping(value = "/getVideoSource", method = RequestMethod.POST)
	public Result<?> getVideoSource(@RequestParam(name="channelId", required=false) String channelId) throws Exception {
		String url = RealMonitor.getRealMonitorSource(channelId, "HLS", 0);
		
		JSONObject obj = new JSONObject();
		obj.put("hlsUrl", url);
		return Result.ok(obj);
	}*/
	
	/**
	 * 
	 * 安全帽详细信息接口
	 * @author guodong.zhou
	 * @date 2020年12月08日
	 * @return
	 */
	@AutoLog(value = "安全帽详细信息接口")
	@ApiOperation(value = "安全帽详细信息接口", notes = "安全帽详细信息接口")
	@RequestMapping(value = "/getHelmetDetail", method = RequestMethod.POST)
	public Result<?> getHelmetDetail(@RequestParam(name="prj_id", required=false) String prj_id) {
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		
		List<LinkedHashMap<String,Object>> helmetlist = new ArrayList<LinkedHashMap<String,Object>>();
		
		helmetlist = iapiservice.getHelmetViewInfo(prj_id);
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(helmetlist);
		return res;
	}
	
	/**
	 * 
	 * 监控详细信息接口
	 * @author guodong.zhou
	 * @date 2020年12月08日
	 * @return
	 */
	@AutoLog(value = "监控详细信息接口")
	@ApiOperation(value = "监控详细信息接口", notes = "监控详细信息接口")
	@RequestMapping(value = "/getVideoDetail", method = RequestMethod.POST)
	public Result<?> getVideoDetail(@RequestParam(name="prj_id", required=false) String prj_id) {
		Result<List<LinkedHashMap<String,Object>>> res = new Result<List<LinkedHashMap<String,Object>>>();
		
		List<LinkedHashMap<String,Object>> vdometlist = new ArrayList<LinkedHashMap<String,Object>>();
		
		vdometlist = iapiservice.getVideoViewInfo(prj_id);
		
		res.setTimestamp(System.currentTimeMillis());
		res.setCode(200);
		res.setMessage("Success");
		res.setResult(vdometlist);
		return res;
	}
}
