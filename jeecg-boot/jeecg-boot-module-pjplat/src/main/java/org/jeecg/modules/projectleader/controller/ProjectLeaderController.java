package org.jeecg.modules.projectleader.controller;

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
import org.jeecg.common.system.vo.SysDepartModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.projectleader.entity.ProjectLeader;
import org.jeecg.modules.projectleader.service.IProjectLeaderService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: BS_PROJECT_LEADER
 * @Author: jeecg-boot
 * @Date:   2020-10-19
 * @Version: V1.0
 */
@Api(tags="BS_PROJECT_LEADER")
@RestController
@RequestMapping("/projectleader/projectLeader")
@Slf4j
public class ProjectLeaderController extends JeecgController<ProjectLeader, IProjectLeaderService> {
	@Autowired
	private IProjectLeaderService projectLeaderService;


	
	/**
	 * 分页列表查询
	 *
	 * @param projectLeader
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-分页列表查询")
	@ApiOperation(value="BS_PROJECT_LEADER-分页列表查询", notes="BS_PROJECT_LEADER-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProjectLeader projectLeader,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectLeader> queryWrapper = QueryGenerator.initQueryWrapper(projectLeader, req.getParameterMap());
		Page<ProjectLeader> page = new Page<ProjectLeader>(pageNo, pageSize);
		IPage<ProjectLeader> pageList = projectLeaderService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 @AutoLog(value = "用户姓名realname")
	 @ApiOperation(value="用户姓名realname", notes="用户姓名realname")
	 @GetMapping(value = "/lists")
	 public Result<?> queryPageLists(@RequestParam (name = "prjlPrjId") String prjlPrjId) {
 		JSONObject jsonObject = projectLeaderService.getProjectRealNameById(prjlPrjId);
		 return Result.ok(jsonObject);
	 }

	 @AutoLog(value = "用户账号username")
	 @ApiOperation(value="用户账号username", notes="用户账号username")
	 @GetMapping(value = "/liste")
	 public Result<?> queryPageListe(@RequestParam (name = "prjlPrjId") String prjlPrjId) {
		 JSONObject jsonObject = projectLeaderService.getProjectUserNameById(prjlPrjId);
		 return Result.ok(jsonObject);
	 }

	/**
	 *   添加
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-添加")
	@ApiOperation(value="BS_PROJECT_LEADER-添加", notes="BS_PROJECT_LEADER-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject jsonObject) {
		projectLeaderService.addProjectLeader(jsonObject);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param jsonObject
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-编辑")
	@ApiOperation(value="BS_PROJECT_LEADER-编辑", notes="BS_PROJECT_LEADER-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject jsonObject) {
		projectLeaderService.updateProjectLeader(jsonObject);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-通过id删除")
	@ApiOperation(value="BS_PROJECT_LEADER-通过id删除", notes="BS_PROJECT_LEADER-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		projectLeaderService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-批量删除")
	@ApiOperation(value="BS_PROJECT_LEADER-批量删除", notes="BS_PROJECT_LEADER-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectLeaderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_LEADER-通过id查询")
	@ApiOperation(value="BS_PROJECT_LEADER-通过id查询", notes="BS_PROJECT_LEADER-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectLeader projectLeader = projectLeaderService.getById(id);
		if(projectLeader==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(projectLeader);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectLeader
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectLeader projectLeader) {
        return super.exportXls(request, projectLeader, ProjectLeader.class, "BS_PROJECT_LEADER");
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
        return super.importExcel(request, response, ProjectLeader.class);
    }

}
