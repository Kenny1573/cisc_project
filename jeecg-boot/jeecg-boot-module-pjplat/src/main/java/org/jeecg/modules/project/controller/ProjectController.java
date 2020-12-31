package org.jeecg.modules.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.contract.entity.Contract;
import org.jeecg.modules.contract.service.IContractService;
import org.jeecg.modules.monthreport.entity.MonthReport;
import org.jeecg.modules.monthreport.service.IMonthReportService;
import org.jeecg.modules.projectcompany.entity.ProjectCompany;
import org.jeecg.modules.projectcompany.service.IProjectCompanyService;
import org.jeecg.modules.project.vo.ProjectPage;
import org.jeecg.modules.projectleader.entity.ProjectLeader;
import org.jeecg.modules.projectleader.service.IProjectLeaderService;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.service.IYearPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.util.*;

import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.project.service.IProjectService;
import org.jeecg.modules.project.service.IYearPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

 /**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Api(tags="项目信息")
@RestController
@RequestMapping("/project/project")
@Slf4j
public class ProjectController extends JeecgController<Project, IProjectService> {

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IYearPlanService yearPlanService;
	@Autowired
	private IProjectCompanyService projectCompanyService;
	@Autowired
	private IProjectLeaderService projectLeaderService;
	 @Autowired
	 private IMonthReportService monthReportService;
	 @Autowired
	 private IContractService contractService;
	 @Autowired
	 private IYearPlanDetailService yearPlanDetailService;



	 @AutoLog(value = "单项目级的项目列表")
	 @ApiOperation(value="单项目级的项目列表", notes="单项目级的项目列表")
		 @GetMapping(value = "/getProjectListByUsername")
	 public Result<?> queryPageList(
			 						@RequestParam(name="prjName",required=false)String prjName,
			 						@RequestParam(name="prjType",required=false)String prjType,
									@RequestParam(name="username")String username,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="1") Integer pageSize,
									HttpServletRequest req) {

		 Page<Project> page = new Page<Project>(pageNo, pageSize);
		 List<Project> projects = projectService.getProjectListByUsername(username,page,prjName,prjType);
		 page.setTotal(projects.size());
		 page.setRecords(projects);
		 return Result.ok(page);
	 }




	 @AutoLog(value = "获取项目中心详情页面内的权限列表")
	 @ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	 @GetMapping(value = "/getProjectListPermission")
	 public Result<?> getProjectListPermission(@RequestParam(name="baseInfo")String baseInfo) {


		 return Result.ok();
	 }





	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param project
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目信息-分页列表查询")
	@ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Project project,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Project> queryWrapper = QueryGenerator.initQueryWrapper(project, req.getParameterMap());
		Page<Project> page = new Page<Project>(pageNo, pageSize);
		IPage<Project> pageList = projectService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 @AutoLog(value = "项目信息-分页列表查询")
	 @ApiOperation(value="项目信息-分页列表查询", notes="项目信息-分页列表查询")
	 @GetMapping(value = "/listProject")
	 public Result<?> listProject() {

		 return Result.ok(projectService.listProject());
	 }
	/**
     *   添加
     * @param jsonObject
     * @return
     */
    @AutoLog(value = "项目信息-添加")
    @ApiOperation(value="项目信息-添加", notes="项目信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
    	projectService.createProject(jsonObject);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     * @param project
     * @return
     */
    @AutoLog(value = "项目信息-编辑")
    @ApiOperation(value="项目信息-编辑", notes="项目信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody Project project) {
        projectService.updateById(project);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "项目信息-通过id删除")
    @ApiOperation(value="项目信息-通过id删除", notes="项目信息-通过id删除")
    @DeleteMapping(value = "/delete")
	@Transactional(rollbackFor = Exception.class)
    public Result<?> delete(@RequestParam(name="id",required=true) Long id) {
    	QueryWrapper<ProjectLeader> projectLeaderQueryWrapper = new QueryWrapper<ProjectLeader>().eq("PRJL_PRJ_ID",id);
    	QueryWrapper<ProjectCompany> projectCompanyQueryWrapper = new QueryWrapper<ProjectCompany>().eq("PRJ_ID",id);
		QueryWrapper<YearPlan> yearPlanQueryWrapper = new QueryWrapper<YearPlan>().eq("YP_PRJ_ID",id);

		QueryWrapper<Contract> contractQueryWrapper = new QueryWrapper<Contract>().eq("CON_PRJ_ID",id);
		contractService.remove(contractQueryWrapper);

		projectLeaderService.remove(projectLeaderQueryWrapper);
    	projectCompanyService.remove(projectCompanyQueryWrapper);
    	List<YearPlan> yearPlans = yearPlanService.selectByMainId(id);
    	if(yearPlans.size()>0) {
			for (YearPlan yearPlan : yearPlans) {
				QueryWrapper<YearPlanDetail> yearPlanDetailQueryWrapper = new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlan.getId());
				YearPlanDetail yearPlanDetail = yearPlanDetailService.getOne(yearPlanDetailQueryWrapper);
				if(yearPlanDetail!=null) {
					QueryWrapper<MonthReport> monthReportQueryWrapper = new QueryWrapper<MonthReport>().eq("MR_YPD_ID", yearPlanDetail.getId());
					monthReportService.remove(monthReportQueryWrapper);
					yearPlanDetailService.remove(yearPlanDetailQueryWrapper);
				}
			}
		}

		yearPlanService.remove(yearPlanQueryWrapper);
        projectService.delMain(id);	
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "项目信息-批量删除")
    @ApiOperation(value="项目信息-批量删除", notes="项目信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList=Arrays.asList(ids.split(","));
		for(String id:idList) {
			this.delete(Long.parseLong(id));

		}
        return Result.ok("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Project project) {
		QueryWrapper<Project> queryWrapper = QueryGenerator.initQueryWrapper(project, request.getParameterMap());
		// Step.2 获取导出数据
		List<Project> pageList = projectService.list(queryWrapper);
		return  projectService.exportXls(request, project, ProjectPage.class, "项目信息",pageList);
    }

	 @RequestMapping(value = "/exportMyXls")
	 public ModelAndView exportMyXls(HttpServletRequest request,@RequestParam(name="username")String username
									 ) {
		 // Step.2 获取导出数据
		 List<Project> pageList = projectService.getProjectListByUsername2(username);
		 return  projectService.exportXls(request, null, ProjectPage.class, "项目信息",pageList);
	 }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Project.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-年度考核计划-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	@AutoLog(value = "年度考核计划-通过主表ID查询")
	@ApiOperation(value="年度考核计划-通过主表ID查询", notes="年度考核计划-通过主表ID查询")
	@GetMapping(value = "/listYearPlanByMainId")
    public Result<?> listYearPlanByMainId(YearPlan yearPlan,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<YearPlan> queryWrapper = QueryGenerator.initQueryWrapper(yearPlan, req.getParameterMap());
        Page<YearPlan> page = new Page<YearPlan>(pageNo, pageSize);
        IPage<YearPlan> pageList = yearPlanService.page(page, queryWrapper);
        List<YearPlan> list = pageList.getRecords();
        list.sort(new Comparator<YearPlan>() {
			@Override
			public int compare(YearPlan yearPlan, YearPlan t1) {
				if (Integer.parseInt(yearPlan.getYpYear())>Integer.parseInt(t1.getYpYear()))
				return 1;
				else return -1;
			}
		});
        pageList.setRecords(list);

        return Result.ok(pageList);
    }

	 /**
	  * 通过主表ID查询
	  * @return
	  */
	 @AutoLog(value = "年度考核计划-通过主表ID查询")
	 @ApiOperation(value="年度考核计划-通过主表ID查询", notes="年度考核计划-通过主表ID查询")
	 @GetMapping(value = "/listYearPlanWithFinanceAndInvestmentByMainId")
	 public Result<?> listYearPlanWithFinanceAndInvestmentByMainId(YearPlan yearPlan,
										   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										   HttpServletRequest req) {
		 QueryWrapper<YearPlan> queryWrapper = QueryGenerator.initQueryWrapper(yearPlan, req.getParameterMap());
		 Page<YearPlan> page = new Page<YearPlan>(pageNo, pageSize);
		 IPage<YearPlan> pageList = yearPlanService.page(page, queryWrapper);
		 List<YearPlan> yearPlans = pageList.getRecords();
		 JSONArray array=yearPlanDetailService.getTotalList(yearPlan.getYpPrjId());
		 JSONArray jsonArray= new JSONArray();
		 for (int i = 0; i <yearPlans.size() ; i++) {
		 	JSONObject jsonObject = new JSONObject();
			 for (int j = 0; j <array.size() ; j++) {
			 	JSONObject jobj= (JSONObject) array.get(j);
			 	if (yearPlans.get(i).getYpYear().equals(jobj.getString("year"))){
			 		jsonObject.put("id",yearPlans.get(i).getId());
					jsonObject.put("ypPrjId",yearPlans.get(i).getYpPrjId());
					jsonObject.put("ypYear",yearPlans.get(i).getYpYear());
					jsonObject.put("ypInvestment",yearPlans.get(i).getYpInvestment());
					jsonObject.put("ypFinance",yearPlans.get(i).getYpFinance());
					jsonObject.put("ypImageProgress",yearPlans.get(i).getYpImageProgress());
					jsonObject.put("investmentTotal",jobj.getString("investment"));
					jsonObject.put("financeTotal",jobj.getString("finance"));

				}

			 }
			 jsonArray.add(jsonObject);

		 }
		 JSONObject res = new JSONObject();
		 res.put("records",jsonArray);

		 return Result.ok(res);
	 }
	/**
	 * 添加
	 * @param yearPlan
	 * @return
	 */
	@AutoLog(value = "年度考核计划-添加")
	@ApiOperation(value="年度考核计划-添加", notes="年度考核计划-添加")
	@PostMapping(value = "/addYearPlan")
	public Result<?> addYearPlan(@RequestBody YearPlan yearPlan) {
		QueryWrapper<YearPlan> queryWrapper = new QueryWrapper<YearPlan>().eq("YP_PRJ_ID",yearPlan.getYpPrjId()).and(objectQueryWrapper -> objectQueryWrapper.eq("YP_YEAR",yearPlan.getYpYear()));
		List<YearPlan> pageList =yearPlanService.list(queryWrapper);
		if (pageList.size()>0)
			return Result.ok("已有要添加年份的年度计划，不能重复添加,建议更新或删除后再添加");
		else {
			yearPlanService.save(yearPlan);
			return Result.ok("添加成功！");
		}
	}
	 @AutoLog(value = "单位信息-批量删除")
	 @ApiOperation(value="单位信息-批量删除", notes="单位信息-批量删除")
	 @PostMapping(value = "/deleteBatchProjectCompany")
	 public Result<?> deleteBatchProjectCompany(@RequestBody JSONObject jsonObject) {
		projectService.deleteBatchProjectCompany(jsonObject);
		 return Result.ok("批量删除成功!");
	 }
    /**
	 * 编辑
	 * @param yearPlan
	 * @return
	 */
	@AutoLog(value = "年度考核计划-编辑")
	@ApiOperation(value="年度考核计划-编辑", notes="年度考核计划-编辑")
	@PutMapping(value = "/editYearPlan")
	public Result<?> editYearPlan(@RequestBody YearPlan yearPlan) {
		yearPlanService.updateById(yearPlan);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "年度考核计划-通过id删除")
	@ApiOperation(value="年度考核计划-通过id删除", notes="年度考核计划-通过id删除")
	@DeleteMapping(value = "/deleteYearPlan")
	public Result<?> deleteYearPlan(@RequestParam(name="id",required=true) String id) {
		yearPlanService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "年度考核计划-批量删除")
	@ApiOperation(value="年度考核计划-批量删除", notes="年度考核计划-批量删除")
	@DeleteMapping(value = "/deleteBatchYearPlan")
	public Result<?> deleteBatchYearPlan(@RequestParam(name="ids",required=true) String ids) {
	    this.yearPlanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportYearPlan")
    public ModelAndView exportYearPlan(HttpServletRequest request, YearPlan yearPlan) {
		 // Step.1 组装查询条件
		 QueryWrapper<YearPlan> queryWrapper = QueryGenerator.initQueryWrapper(yearPlan, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<YearPlan> pageList = yearPlanService.list(queryWrapper);
		 List<YearPlan> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "年度考核计划"); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, YearPlan.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("年度考核计划报表", "导出人:" + sysUser.getRealname(), "年度考核计划"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importYearPlan/{mainId}")
    public Result<?> importYearPlan(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") Long mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<YearPlan> list = ExcelImportUtil.importExcel(file.getInputStream(), YearPlan.class, params);
				 for (YearPlan temp : list) {
                    temp.setYpPrjId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 yearPlanService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.ok("文件导入成功！数据行数：" + list.size());
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-年度考核计划-end----------------------------------------------*/

	 /**
	  * 通过id获取关联的单位信息
	  * @param cid
	  * @return
	  */
	 @AutoLog(value = "通过id获取关联的单位信息")
	 @ApiOperation(value="通过id获取关联的单位信息", notes="通过id获取关联的单位信息")
	 @GetMapping(value = "/getProjectCompanyInfoById")
	 public Result<?> getProjectCompanyInfoById(Long cid,String type,
										 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										 HttpServletRequest req ) {
		 List<Company> companyList = projectService.getCompanyInfoById(cid,type,pageNo,pageSize);
		 Page<Company> page = new Page<>();
		 page.setRecords(companyList);
		 page.setCurrent(1);
		 page.setTotal(companyList.size());
		 page.setSize(20);

		 return Result.ok(page);
	 }

	 /**
	  * 通过id删除关联的单位记录
	  * @param cid
	  * @return
	  */
	 @AutoLog(value = "通过id删除关联的单位记录")
	 @ApiOperation(value="通过id删除关联的单位记录", notes="通过id删除关联的单位记录")
	 @DeleteMapping(value = "/deleteProjectCompanyInfoById")
	 public Result<?> deleteProjectCompanyInfoById(@RequestParam(name="id",required=true)Long cid,@RequestParam(name="prjId",required=true) Long prjId) {
		 projectService.deleteCompanyInfoById(cid,prjId);
		 return Result.ok("删除成功!");
 }
	 /**
	  * 通过projectCompany添加要关联的单位记录
	  * @param projectCompany
	  * @return
	  */
	 @AutoLog(value = "通过projectCompany添加要关联的单位记录")
	 @ApiOperation(value="通过projectCompany添加要关联的单位记录", notes="通过projectCompany添加要关联的单位记录")
	 @GetMapping(value = "/addProjectCompanyInfo")
	 public Result<?> addProjectCompanyInfo(@RequestBody ProjectCompany projectCompany,HttpServletRequest req ) {
		 QueryWrapper<ProjectCompany> queryWrapper = QueryGenerator.initQueryWrapper(projectCompany, req.getParameterMap());
		 Page<ProjectCompany> page = new Page<ProjectCompany>(1, 1);
		 IPage<ProjectCompany> pageList = projectCompanyService.page(page, queryWrapper);
		 if (pageList.getTotal()>0)
			 return Result.ok("已存在该单位，请勿重复添加该单位!");
		 else
		 	projectCompanyService.save(projectCompany);
		 return Result.ok("添加成功!");
	 }


	 @AutoLog(value = "项目关联单位批量添加")
	 @ApiOperation(value="项目关联单位批量添加", notes="项目关联单位批量添加")
	 @RequestMapping(value = "/addProjectCompanyBatch")
	 public Result<?> addProjectCompanyBatch(@RequestBody JSONObject jsonObject ) {
		 this.projectService.addProjectCompanyBatch(jsonObject);
		 return Result.ok("添加成功!");
	 }


}
