package org.jeecg.modules.kenny.controller;

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
import org.jeecg.modules.kenny.entity.BsHelmetHeartbeat;
import org.jeecg.modules.kenny.mapper.BsHelmetHeartbeatMapper;
import org.jeecg.modules.kenny.service.IBsHelmetHeartbeatService;

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
 * @Description: BS_HELMET_HEARTBEAT
 * @Author: jeecg-boot
 * @Date:   2020-12-29
 * @Version: V1.0
 */
@Api(tags="BS_HELMET_HEARTBEAT")
@RestController
@RequestMapping("/kenny/bsHelmetHeartbeat")
@Slf4j
public class BsHelmetHeartbeatController extends JeecgController<BsHelmetHeartbeat, IBsHelmetHeartbeatService> {
	@Autowired
	private IBsHelmetHeartbeatService bsHelmetHeartbeatService;
	@Autowired
	private BsHelmetHeartbeatMapper bsHelmetHeartbeatMapper;
	/**
	 * 分页列表查询
	 *
	 * @param bsHelmetHeartbeat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-分页列表查询")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-分页列表查询", notes="BS_HELMET_HEARTBEAT-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsHelmetHeartbeat bsHelmetHeartbeat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		QueryWrapper<BsHelmetHeartbeat> queryWrapper = QueryGenerator.initQueryWrapper(bsHelmetHeartbeat, req.getParameterMap());
//		Page<BsHelmetHeartbeat> page = new Page<BsHelmetHeartbeat>(pageNo, pageSize);
//		IPage<BsHelmetHeartbeat> pageList = bsHelmetHeartbeatService.page(page, queryWrapper);
		QueryWrapper<BsHelmetHeartbeat> bsHelmetHeartbeatQueryWrapper = new QueryWrapper<>();
		Page<BsHelmetHeartbeat> bsHelmetHeartbeatPage = new Page<>(pageNo, pageSize);
		IPage<BsHelmetHeartbeat> pageList = bsHelmetHeartbeatMapper.selectPage(bsHelmetHeartbeatPage, null);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param bsHelmetHeartbeat
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-添加")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-添加", notes="BS_HELMET_HEARTBEAT-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsHelmetHeartbeat bsHelmetHeartbeat) {
		bsHelmetHeartbeatService.save(bsHelmetHeartbeat);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsHelmetHeartbeat
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-编辑")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-编辑", notes="BS_HELMET_HEARTBEAT-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsHelmetHeartbeat bsHelmetHeartbeat) {
		bsHelmetHeartbeatService.updateById(bsHelmetHeartbeat);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-通过id删除")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-通过id删除", notes="BS_HELMET_HEARTBEAT-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsHelmetHeartbeatService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-批量删除")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-批量删除", notes="BS_HELMET_HEARTBEAT-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsHelmetHeartbeatService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_HELMET_HEARTBEAT-通过id查询")
	@ApiOperation(value="BS_HELMET_HEARTBEAT-通过id查询", notes="BS_HELMET_HEARTBEAT-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsHelmetHeartbeat bsHelmetHeartbeat = bsHelmetHeartbeatService.getById(id);
		if(bsHelmetHeartbeat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bsHelmetHeartbeat);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsHelmetHeartbeat
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsHelmetHeartbeat bsHelmetHeartbeat) {
        return super.exportXls(request, bsHelmetHeartbeat, BsHelmetHeartbeat.class, "BS_HELMET_HEARTBEAT");
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
        return super.importExcel(request, response, BsHelmetHeartbeat.class);
    }

}
