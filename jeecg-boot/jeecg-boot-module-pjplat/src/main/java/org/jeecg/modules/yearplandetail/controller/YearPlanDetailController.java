package org.jeecg.modules.yearplandetail.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.service.IYearPlanDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 月度计划
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
@Api(tags="月度计划")
@RestController
@RequestMapping("/yearplandetail/yearPlanDetail")
@Slf4j
public class YearPlanDetailController extends JeecgController<YearPlanDetail, IYearPlanDetailService> {
	@Autowired
	private IYearPlanDetailService yearPlanDetailService;


	 @AutoLog(value = "月度计划")
	 @ApiOperation(value="月度计划", notes="月度计划查询")
	 @GetMapping(value = "/getMonthPlanList")
	 public Result<?> getMonthPlanList(Long prjId){
	 	return Result.ok(yearPlanDetailService.getMonthPlanList(prjId));
	 }


	 @AutoLog(value = "新增/更新某月的月度计划")
	 @ApiOperation(value="新增/更新某月的月度计划", notes="新增/更新某月的月度计划")
	 @PostMapping(value = "/addOrUpdateMonthPlanList")
	 public Result<?> addOrUpdateMonthPlanList(@RequestBody JSONObject jsonObject){
	 	yearPlanDetailService.addOrUpdateMonthPlanList(jsonObject);
	 	return Result.ok("修改成功");

	 }






































	/**
	 * 分页列表查询
	 *
	 * @param yearPlanDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "月度计划-分页列表查询")
	@ApiOperation(value="月度计划-分页列表查询", notes="月度计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(YearPlanDetail yearPlanDetail,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<YearPlanDetail> queryWrapper = QueryGenerator.initQueryWrapper(yearPlanDetail, req.getParameterMap());
		Page<YearPlanDetail> page = new Page<YearPlanDetail>(pageNo, pageSize);
		IPage<YearPlanDetail> pageList = yearPlanDetailService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yearPlanDetail
	 * @return
	 */
	@AutoLog(value = "月度计划-添加")
	@ApiOperation(value="月度计划-添加", notes="月度计划-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody YearPlanDetail yearPlanDetail) {
		yearPlanDetailService.save(yearPlanDetail);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param yearPlanDetail
	 * @return
	 */
	@AutoLog(value = "月度计划-编辑")
	@ApiOperation(value="月度计划-编辑", notes="月度计划-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody YearPlanDetail yearPlanDetail) {
		yearPlanDetailService.updateById(yearPlanDetail);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "月度计划-通过id删除")
	@ApiOperation(value="月度计划-通过id删除", notes="月度计划-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		yearPlanDetailService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "月度计划-批量删除")
	@ApiOperation(value="月度计划-批量删除", notes="月度计划-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yearPlanDetailService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "月度计划-通过id查询")
	@ApiOperation(value="月度计划-通过id查询", notes="月度计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		YearPlanDetail yearPlanDetail = yearPlanDetailService.getById(id);
		if(yearPlanDetail==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(yearPlanDetail);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yearPlanDetail
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YearPlanDetail yearPlanDetail) {
        return super.exportXls(request, yearPlanDetail, YearPlanDetail.class, "月度计划");
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
        return super.importExcel(request, response, YearPlanDetail.class);
    }

}
