package org.jeecg.modules.helmet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.helmet.entity.HelmetExcelEntity;
import org.jeecg.modules.helmet.entity.HelmetExcelExport;
import org.jeecg.modules.helmet.service.IHelmetService;
import org.jeecg.modules.helmet.task.MessageQueueTask;
import org.jeecg.modules.utils.MonthUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/helmet")
public class HelmetController  {

    @Autowired
    IHelmetService helmetService;


    @AutoLog(value = "安全帽列表")
    @ApiOperation(value="安全帽列表", notes="安全帽列表")
    @GetMapping("/getList")
    public Result<?> getList(@RequestParam(name = "pageStart",defaultValue = "0")Integer pageStart,
                             @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        Page page = new Page(pageStart,pageSize);
        page.setRecords(helmetService.getList(pageStart,pageSize));

       return Result.ok(page);
    }

    @AutoLog(value = "安全帽序列号模糊查询列表")
    @ApiOperation(value="安全帽序列号模糊查询列表", notes="安全帽序列号模糊查询列表")
    @GetMapping("/getLikeList")
    public Result<?> getLikeList(@RequestParam(name = "pageStart",defaultValue = "0")Integer pageStart,
                             @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize,String deviceSerial){
        if (deviceSerial==null)
            deviceSerial="";
        Page page = new Page(pageStart,pageSize);
        page.setRecords(helmetService.getLikeList(pageStart,pageSize,deviceSerial));

        return Result.ok(page);
    }

    @AutoLog(value = "根据序列号以及验证码添加安全帽")
    @ApiOperation(value="根据序列号以及验证码添加安全帽", notes="根据序列号以及验证码添加安全帽")
    @GetMapping("/addHelmet")
    public Result<?> addHelmet(@RequestParam(name = "deviceSerial",required = true)String deviceSerial,
                                    @RequestParam(name = "validateCode",required = true)String validateCode) {
        return Result.ok(helmetService.addHelmet(deviceSerial,validateCode).getString("msg"));
    }


    @AutoLog(value = "根据序列号以及验证码批量添加安全帽")
    @ApiOperation(value="根据序列号以及验证码批量添加安全帽", notes="根据序列号以及验证码批量添加安全帽")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {

        return helmetService.importExcel(request, response, HelmetExcelEntity.class);
    }

    @AutoLog(value = "根据序列号获取安全帽配置")
    @ApiOperation(value="根据序列号获取安全帽配置", notes="根据序列号获取安全帽配置")
    @GetMapping("/getConfigInfo")
    public Result<?> getConfigInfo(@RequestParam(name = "deviceSerial",required = true)String deviceSerial) {
        return Result.ok(helmetService.getConfigInfo(deviceSerial));
    }


    @AutoLog(value = "配置安全帽")
    @ApiOperation(value="配置安全帽", notes="配置安全帽")
    @PostMapping("/configHelmet")
    public Result<?> configHelmet(@RequestBody  JSONObject safetyHelmetCfg) {
        return Result.ok(helmetService.configHelmet(safetyHelmetCfg));
    }

    @AutoLog(value = "获取安全帽配置能力集")
    @ApiOperation(value="获取安全帽配置能力集", notes="获取安全帽配置能力集")
    @GetMapping("/configHelmetInfo")
    public Result<?> configHelmetInfo(String deviceSerial) {
        return helmetService.configHelmetInfo(deviceSerial);
    }


    @AutoLog(value = "个人安全帽录入")
    @ApiOperation(value="个人安全帽录入", notes="个人安全帽录入")
    @GetMapping("/addMyHelmet")
    public Result<?> addHelmet(@RequestParam(name = "deviceSerial",required = true)String deviceSerial,
                               @RequestParam(name = "validateCode",required = true)String validateCode,
                               @RequestParam(name = "username",required = true)String username) {
        return helmetService.addMyHelmet(deviceSerial,validateCode,username);
    }


    @AutoLog(value = "获取个人安全帽信息")
    @ApiOperation(value="获取个人安全帽信息", notes="获取个人安全帽信息")
    @GetMapping("/getHelmetInfo")
    public Result<?> getHelmetInfo(@RequestParam(name = "username",required = true)String username) {
        return helmetService.getHelmetInfo(username);
    }

    @AutoLog(value = "解绑个人安全帽")
    @ApiOperation(value="解绑个人安全帽", notes="解绑个人安全帽")
    @GetMapping("/unlinkHelmet")
    public Result<?> unlinkHelmet(@RequestParam(name = "deviceSerial",required = true)String deviceSerial,
                                  @RequestParam(name = "username",required = true)String username) {
        return helmetService.unlinkHelmet(deviceSerial,username);
    }



