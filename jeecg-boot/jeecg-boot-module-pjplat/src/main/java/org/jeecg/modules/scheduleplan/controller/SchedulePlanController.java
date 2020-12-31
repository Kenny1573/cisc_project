package org.jeecg.modules.scheduleplan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.scheduleplan.entity.SchedulePlan;
import org.jeecg.modules.scheduleplan.service.ISchedulePlanService;

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
 * @Description: 里程碑计划
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
@Api(tags="里程碑计划")
@RestController
@RequestMapping("/scheduleplan/schedulePlan")
@Slf4j
public class SchedulePlanController extends JeecgController<SchedulePlan, ISchedulePlanService> {
	@Autowired
	private ISchedulePlanService schedulePlanService;

	 @AutoLog(value = "获取树形菜单所有数据")
	 @ApiOperation(value="获取树形菜单所有数据", notes="获取树形菜单所有数据")
		 @GetMapping(value = "/treeList")
	 public Result<?> queryPageList(@RequestParam(name="prjId") Long prjId) {
		 return Result.ok(schedulePlanService.getMenu(prjId));
	 }

	 @AutoLog(value = "获取gantt所有数据")
	 @ApiOperation(value="获取gantt所有数据", notes="获取gantt所有数据")
	 @GetMapping(value = "/ganttList")
	 public Result<?> queryganttList(@RequestParam(name="prjId") Long prjId) {
		 return Result.ok(schedulePlanService.getGantt(prjId));
	 }



	 @AutoLog(value = "里程碑计划-根据Id获取详情")
	 @ApiOperation(value="里程碑计划-根据Id获取详情", notes="里程碑计划-根据Id获取详情")
	 @GetMapping(value = "/getSchedulePlanById")
	 public Result<?> getSchedulePlanById(@RequestParam(name="id",required=true) String id) {

		 return Result.ok(schedulePlanService.getById(id));
	 }













	 /**
	 * 分页列表查询
	 *
	 * @param schedulePlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "里程碑计划-分页列表查询")
	@ApiOperation(value="里程碑计划-分页列表查询", notes="里程碑计划-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SchedulePlan schedulePlan,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SchedulePlan> queryWrapper = QueryGenerator.initQueryWrapper(schedulePlan, req.getParameterMap());
		Page<SchedulePlan> page = new Page<SchedulePlan>(pageNo, pageSize);
		IPage<SchedulePlan> pageList = schedulePlanService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param schedulePlan
	 * @return
	 */
	@AutoLog(value = "里程碑计划-添加")
	@ApiOperation(value="里程碑计划-添加", notes="里程碑计划-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SchedulePlan schedulePlan) {
		schedulePlan.setSpNodeInFactBegin(schedulePlan.getSpNodeBeginTime());
		schedulePlan.setSpNodeInFactEnd(schedulePlan.getSpNodeEndTime());
		schedulePlanService.save(schedulePlan);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param schedulePlan
	 * @return
	 */
	@AutoLog(value = "里程碑计划-编辑")
	@ApiOperation(value="里程碑计划-编辑", notes="里程碑计划-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SchedulePlan schedulePlan) {
		schedulePlanService.updateById(schedulePlan);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "里程碑计划-通过id删除")
	@ApiOperation(value="里程碑计划-通过id删除", notes="里程碑计划-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		schedulePlanService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "里程碑计划-批量删除")
	@ApiOperation(value="里程碑计划-批量删除", notes="里程碑计划-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.schedulePlanService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "里程碑计划-通过id查询")
	@ApiOperation(value="里程碑计划-通过id查询", notes="里程碑计划-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SchedulePlan schedulePlan = schedulePlanService.getById(id);
		if(schedulePlan==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(schedulePlan);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param schedulePlan
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SchedulePlan schedulePlan) {
        return super.exportXls(request, schedulePlan, SchedulePlan.class, "里程碑计划");
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
        return super.importExcel(request, response, SchedulePlan.class);
    }

}
