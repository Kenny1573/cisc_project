package org.jeecg.modules.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.projectcompany.entity.ProjectCompany;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.projectcompany.mapper.ProjectCompanyMapper;
import org.jeecg.modules.project.mapper.YearPlanMapper;
import org.jeecg.modules.project.mapper.ProjectMapper;
import org.jeecg.modules.projectcompany.service.IProjectCompanyService;
import org.jeecg.modules.project.service.IProjectService;
import org.jeecg.modules.project.vo.ProjectPage;
import org.jeecg.modules.projectleader.mapper.ProjectLeaderMapper;
import org.jeecg.modules.projectleader.service.IProjectLeaderService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@DS("pjplat")
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private YearPlanMapper yearPlanMapper;

	@Autowired
	private ProjectCompanyMapper projectCompanyMapper;
	@Autowired
	private IProjectCompanyService projectCompanyService;
	@Autowired
	ProjectLeaderMapper projectLeaderMapper;
	@Autowired
	IProjectLeaderService projectLeaderService;

	@Override
	public List<Project> getProjectListByUsername(String username, Page<Project> page, String prjName, String prjType) {
		String ids = projectMapper.getProjectListByUsername(username);
		List<Project> projects = new ArrayList<>();

		if (ids!=null&&!ids.equals("")) {
			List<String> idsList = Arrays.asList(ids.split(","));
			int curIndex = page.getCurrent()>1? (int) ((page.getCurrent() - 1) * page.getSize()) :0;
			for (int i=0;i<page.getSize()&&i<idsList.size()-curIndex;i++){
				if (prjName!=null)
					prjName=prjName.replace("*","%");
				Project project = projectMapper.getProject(idsList.get((int) (curIndex+i)),prjName,prjType);
				if (project!=null)
					projects.add(project);
			}

		}


		return projects;
	}

	@Override
	public List<Project> getProjectListByUsername2(String username) {
		String ids = projectMapper.getProjectListByUsername(username);
		List<Project> projects = new ArrayList<>();

		if (ids!=null&&!ids.equals("")) {
			List<String> idsList = Arrays.asList(ids.split(","));
			for (int i=0;i<idsList.size();i++){
				Project project = projectMapper.getProject(idsList.get(i),null,null);
				if (project!=null)
					projects.add(project);
			}

		}


		return projects;
	}

	//根据项目id获取该项目的负责单位的信息
	@Override
	public List<Company> getCompanyInfoById(Long mainId,String type, Integer pageNo, Integer pageSize) {
		Integer start = (pageNo-1)*pageSize +1;
		Integer end = start+pageSize-1;
		return projectMapper.getCompanyInfoById(mainId,type,start,end);
	}
	//根据项目id删除该项目的负责单位的信息
	@Override
	public boolean deleteCompanyInfoById(Long cId,Long prjId) {
		return projectMapper.deleteCompanyInfoById(cId,prjId);
	}

	@Override
	@Transactional
	public void delMain(Long id) {

		projectMapper.delById(id);
	}



	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			yearPlanMapper.deleteByMainId(Long.parseLong((String) id) );
			projectMapper.deleteById(id);
		}
	}


	//为项目添加负责单位
	@Override
	@Transactional
	public void addProjectCompanyBatch(JSONObject jsonObject) {
		//勾选中的公司的id
		String ids = jsonObject.getString("ids");

		String mainId = jsonObject.getString("mainId");
		//公司类型1：施工单位2：监管单位
		String type =jsonObject.getString("type");
		List<String> asList=Arrays.asList(ids.split(","));
		QueryWrapper<ProjectCompany> query = new QueryWrapper<ProjectCompany>().eq("PRJ_ID",mainId).eq("COM_TYPE", type);
		List<ProjectCompany> oldList =projectCompanyService.list(query);

		//去除原本列表中有的而在模态框内没有勾选中的公司记录
		oldList.stream().filter(x-> !asList.contains(x.getComId()))
				.forEach(item->{
			projectCompanyService.remove(new QueryWrapper<ProjectCompany>().eq("PRJ_ID",mainId).and(objectQueryWrapper -> objectQueryWrapper.eq("COM_ID",item.getComId())));
		});
		if(!ids.equals("")) {
			for (String id : asList) {
				ProjectCompany projectCompany = new ProjectCompany(null, type, mainId, id);
				//判断在勾选中的公司，数据库中是否就已经存有数据，有则跳过，无则添加
				QueryWrapper<ProjectCompany> queryWrapper = new QueryWrapper<ProjectCompany>().eq("PRJ_ID", mainId).and(objectQueryWrapper -> objectQueryWrapper.eq("COM_ID", id)).eq("COM_TYPE", type);
				List<ProjectCompany> pageList = projectCompanyService.list(queryWrapper);
				if (pageList.size() > 0)
					continue;
				else
					projectCompanyMapper.insert(projectCompany);

			}
		}
	}

	@Override
	public void createProject(JSONObject jsonObject) {
		Map map=setMapstr();
		Project project=JSONObject.parseObject(JSON.toJSONString(jsonObject.get("project")),Project.class);

/*
		BigDecimal prjRoughEstimate0=jsonObject.getJSONObject("project").getBigDecimal("prjRoughEstimate0");
		BigDecimal prjRoughEstimate1=jsonObject.getJSONObject("project").getBigDecimal("prjRoughEstimate1");
		BigDecimal prjRoughEstimate2=jsonObject.getJSONObject("project").getBigDecimal("prjRoughEstimate2");

		BigDecimal prjEstimate0=jsonObject.getJSONObject("project").getBigDecimal("prjEstimate0");
		BigDecimal prjEstimate1=jsonObject.getJSONObject("project").getBigDecimal("prjEstimate1");
		BigDecimal prjEstimate2=jsonObject.getJSONObject("project").getBigDecimal("prjEstimate2");

		BigDecimal prjBudgetEstimate0=jsonObject.getJSONObject("project").getBigDecimal("prjBudgetEstimate0");
		BigDecimal prjBudgetEstimate1=jsonObject.getJSONObject("project").getBigDecimal("prjBudgetEstimate1");
		BigDecimal prjBudgetEstimate2=jsonObject.getJSONObject("project").getBigDecimal("prjBudgetEstimate2");

		BigDecimal prjFinalEstimate0=jsonObject.getJSONObject("project").getBigDecimal("prjFinalEstimate0");
		BigDecimal prjFinalEstimate1=jsonObject.getJSONObject("project").getBigDecimal("prjFinalEstimate1");
		BigDecimal prjFinalEstimate2=jsonObject.getJSONObject("project").getBigDecimal("prjFinalEstimate2");

		project.setPrjRoughEstimate(getMoney(prjRoughEstimate0,prjRoughEstimate1,prjRoughEstimate2));
		project.setPrjEstimate(getMoney(prjEstimate0,prjEstimate1,prjEstimate2));
		project.setPrjBudgetEstimate(getMoney(prjBudgetEstimate0,prjBudgetEstimate1,prjBudgetEstimate2));
		project.setPrjFinalEstimate(getMoney(prjFinalEstimate0,prjFinalEstimate1,prjFinalEstimate2));
*/

		projectMapper.insert(project);
		JSONObject leaders =  jsonObject.getJSONObject("leaders");
		leaders.put("prjId",project.getId());
		projectLeaderService.addProjectLeader(leaders);
		List<Company> companyList1= JSON.parseArray(JSON.toJSONString(jsonObject.get("companies1")),Company.class);
		if(companyList1!=null) {
			companyList1.forEach(company -> {
				projectCompanyService.save(new ProjectCompany(project.getId().toString(), "1", company.getId().toString()));
			});
		}

			List<Company> companyList2 = JSON.parseArray(JSON.toJSONString(jsonObject.get("companies2")), Company.class);
		if(companyList2!=null) {
			companyList2.forEach(company -> {
				projectCompanyService.save(new ProjectCompany(project.getId().toString(), "1", company.getId().toString()));
			});
		}
		List<YearPlan> yearPlanList=JSONObject.parseArray(JSON.toJSONString(jsonObject.get("yearPlans")),YearPlan.class);
		yearPlanList.forEach(yearPlan -> {
			yearPlan.setYpPrjId(project.getId());
			yearPlanMapper.insert(yearPlan);
		});
	}

	public BigDecimal getMoney(BigDecimal b1,BigDecimal b2,BigDecimal b3){
		BigDecimal zero = new BigDecimal("0");
		if(b1==null)
			b1=zero;
		if (b2==null)
			b2=zero;
		if(b3==null)
			b3=zero;//一亿
		BigDecimal hundredMillion = new BigDecimal("100000000");
		//一万
		BigDecimal  tenThousand = new BigDecimal("10000");
		b1 = b1.multiply(hundredMillion);
		b2=b2.multiply(tenThousand);
		return b1.add(b2).add(b3).divide(tenThousand);



	}

	@Override
	public void deleteBatchProjectCompany(JSONObject jsonObject) {
		String ids=jsonObject.getString("ids");
		Long prjId= Long.parseLong(jsonObject.getString("prjId"));
		List<String> list= Arrays.asList(ids.split(","));
		list.forEach((id)->{
			projectMapper.deleteCompanyInfoById(Long.parseLong(id),prjId);
		});

	}

	@Override
	public List<KeyValue<String,String>> listProject() {
		return projectMapper.listProject();
	}

	@Override
	public ModelAndView exportXls(HttpServletRequest request, Project object, Class<ProjectPage> clazz, String title,List<Project> pageList) {
		// Step.1 组装查询条件

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();


		List<ProjectPage> exportList = new ArrayList<>();

		// 过滤选中数据
		String selections = request.getParameter("selections");
		if (oConvertUtils.isNotEmpty(selections)) {
			List<String> selectionList = Arrays.asList(selections.split(","));
			pageList.stream().filter(item -> selectionList.contains(item.getId().toString())).forEach(record->exportList.add(getProjectPage(record)));
		} else {
			pageList.stream().forEach(record->exportList.add(getProjectPage(record)));

		}

		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		if (sysUser!=null)
		    mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + sysUser.getRealname()+"    金额单位：万元", title));
		else
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "金额单位：万元", title));
//        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "", title));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}

	public ProjectPage getProjectPage(Project record){
		ProjectPage projectPage = new ProjectPage();
		projectPage.setId(record.getId());
		projectPage.setPrjName(record.getPrjName());
		projectPage.setPrjNumber(record.getPrjNumber());
		projectPage.setPrjType(record.getPrjType());
		projectPage.setPrjStage(record.getPrjStage());
		projectPage.setPrjRoughEstimate(record.getPrjRoughEstimate());
		projectPage.setPrjEstimate(record.getPrjEstimate());
		projectPage.setPrjBudgetEstimate(record.getPrjBudgetEstimate());
		projectPage.setPrjFinalEstimate(record.getPrjFinalEstimate());
		projectPage.setPrjBuildingSize(record.getPrjBuildingSize());
		projectPage.setPrjBuildingContent(record.getPrjBuildingContent());
		projectPage.setPrjBuildingAddress(record.getPrjBuildingAddress());
		projectPage.setPrjLatitude(record.getPrjLatitude());
		projectPage.setPrjLongitude(record.getPrjLongitude());
		projectPage.setPrjParams(record.getPrjParams());
		JSONObject jsonObject=projectLeaderService.getProjectRealNameById(record.getId().toString());
		projectPage.setPreOwner(getArray(jsonObject.getString("preOwner")));
		projectPage.setMidOwner(getArray(jsonObject.getString("midOwner")));
		projectPage.setOffice(getArray(jsonObject.getString("office")));
		projectPage.setPlan(getArray(jsonObject.getString("plan")));
		projectPage.setBridge(getArray(jsonObject.getString("bridge")));
		projectPage.setBuild(getArray(jsonObject.getString("build")));
		projectPage.setQuality(getArray(jsonObject.getString("quality")));
		projectPage.setFinance(getArray(jsonObject.getString("finance")));
		projectPage.setOffice(getArray(jsonObject.getString("other")));
		List<YearPlan> yearPlans = yearPlanMapper.selectByMainId(record.getId());
		projectPage.setYearPlanList(yearPlans);
		List<Company> companies1 = projectMapper.getCompanyInfoById(record.getId(),"1",0,20);
		projectPage.setCompanyList1(companies1);
		List<Company> companies2 = projectMapper.getCompanyInfoById(record.getId(),"2",0,20);
		projectPage.setCompanyList2(companies2);
		return projectPage;
	}

	String getArray(String s){
		return s.replaceAll("\\[","").replaceAll("\\]","");
	}
	public Map setMapstr(){

		String bussiness = projectLeaderMapper.getDepartmentIdByDepartmentName("无锡市城市重点建设项目管理中心", "");
		Map departmentMap = new HashMap();
		departmentMap.put("preOwner", "33025");
		departmentMap.put("midOwner", "33026");
		departmentMap.put("other", "33027");
		String office = projectLeaderMapper.getDepartmentIdByDepartmentName("总工程师办公室", bussiness);
		departmentMap.put("office", office);
		String plan = projectLeaderMapper.getDepartmentIdByDepartmentName("计划部", bussiness);
		departmentMap.put("plan", plan);
		String bridge = projectLeaderMapper.getDepartmentIdByDepartmentName("道桥工程部", bussiness);
		departmentMap.put("bridge", bridge);
		String build = projectLeaderMapper.getDepartmentIdByDepartmentName("建筑工程部", bussiness);
		departmentMap.put("build", build);
		String quality = projectLeaderMapper.getDepartmentIdByDepartmentName("质量安全监督部", bussiness);
		departmentMap.put("quality", quality);
		String finance = projectLeaderMapper.getDepartmentIdByDepartmentName("财务审计部", bussiness);
		departmentMap.put("finance", finance);
		return departmentMap;
	}

	public static void main(String[] args) {
		String ids="";
		List<String> idsList = Arrays.asList(ids.split(","));
		System.out.println(idsList.size());
		System.out.println(idsList.get(0));

	}

}
