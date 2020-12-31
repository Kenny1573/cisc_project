package org.jeecg.modules.problemsheet.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.SysCategoryModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.problemsheet.entity.BsProblemSheet;
import org.jeecg.modules.problemsheet.entity.ProblemDeal;
import org.jeecg.modules.problemsheet.service.IBsProblemSheetService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 检查整改单
 * @Author: jeecg-boot
 * @Date:   2020-11-04
 * @Version: V1.0
 */
@Api(tags="检查整改单")
@RestController
@RequestMapping("/problemsheet/bsProblemSheet")
@Slf4j
public class BsProblemSheetController extends JeecgController<BsProblemSheet, IBsProblemSheetService> {
	
	@Autowired
	private IBsProblemSheetService bsProblemSheetService;
	
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	
	/**
	 * 分页列表查询
	 *
	 * @param bsProblemSheet
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "检查整改单-分页列表查询")
	@ApiOperation(value="检查整改单-分页列表查询", notes="检查整改单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(BsProblemSheet bsProblemSheet,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		String psCUnit = "";
		String psSUnit = "";
		
		Map<String, String[]> newpm = new HashMap(req.getParameterMap());
		
		if(newpm != null && newpm.containsKey("psConstructionUnit")) {
			psCUnit = newpm.get("psConstructionUnit")[0];
			newpm.remove("psConstructionUnit");
		}
		
		if(newpm != null && newpm.containsKey("psSupervisingUnit")) {
			psSUnit = newpm.get("psSupervisingUnit")[0];
			newpm.remove("psSupervisingUnit");
		}
		
		QueryWrapper<BsProblemSheet> queryWrapper = QueryGenerator.initQueryWrapper(bsProblemSheet, newpm);
		
		//手动查询条件
		if(oConvertUtils.isNotEmpty(psCUnit)) {
			queryWrapper.inSql("ps_prj_id", "select prj_id from pjplat.bs_project_company where com_type='1' and com_id="+psCUnit);
		}
		
		if(oConvertUtils.isNotEmpty(psSUnit)) {
			queryWrapper.inSql("ps_prj_id", "select prj_id from pjplat.bs_project_company where com_type='2' and com_id="+psSUnit);
		}
		
		//手动查询字段
		queryWrapper.select("id","ps_prj_id","(select min(com_id) from pjplat.bs_project_company where com_type='1' and prj_id=ps_prj_id) ps_construction_unit","(select min(com_id) from pjplat.bs_project_company where com_type='2' and prj_id=ps_prj_id) ps_supervising_unit","ps_location","ps_type","ps_level","ps_description","ps_advice","act_status","create_by","create_time","update_by","update_time","ps_time");
		
		Page<BsProblemSheet> page = new Page<BsProblemSheet>(pageNo, pageSize);
		IPage<BsProblemSheet> pageList = bsProblemSheetService.page(page, queryWrapper);
		
		List<DictModel> DictMsys = sysBaseAPI.queryFilterTableDictInfo("sys_category", "name", "code", "code like 'B03%'");
		List<DictModel> DictMsdi = sysBaseAPI.queryFilterTableDictInfo("sys_dict_item", "item_text", "item_value", "dict_id = (select id from sys_dict where dict_code = 'hazard_level')");

		//List prjs = pageList.getRecords().stream().map(BsProblemSheet->BsProblemSheet.getPsPrjId()).collect(Collectors.toList());
		
		pageList.getRecords().forEach(item->{
            //TODO 临时借用这个字段用于页面展示
			//item
			String tmpwt = "";
			String tmpfl = "";
			String tmpdj = "";
			
			if(item.getPsType() != null && !item.getPsType().equals("")) {
				
				for(DictModel dtm:DictMsys) {
					
					if(dtm.getValue().equals(item.getPsType().substring(0, 6))) {
						tmpwt = dtm.getText();
						if(!tmpfl.equals("")) {
							break;
						}	
					}
					
					if(dtm.getValue().equals(item.getPsType())) {
						tmpfl = dtm.getText();
						if(!tmpwt.equals("")) {
							break;
						}
					}
				}
			}
			
			if(item.getPsLevel() != null && !item.getPsLevel().equals("")) {
				
				for(DictModel dtm:DictMsdi) {
					if(dtm.getValue().equals(item.getPsLevel())) {
						tmpdj = "(等级:"+dtm.getText()+")";
						break;
					}
				}
				
			}
			
			item.setWtms(tmpwt+":"+tmpfl+tmpdj);
			
        });
		
		return Result.ok(pageList);
	}
	
	
	/**
	 * 分页列表查询
	 *
	 * @param psid
	 * @param pictype
	 * @return
	 */
	@AutoLog(value = "检查整改单照片")
	@ApiOperation(value="检查整改单照片", notes="检查整改单照片")
	@GetMapping(value = "/PSpiclist")
	public Result<?> queryPSpicList(@RequestParam(name="psid",required=true) Integer psid,@RequestParam(name="pictype",required=true) String pictype,@RequestParam(name="picurl",required=true) String picurl,@RequestParam(name="ydy",required=false) String ydy) {
		
		if(psid == null || psid.equals("")) {
			psid = -1;	
		}
		
		if(pictype == null || pictype.equals("")) {
			pictype = "1";	
		}
		
		if(picurl == null || picurl.equals("")) {
			picurl = "http://127.0.0.1:8080/jeecg-boot/";	
		}
		
		if(ydy == null || ydy.equals("")) {
			ydy = "";	
		}
		
		List<DictModel> DictMbpsp = sysBaseAPI.queryFilterTableDictInfo("pjplat.bs_problem_sheet_photo", "psp_photo_path", "psp_photo_type", "psp_ps_id="+psid+" ");//and psp_photo_type = '"+pictype+"'
		
		JSONArray pkjson = new JSONArray();
		JSONObject jsonf = new JSONObject();
		JSONArray jsobarray = new JSONArray();
		
		int i = 0;
		
		for(DictModel ptmp:DictMbpsp) {
			
			JSONObject jsob = new JSONObject();
			
			//jsob.put("psppid", i);
			//jsob.put("psp_photo_type", ptmp.getValue());
			jsob.put("imgUrl", picurl+ptmp.getText());
			
			i++;
			
			jsobarray.add(jsob);
		}
		
		if(!ydy.equals("dy") ) {
			
			for(int j=i;j<9;j++) {
	            JSONObject jsob = new JSONObject();
	            jsob.put("imgUrl", picurl+"nodata.png");
				jsobarray.add(jsob);
			}
			
		}
		
		jsonf.put("key", 0);
		jsonf.put("fileDetails", jsobarray);
			
		pkjson.add(jsonf);
		
		return Result.ok(pkjson);
	}
	
	
	/**
	 * 分页列表查询
	 *
	 * @param psid
	 * @param taskid
	 * @return
	 */
	@AutoLog(value = "整改照片")
	@ApiOperation(value="整改照片", notes="整改照片")
	@GetMapping(value = "/ZGpiclist")
	public Result<?> queryZGpicList(@RequestParam(name="psid",required=true) Integer psid,@RequestParam(name="taskid",required=true) String taskid,@RequestParam(name="picurl",required=true) String picurl,@RequestParam(name="ydy",required=false) String ydy) {
		
		if(psid == null || psid.equals("")) {
			psid = -1;	
		}
		
		if(taskid == null || taskid.equals("")) {
			taskid = "-1";	
		}
		
		if(picurl == null || picurl.equals("")) {
			picurl = "http://127.0.0.1:8080/jeecg-boot/";	
		}
		
		if(ydy == null || ydy.equals("")) {
			ydy = "";	
		}
		
		List<DictModel> DictMbpsp = sysBaseAPI.queryFilterTableDictInfo("pjplat.bs_problem_sheet_record", "psr_ps_photo", "psr_act_task_id", "psr_ps_id="+psid+" and psr_act_task_id = "+taskid);
		
		JSONArray pkjson = new JSONArray();
		JSONObject jsonf = new JSONObject();
		JSONArray jsobarray = new JSONArray();
		
		int i = 0;
		
		for(DictModel ptmp:DictMbpsp) {
			
			JSONObject jsob = new JSONObject();
			
			//jsob.put("psppid", i);
			//jsob.put("psp_photo_type", ptmp.getValue());
			jsob.put("imgUrl", picurl+ptmp.getText());
			
			i++;
			
			jsobarray.add(jsob);
		}
		
		if(!ydy.equals("dy") ) {
			for(int j=i;j<9;j++) {
	            JSONObject jsob = new JSONObject();
	            jsob.put("imgUrl", picurl+"nodata.png");
				jsobarray.add(jsob);
			}
		}
		
		jsonf.put("key", 0);
		jsonf.put("ZfileDetails", jsobarray);
			
		pkjson.add(jsonf);
		
		return Result.ok(pkjson);
	}
	
	
	/**
	 * 分页列表查询
	 *
	 * @param sheetid
	 * @param stype
	 * @return
	 */
	@AutoLog(value = "整改单")
	@ApiOperation(value="整改单", notes="整改单")
	@GetMapping(value = "/PSdealcplist")
	public Result<?> queryPSdealcpList(@RequestParam(name="sheetid",required=true) Integer sheetid,@RequestParam(name="stype",required=true) String stype) {
		
		//JSONArray pkjson = new JSONArray();
		JSONObject jsobtmp = new JSONObject();
		
		ProblemDeal problemtmp = new ProblemDeal();
		ProblemDeal tmp = new ProblemDeal();
		
		tmp.setId(Long.valueOf(0));
		tmp.setAvmeg("");
		tmp.setDeakey("");
		tmp.setDealsj("");
		tmp.setDes("");
		tmp.setTaskid("0");
		tmp.setType("");
		tmp.setUname("");
		
		if(sheetid == null || sheetid.equals("")) {
			sheetid = -1;
		}
		
		if(stype == null || stype.equals("")) {
			stype = "1";
		}
		
		List<DictModel> DictMbpsjc = sysBaseAPI.queryFilterTableDictInfo("htplat.act_z_business", "nvl((select realname from htplat.sys_user where username=user_id),'')", "to_char(apply_time,'yyyy/mm/dd hh24:mi:ss')", "table_name='pjplat.BS_PROBLEM_SHEET'and table_id='"+sheetid+"'");
		
		if(DictMbpsjc != null && !DictMbpsjc.isEmpty()) {
			
			if(DictMbpsjc.get(0).getText()== null) {
				problemtmp.setUname("");
			}else {
				problemtmp.setUname(DictMbpsjc.get(0).getText());
			}
			
			if(DictMbpsjc.get(0).getValue()== null) {
				problemtmp.setDealsj("");
			}else {
				problemtmp.setDealsj(DictMbpsjc.get(0).getValue());
			}
			
			//problemtmp.setDealsj(DictMbpsjc.get(0).getValue());
			problemtmp.setId(Long.valueOf(0));
			problemtmp.setAvmeg("");
			problemtmp.setDeakey("");
			problemtmp.setDes("");
			problemtmp.setTaskid("0");
			problemtmp.setType("");
		}else {
			problemtmp = tmp;
		}
		
		jsobtmp.put("start", problemtmp);
		
		problemtmp = new ProblemDeal();
		
		//整改情况
		stype = "deal";
		List<ProblemDeal> xmzg = bsProblemSheetService.getAvmessagebyid(String.valueOf(sheetid), stype);
	
		if(xmzg != null && !xmzg.isEmpty()) {
			problemtmp = xmzg.get(0);
		}else {
			problemtmp = tmp;
		}
		
		jsobtmp.put("deal", problemtmp);
		
		problemtmp = new ProblemDeal();
		//确认
		stype = "sure";
		List<ProblemDeal> xmzgqr = bsProblemSheetService.getAvmessagebyid(String.valueOf(sheetid), stype);
		
		if(xmzgqr != null && !xmzgqr.isEmpty()) {
			problemtmp = xmzgqr.get(0);
		}else {
			problemtmp = tmp;
		}
		
		jsobtmp.put("sure", problemtmp);
		
		problemtmp = new ProblemDeal();
		//检查
		stype = "check";
		List<ProblemDeal> xmzgck = bsProblemSheetService.getAvmessagebyid(String.valueOf(sheetid), stype);
		
		if(xmzgck != null && !xmzgck.isEmpty()) {
			problemtmp = xmzgck.get(0);
		}else {
			problemtmp = tmp;
		}
		
		jsobtmp.put("check", problemtmp);
	
		return Result.ok(jsobtmp);
	}
	
