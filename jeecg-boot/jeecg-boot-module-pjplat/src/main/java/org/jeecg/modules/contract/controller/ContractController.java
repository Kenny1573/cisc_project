package org.jeecg.modules.contract.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.contract.entity.Contract;
import org.jeecg.modules.contract.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 合同信息
 * @Author: jeecg-boot
 * @Date:   2020-10-22
 * @Version: V1.0
 */
@Api(tags="合同信息")
@RestController
@RequestMapping("/contract/contract")
@Slf4j
public class ContractController extends JeecgController<Contract, IContractService> {
	@Autowired
	private IContractService contractService;
	
	/**
	 * 分页列表查询
	 *
	 * @param contract
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "合同信息-分页列表查询")
	@ApiOperation(value="合同信息-分页列表查询", notes="合同信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Contract contract,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Contract> queryWrapper = QueryGenerator.initQueryWrapper(contract, req.getParameterMap());
		Page<Contract> page = new Page<Contract>(pageNo, pageSize);
		IPage<Contract> pageList = contractService.page(page, queryWrapper);
		List<JSONObject> jsonObjectList = new ArrayList<>();

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		pageList.getRecords().forEach(item->{

			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(item));
            if(item.getConBeginDate()!=null)
                jsonObject.put("conBeginDate",sdf.format(item.getConBeginDate()));
            if(item.getConEndDate()!=null)
                jsonObject.put("conEndDate",sdf.format(item.getConEndDate()));
            if(item.getConSignDate()!=null)
                jsonObject.put("conSignDate",sdf.format(item.getConSignDate()));
			String projectName=contractService.getCompanyName(item.getConPrjId());
			jsonObject.put("prjName",projectName);
			jsonObjectList.add(jsonObject);
		});
		Page<JSONObject> res = new Page<>();
		res.setRecords(jsonObjectList);
		res.setCurrent(pageList.getCurrent());
		res.setTotal(pageList.getTotal());
		res.setSize(pageList.getSize());

		return Result.ok(res);
	}


	/**
	 *   添加
	 *
	 * @param contract
	 * @return
	 */
	@AutoLog(value = "合同信息-添加")
	@ApiOperation(value="合同信息-添加", notes="合同信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Contract contract) {
		contractService.save(contract);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param contract
	 * @return
	 */
	@AutoLog(value = "合同信息-编辑")
	@ApiOperation(value="合同信息-编辑", notes="合同信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Contract contract) {
		contractService.updateById(contract);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "合同信息-通过id删除")
	@ApiOperation(value="合同信息-通过id删除", notes="合同信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		contractService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "合同信息-批量删除")
	@ApiOperation(value="合同信息-批量删除", notes="合同信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.contractService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "合同信息-通过id查询")
	@ApiOperation(value="合同信息-通过id查询", notes="合同信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Contract contract = contractService.getById(id);
		if(contract==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(contract);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param contract
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Contract contract) {
        return super.exportXls(request, contract, Contract.class, "合同信息");
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
        return super.importExcel(request, response, Contract.class);
    }

}
