package org.jeecg.modules.projectcompany.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.projectcompany.entity.ProjectCompany;
import org.jeecg.modules.projectcompany.service.IProjectCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: BS_PROJECT_COMPANY
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Api(tags="BS_PROJECT_COMPANY")
@RestController
@RequestMapping("/projectCompany/projectCompany")
@Slf4j
public class ProjectCompanyController extends JeecgController<ProjectCompany, IProjectCompanyService> {
	@Autowired
	private IProjectCompanyService projectCompanyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param projectCompany
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-分页列表查询")
	@ApiOperation(value="BS_PROJECT_COMPANY-分页列表查询", notes="BS_PROJECT_COMPANY-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ProjectCompany projectCompany,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProjectCompany> queryWrapper = QueryGenerator.initQueryWrapper(projectCompany, req.getParameterMap());
		Page<ProjectCompany> page = new Page<ProjectCompany>(pageNo, pageSize);
		IPage<ProjectCompany> pageList = projectCompanyService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param projectCompany
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-添加")
	@ApiOperation(value="BS_PROJECT_COMPANY-添加", notes="BS_PROJECT_COMPANY-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProjectCompany projectCompany) {
		projectCompanyService.save(projectCompany);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param projectCompany
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-编辑")
	@ApiOperation(value="BS_PROJECT_COMPANY-编辑", notes="BS_PROJECT_COMPANY-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProjectCompany projectCompany) {
		projectCompanyService.updateById(projectCompany);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-通过id删除")
	@ApiOperation(value="BS_PROJECT_COMPANY-通过id删除", notes="BS_PROJECT_COMPANY-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		projectCompanyService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-批量删除")
	@ApiOperation(value="BS_PROJECT_COMPANY-批量删除", notes="BS_PROJECT_COMPANY-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.projectCompanyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "BS_PROJECT_COMPANY-通过id查询")
	@ApiOperation(value="BS_PROJECT_COMPANY-通过id查询", notes="BS_PROJECT_COMPANY-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ProjectCompany projectCompany = projectCompanyService.getById(id);
		if(projectCompany==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(projectCompany);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param projectCompany
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProjectCompany projectCompany) {
        return super.exportXls(request, projectCompany, ProjectCompany.class, "BS_PROJECT_COMPANY");
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
        return super.importExcel(request, response, ProjectCompany.class);
    }

}