	/**
	 *   添加
	 *
	 * @param bsProblemSheet
	 * @return
	 */
	@AutoLog(value = "检查整改单-添加")
	@ApiOperation(value="检查整改单-添加", notes="检查整改单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody BsProblemSheet bsProblemSheet) {
		bsProblemSheetService.save(bsProblemSheet);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param bsProblemSheet
	 * @return
	 */
	@AutoLog(value = "检查整改单-编辑")
	@ApiOperation(value="检查整改单-编辑", notes="检查整改单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody BsProblemSheet bsProblemSheet) {
		bsProblemSheetService.updateById(bsProblemSheet);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查整改单-通过id删除")
	@ApiOperation(value="检查整改单-通过id删除", notes="检查整改单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		bsProblemSheetService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "检查整改单-批量删除")
	@ApiOperation(value="检查整改单-批量删除", notes="检查整改单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.bsProblemSheetService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "检查整改单-通过id查询")
	@ApiOperation(value="检查整改单-通过id查询", notes="检查整改单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		BsProblemSheet bsProblemSheet = bsProblemSheetService.getById(id);
		if(bsProblemSheet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(bsProblemSheet);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param bsProblemSheet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BsProblemSheet bsProblemSheet) {
        return super.exportXls(request, bsProblemSheet, BsProblemSheet.class, "检查整改单");
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
        return super.importExcel(request, response, BsProblemSheet.class);
    }

}