    @AutoLog(value = "分配安全帽")
    @ApiOperation(value="分配安全帽", notes="分配安全帽")
    @GetMapping("/linkHelmet")
    public Result<?> linkHelmet(@RequestParam(name = "deviceSerial",required = true)String deviceSerial,
                                  @RequestParam(name = "username",required = true)String username) {
        return helmetService.linkHelmet(deviceSerial,username);
    }


    @AutoLog(value = "使安全帽废弃")
    @ApiOperation(value="使安全帽废弃", notes="移除安全帽与本平台的关联，")
    @GetMapping("/dismissHelmet")
    public Result<?> dismissHelmet(@RequestParam(name = "deviceSerial",required = true)String deviceSerial,
                                @RequestParam(name = "username",required = true)String username) {

        return helmetService.dismissHelmet(deviceSerial,username);
    }


    @AutoLog(value = "戴帽统计")
    @ApiOperation(value="戴帽统计", notes="戴帽统计，")
    @GetMapping("/wearHelmetStatistics")
    public Result<?> wearHelmetStatistics(@RequestParam(name = "prjId",required = true)String prjId,
                                   @RequestParam(name = "date1",required = true) Date date1,
                                          @RequestParam(name = "startTime",required = false,defaultValue = "08")Integer startTime,
                                          @RequestParam(name = "endTime",required = false,defaultValue = "17")Integer endTime
                                          ) {

        return helmetService.wearHelmetStatistics(prjId,date1,startTime,endTime);
    }

    @AutoLog(value = "异常统计")
    @ApiOperation(value="异常统计", notes="异常统计，")
    @GetMapping("/errorStatistics")
    public Result<?>errorStatistics(@RequestParam(name = "prjId",required = true)String prjId,
                                          @RequestParam(name = "startDate",required = false) String startDate,
                                            @RequestParam(name = "endDate",required = false) String endDate,
                                          @RequestParam(name = "startTime",required = false,defaultValue = "08")Integer startTime,
                                          @RequestParam(name = "endTime",required = false,defaultValue = "18")Integer endTime,
                                    @RequestParam(name = "type",required = true)String type) throws ParseException {
        return helmetService.errorStatistics(prjId,startDate,endDate,startTime,endTime,type);
    }
    @RequestMapping(value = "/exportData")
    public ModelAndView exportXls(@RequestBody JSONObject jsonObject) throws ParseException {

        JSONArray type = jsonObject.getJSONArray("xData");
        JSONArray num = jsonObject.getJSONArray("yData");
        String startDate=jsonObject.getString("startDate").replace("\"","").substring(0,10);
        String endDate=jsonObject.getString("endDate").replace("\"","").substring(0,10);
        Integer start =  jsonObject.getInteger("startTime");
        Integer end = jsonObject.getInteger("endTime");


        String typeDate=jsonObject.getString("type");

        List<HelmetExcelExport> list = new ArrayList<>();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        for (int i = 0; i <type.size() ; i++) {
            HelmetExcelExport helmetExcelExport = new HelmetExcelExport();
            helmetExcelExport.setType(type.getString(i));
            helmetExcelExport.setNum(num.getLong(i));
            list.add(helmetExcelExport);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date s = sdf.parse(startDate);
        Date e = sdf.parse(endDate) ;
        switch (typeDate){
            case "day":{
                mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("安全帽统计数据","日期："+startDate+" ~ "+endDate+" 时段："+start+"时 ~ "+end+"时",  "安全帽统计数据"));

            };break;
            case "month":{

                startDate= MonthUtil.getMonthFirstDay(startDate);
                endDate = MonthUtil.getMonthLastDay(endDate);
                mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("安全帽统计数据","日期："+startDate+" ~ "+endDate+" 时段："+start+"时 ~ "+end+"时",  "安全帽统计数据"));

            };break;
            case "week":{

                startDate=MonthUtil.getWeekMonDay(s);
                endDate=MonthUtil.getWeekSunDay(e);
                mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("安全帽统计数据","日期："+startDate+" ~ "+endDate+" 时段："+start+"时 ~ "+end+"时",  "安全帽统计数据"));

            };break;
            case "year":{
                s.setMonth(0);
                s.setDate(1);
                e.setMonth(11);
                e.setDate(31);
                startDate=sdf.format(s);
                endDate=sdf.format(e);
                mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("安全帽统计数据","日期："+startDate+" ~ "+endDate+" 时段："+start+"时 ~ "+end+"时",  "安全帽统计数据"));

            };break;
        }

        mv.addObject(NormalExcelConstants.CLASS, HelmetExcelExport.class);

        mv.addObject(NormalExcelConstants.FILE_NAME, "安全帽统计数据"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.DATA_LIST, list);
        return mv;


    }

    @Autowired
    MessageQueueTask messageQueueTask;




}
