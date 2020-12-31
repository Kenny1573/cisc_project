package org.jeecg.modules.monthreport.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.monthreport.entity.MonthReport;
import org.jeecg.modules.monthreport.service.IMonthReportService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.monthreport.vo.MonthReportExport;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.service.IYearPlanService;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.service.IYearPlanDetailService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Api(tags="进度月报")
@RestController
@RequestMapping("/monthreport/monthReport")
@Slf4j
public class MonthReportController extends JeecgController<MonthReport, IMonthReportService> {
	@Autowired
	 IYearPlanService yearPlanService;
	@Autowired
	 IYearPlanDetailService yearPlanDetailService;
	@Autowired
	private IMonthReportService monthReportService;

	 @AutoLog(value = "获取目标进度月报的上月的投资计划以及上月进度月报的下月计划安排")
	 @ApiOperation(value="新增/更新某月的月度计划", notes="新增/更新某月的月度计划")
	 @GetMapping(value = "/getLastMonthData")
	 public Result<?> getLastMonthData(@RequestParam(name="prjId") Long prjId,
									   @RequestParam(name="year") String year,
									   @RequestParam(name="month") String month){

		 //根据prjId,year获取到该年的年度考核计划
		 YearPlan thisYearPlan = yearPlanService.getOne(new QueryWrapper<YearPlan>().eq("YP_PRJ_ID", prjId).eq("YP_YEAR", year));

		 QueryWrapper<YearPlanDetail> queryWrapper = new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", thisYearPlan.getId()).eq("YPD_MONTH", month);
		 YearPlanDetail thisPlanDetail = yearPlanDetailService.getOne(queryWrapper);
		 MonthReport monthReport = monthReportService.getOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID",thisPlanDetail.getId()));
	 	JSONObject jsonObject =monthReportService.getLastMonthData(prjId,year,month);
		 if (monthReport!=null){
			 jsonObject.put("mrFinishInvestment",monthReport.getMrFinishInvestment());
			 jsonObject.put("mrDescription",monthReport.getMrDescription());
			 jsonObject.put("mrNeedCoordinate",monthReport.getMrNeedCoordinate());
			 jsonObject.put("mrNextPlan",monthReport.getMrNextPlan());
			 jsonObject.put("mrNeedCoordinateShow",monthReport.getMrNeedCoordinateShow());
		 }else{
			 jsonObject.put("mrFinishInvestment","");
			 jsonObject.put("mrDescription","");
			 jsonObject.put("mrNeedCoordinate","");
			 jsonObject.put("mrNextPlan","");
			 jsonObject.put("mrNeedCoordinateShow","");
		 }
		 return Result.ok(jsonObject );

	 }


	
	/**
	 * 分页列表查询
	 *
	 * @param monthReport
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "进度月报-分页列表查询")
	@ApiOperation(value="进度月报-分页列表查询", notes="进度月报-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MonthReport monthReport,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MonthReport> queryWrapper = QueryGenerator.initQueryWrapper(monthReport, req.getParameterMap());
		Page<MonthReport> page = new Page<MonthReport>(pageNo, pageSize);
		return Result.ok(monthReportService.listMonthReport(page));
	}

	 /**
	  * 分页列表查询
	  *
	  * @param
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "进度月报-分页列表查询")
	 @ApiOperation(value="进度月报-分页列表查询", notes="进度月报-分页列表查询")
	 @GetMapping(value = "/listByPrjId")
	 public Result<?> queryPageList(@RequestParam(name="prjId") Long prjId,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize
									) {
			 Page<JSONObject> page = new Page<JSONObject>(pageNo, pageSize);
		 page=monthReportService.listMonthReportByPrjId(prjId,page);
		 return Result.ok(page);
	 }
	
	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "进度月报-添加")
	@ApiOperation(value="进度月报-添加", notes="进度月报-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		return  monthReportService.addMonthReport(jsonObject);
	}
	
	/**
	 *  编辑
	 *
	 * @param monthReport
	 * @return
	 */
	@AutoLog(value = "进度月报-编辑")
	@ApiOperation(value="进度月报-编辑", notes="进度月报-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody MonthReport monthReport) {

		monthReportService.updateById(monthReport);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "进度月报-通过id删除")
	@ApiOperation(value="进度月报-通过id删除", notes="进度月报-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		monthReportService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "进度月报-批量删除")
	@ApiOperation(value="进度月报-批量删除", notes="进度月报-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.monthReportService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "进度月报-通过id查询")
	@ApiOperation(value="进度月报-通过id查询", notes="进度月报-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		MonthReport monthReport = monthReportService.getById(id);
		if(monthReport==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(monthReport);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "进度月报-通过id查询")
	 @ApiOperation(value="进度月报-通过id查询", notes="进度月报-通过id查询")
	 @GetMapping(value = "/getProjectByMrId")
	 public Result<?> getProjectByMrId(@RequestParam(name="mrYpdId",required=true) Long id) {
		 return Result.ok(monthReportService.getProjectByMrYpdId(id));
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param monthReport
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MonthReport monthReport) {
        return monthReportService.exportXls(request, MonthReportExport.class, "进度月报");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MonthReport.class);
    }

}
