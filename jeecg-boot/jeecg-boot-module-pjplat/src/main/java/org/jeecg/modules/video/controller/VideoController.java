package org.jeecg.modules.video.controller;

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
import org.jeecg.modules.video.entity.Video;
import org.jeecg.modules.video.service.IVideoService;

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
 * @Description: 监控管理
 * @Author: jeecg-boot
 * @Date:   2020-12-03
 * @Version: V1.0
 */
@Api(tags="监控管理")
@RestController
@RequestMapping("/video/video")
@Slf4j
public class VideoController extends JeecgController<Video, IVideoService> {
	@Autowired
	private IVideoService videoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param video
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "监控管理-分页列表查询")
	@ApiOperation(value="监控管理-分页列表查询", notes="监控管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Video video,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Video> queryWrapper = QueryGenerator.initQueryWrapper(video, req.getParameterMap());
		Page<Video> page = new Page<Video>(pageNo, pageSize);
		IPage<Video> pageList = videoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param video
	 * @return
	 */
	@AutoLog(value = "监控管理-添加")
	@ApiOperation(value="监控管理-添加", notes="监控管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Video video) {
		videoService.save(video);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param video
	 * @return
	 */
	@AutoLog(value = "监控管理-编辑")
	@ApiOperation(value="监控管理-编辑", notes="监控管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Video video) {
		videoService.updateById(video);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控管理-通过id删除")
	@ApiOperation(value="监控管理-通过id删除", notes="监控管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		videoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监控管理-批量删除")
	@ApiOperation(value="监控管理-批量删除", notes="监控管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.videoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监控管理-通过id查询")
	@ApiOperation(value="监控管理-通过id查询", notes="监控管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Video video = videoService.getById(id);
		if(video==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(video);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param video
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Video video) {
        return super.exportXls(request, video, Video.class, "监控管理");
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
        return super.importExcel(request, response, Video.class);
    }

}
