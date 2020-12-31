package org.jeecg.modules.monthreport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.BeanProperty;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.monthreport.entity.MonthReport;
import org.jeecg.modules.monthreport.mapper.MonthReportMapper;
import org.jeecg.modules.monthreport.service.IMonthReportService;
import org.jeecg.modules.monthreport.vo.MonthReportExport;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.mapper.ProjectMapper;
import org.jeecg.modules.project.mapper.YearPlanMapper;
import org.jeecg.modules.project.service.IProjectService;
import org.jeecg.modules.project.service.IYearPlanService;
import org.jeecg.modules.project.vo.ProjectPage;
import org.jeecg.modules.utils.MonthUtil;
import org.jeecg.modules.yearplandetail.entity.MonthPlan;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.mapper.YearPlanDetailMapper;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date: 2020-10-30
 * @Version: V1.0
 */
@Service
public class MonthReportServiceImpl extends ServiceImpl<MonthReportMapper, MonthReport> implements IMonthReportService {

    @Autowired
    MonthReportMapper monthReportMapper;
    @Autowired
    YearPlanDetailMapper yearPlanDetailMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    YearPlanMapper yearPlanMapper;
    @Autowired
    IYearPlanService yearPlanService;

    @Override
    public IPage listMonthReport(Page<MonthReport> page) {
        IPage pageList = monthReportMapper.selectPage(page, new QueryWrapper<>());
        List<MonthReport> monthReports = pageList.getRecords();
        JSONArray jsonArray = new JSONArray();
        for (MonthReport monthReport : monthReports) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(monthReport);
            YearPlanDetail yearPlanDetail = yearPlanDetailMapper.selectById(monthReport.getMrYpdId());
            YearPlan yearPlan = yearPlanMapper.selectById(yearPlanDetail.getYpdYpId());
            Project project = projectMapper.selectById(yearPlan.getYpPrjId());
            jsonObject.put("month", yearPlanDetail.getYpdMonth());
            jsonObject.put("year", yearPlan.getYpYear());
            jsonObject.put("projectName", project.getPrjName());
            jsonArray.add(jsonObject);
        }
        pageList.setRecords(jsonArray);
        return pageList;
    }

    @Override
    public ModelAndView exportXls(HttpServletRequest request,  Class<MonthReportExport> clazz, String title) {
        // Step.1 组装查询条件

        List<MonthReport> pa = this.list();
        List<MonthReportExport> pageList = pa.stream().map(m->{
            MonthReportExport mExport = new MonthReportExport();
            YearPlanDetail yearPlanDetail = yearPlanDetailMapper.selectById(m.getMrYpdId());
            YearPlan yearPlan = yearPlanMapper.selectById(yearPlanDetail.getYpdYpId());
            if (yearPlan!=null) {

                mExport.setYear(yearPlan.getYpYear());
                mExport.setMrYpdId(m.getMrYpdId());
                mExport.setMrDescription(m.getMrDescription());
                mExport.setMrFinishInvestment(m.getMrFinishInvestment());
                mExport.setMrNextPlan(m.getMrNextPlan());
                mExport.setMrNeedCoordinate(m.getMrNeedCoordinate());
                mExport.setMrNeedCoordinateShow(m.getMrNeedCoordinateShow());
                return mExport;
            }else
                return null;
        }).filter(x->x!=null).collect(Collectors.toList());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();


        List<MonthReportExport> exportList = new ArrayList<>();

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            pageList.stream().filter(item -> selectionList.contains(item.getId().toString())).forEach(record->exportList.add(record));
        } else {
            pageList.stream().forEach(record->exportList.add(record));

        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        if (sysUser!=null)
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + sysUser.getRealname()+"    金额单位：万元", title));
        else
            mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "金额单位：万元", title));
//        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "", title));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }


    @Override
    public Page listMonthReportByPrjId(Long prjId, Page<JSONObject> page) {
            Project project = projectMapper.selectById(prjId);
        List<YearPlan> yearPlans = yearPlanMapper.selectList(new QueryWrapper<YearPlan>().eq("YP_PRJ_ID", prjId));
        Map<Long, String> yearMaps = new HashMap<>();
        for (YearPlan yearPlan : yearPlans) {
            yearMaps.put(yearPlan.getId(), yearPlan.getYpYear());
        }

       List<JSONObject> array = new ArrayList<>();
        for (Map.Entry<Long, String> entry : yearMaps.entrySet()) {
            List<JSONObject> ids = yearPlanDetailMapper.selectList(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", entry.getKey())).stream().map(yearPlanDetail -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("year", entry.getValue());
                jsonObject.put("month", yearPlanDetail.getYpdMonth());
                jsonObject.put("id", yearPlanDetail.getId());
                return jsonObject;
            }).collect(Collectors.toList());
            ids.forEach(item -> {
                array.add(item);
            });
        }
        ;
        List<JSONObject> res = new ArrayList<>();
        for (JSONObject jobj : array) {
            List<MonthReport> list = monthReportMapper.selectList(new QueryWrapper<MonthReport>().eq("MR_YPD_ID", jobj.getString("id")));
            list.forEach(monthReport -> {
                JSONObject monthRe = (JSONObject) JSONObject.toJSON(monthReport);
                monthRe.put("year", jobj.getString("year"));
                monthRe.put("month", jobj.getString("month"));
                monthRe.put("projectName", project.getPrjName());
                res.add(monthRe);
            });
        }
        res.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject jsonObject, JSONObject t1) {
                Integer year = jsonObject.getInteger("year");
                Integer year1 = t1.getInteger("year");
                Integer m1 = jsonObject.getInteger("month");
                Integer m2 =  t1.getInteger("month");
                if (year<year1)
                    return 1;
                else if(year>year1)
                    return -1;
                else{
                    if(m1<m2)
                        return 1;
                    else return -1;
                }
            }
        });
        page.setRecords(res);
        return page;
    }

    @Override
    public JSONObject getLastMonthData(Long prjId, String year, String month) {

        //根据prjId,year获取到该年的年度考核计划
        YearPlan thisYearPlan = yearPlanMapper.selectOne(new QueryWrapper<YearPlan>().eq("YP_PRJ_ID", prjId).eq("YP_YEAR", year));
        //根据prjId获取该项目的年度考核计划yearPlan
        List<YearPlan> yearPlanList = yearPlanService.selectByMainId(prjId);

        JSONObject jsonObject = new JSONObject();
        YearPlanDetail nextDetail =null;
        QueryWrapper<YearPlanDetail> queryWrapper = new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", thisYearPlan.getId()).eq("YPD_MONTH", month);
        YearPlanDetail thisPlanDetail = yearPlanDetailMapper.selectOne(queryWrapper);
        MonthReport monthReport = monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID",thisPlanDetail.getId()));
        BigDecimal t1= getYearInFactInvestment(thisYearPlan.getId().toString(),month);
        BigDecimal t2 = getInFactInvestment(yearPlanList,year,month,thisYearPlan.getId().toString());
        //判断该项目的年度计划是否跨年,等于1是不跨年的
        if (yearPlanList.size() == 1) {
            //判断请求的月份是否是1月份，因为不跨年的年度计划1月份是没有当月的投资计划的
            if (month.equals("1")) {
                jsonObject = getJanResult(thisYearPlan, month);
            } else {
                if (!month.equals("12")) {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", thisYearPlan.getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                }
                return getResult(thisPlanDetail, year, year, nextDetail,t1,t2);
            }
        }
        //跨年项目
        else {

            yearPlanList.sort(new Comparator<YearPlan>() {
                @Override
                public int compare(YearPlan t0, YearPlan t1) {
                    Integer int0 = Integer.parseInt(t0.getYpYear());
                    Integer int1 = Integer.parseInt(t1.getYpYear());
                    return int0.compareTo(int1);
                }
            });
            int index = yearPlanList.indexOf(thisYearPlan);
            //判断是否是该年度计划的第一年
            if (yearPlanList.get(0).getYpYear().equals(year)) {
                if (month.equals("1")) {
                    jsonObject = getJanResult(thisYearPlan, month);
                } else if (!month.equals("12")) {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", thisYearPlan.getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    return getResult(thisPlanDetail, year, year, nextDetail,t1,t2);

                } else {

                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(1).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    return getResult(thisPlanDetail, year, yearPlanList.get(1).getYpYear(),nextDetail,t1,t2);
                }

            }
            //判断是否是最后一年(不是)
            else if (!yearPlanList.get(yearPlanList.size() - 1).getYpYear().equals(year)) {

                //获取上一个年度计划的12月份的信息
                if (month.equals("1")) {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(index-1).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    YearPlan yearPlan = yearPlanList.get(index - 1);
                    return getResult(thisPlanDetail, yearPlan.getYpYear(), year, nextDetail,t1,t2);

                } else if (month.equals("12")) {

                    YearPlan nextYearPlan = yearPlanList.get(index + 1);
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(index + 1).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    return getResult(thisPlanDetail, year, nextYearPlan.getYpYear(), nextDetail,t1,t2);
                } else {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(index).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    return getResult(thisPlanDetail, year, year,  nextDetail,t1,t2);
                }
            }
            //是
            else {
                //获取上一个年度计划的12月份的信息
                if (month.equals("1")) {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(index).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    YearPlan yearPlan = yearPlanList.get(index - 1);

                    return getResult(thisPlanDetail, yearPlan.getYpYear(), year, nextDetail,t1,t2);
                } else if (month.equals("12")) {
                    return getResult(thisPlanDetail, year, year, nextDetail,t1,t2);
                } else {
                    nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlanList.get(index).getId()).eq("YPD_MONTH", MonthUtil.getNextMonth(month)));
                    return getResult(thisPlanDetail, year, year,nextDetail,t1,t2);
                }
            }
        }

        return jsonObject;

    }
    public JSONObject getResult(YearPlanDetail thisPlanDetail, String thisYear, String nextYear, YearPlanDetail nextDetail,BigDecimal t1,BigDecimal t2) {
            if (nextDetail == null) {
                if (thisPlanDetail != null)
                    return newLastMonthData(thisYear, nextYear, "2", thisPlanDetail.getYpdInvestment(), thisPlanDetail.getYpdFinance(),  null, null, null,thisPlanDetail.getYpdPlan(),null,t1,t2);
                else
                    return newLastMonthData(thisYear, nextYear, "2", null, null,  null, null, null,null,null,t1,t2);

            } else {
                if (thisPlanDetail != null)
                    return newLastMonthData(thisYear, nextYear, "2", thisPlanDetail.getYpdInvestment(), thisPlanDetail.getYpdFinance(),  nextDetail.getYpdInvestment(), nextDetail.getYpdFinance(), nextDetail.getId().toString(),thisPlanDetail.getYpdPlan(),nextDetail.getYpdPlan(),t1,t2);
                else
                    return newLastMonthData(thisYear, nextYear, "2", null, null,  nextDetail.getYpdInvestment(), nextDetail.getYpdFinance(), nextDetail.getId().toString(),null,nextDetail.getYpdPlan(),t1,t2);

            }


    }

    public JSONObject getJanResult(YearPlan yearPlan, String month) {
        BigDecimal total = getYearInFactInvestment(yearPlan.getYpYear(),month);
        YearPlanDetail thisDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlan.getId()).eq("YPD_MONTH", "1"));
        YearPlanDetail nextDetail = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", yearPlan.getId()).eq("YPD_MONTH", MonthUtil.getNextMonth("1")));
        return newLastMonthData(yearPlan.getYpYear(), yearPlan.getYpYear(), "1", thisDetail.getYpdInvestment(), thisDetail.getYpdFinance(), nextDetail.getYpdInvestment(), nextDetail.getYpdFinance(), nextDetail.getId().toString(),thisDetail.getYpdPlan(),nextDetail.getYpdPlan(),total,total);
    }
    public JSONObject newLastMonthData(String lastYear, String nextYear, String status, String lastInvestment, String lastFinance,  String nextInvestment, String nextFinance, String yearPlanDetailId,String thisPlan,String nextPlan,BigDecimal total,BigDecimal total2) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("status", status);
        jsonObject.put("lastYear", lastYear);
        jsonObject.put("thisInvestment", lastInvestment);
        jsonObject.put("thisFinance", lastFinance);
        jsonObject.put("nextYear", nextYear);
        jsonObject.put("nextInvestment", nextInvestment);
        jsonObject.put("nextFinance", nextFinance);
        jsonObject.put("yearPlanDetailId", yearPlanDetailId);
        jsonObject.put("thisPlan", thisPlan);
        jsonObject.put("nextPlan", nextPlan);
        jsonObject.put("t1",total);
        jsonObject.put("t2",total2);
        return jsonObject;
    }

    //根据年份id和月份得到该年累计至今完成投资额
    public BigDecimal getYearInFactInvestment(String id,String month){
        BigDecimal total = new BigDecimal("0");
        Integer mon = Integer.parseInt(month);
        List<YearPlanDetail> thisPlanDetails =yearPlanDetailMapper.selectList(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", id));
        int i = 1;
            while (i<mon){
                for (YearPlanDetail x:thisPlanDetails){
                    if(Integer.parseInt(x.getYpdMonth())==i){
                        MonthReport monthReport =  monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID",x.getId()));
                        if (monthReport!=null){
                            BigDecimal now = new BigDecimal(monthReport.getMrFinishInvestment());
                            total=total.add(now);
                        }

                    }
                }
                i++;

            }
            return total;
    }

    //项目累计值当前月份的完成投资金额
    public BigDecimal getInFactInvestment(List<YearPlan> yearPlanList,String year,String month,String id){
        BigDecimal total = new BigDecimal("0");
        Integer yearNum = Integer.parseInt(year);
        for (YearPlan x:yearPlanList){
            if(Integer.parseInt(x.getYpYear())<yearNum){
                List<YearPlanDetail> details = yearPlanDetailMapper.selectList(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID",x.getId()));
                for (YearPlanDetail detail:details){
                    MonthReport monthReport = monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID",detail.getId()));
                    if (monthReport!=null){
                        BigDecimal now = new BigDecimal(monthReport.getMrFinishInvestment());
                        total=total.add(now);
                    }
                }
            }
        };


        List<YearPlanDetail> thisPlanDetails =yearPlanDetailMapper.selectList(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID", id));
        Integer mon = Integer.parseInt(month);
        int i = 1;
        while (i<mon){
            for (YearPlanDetail x:thisPlanDetails){
                if(Integer.parseInt(x.getYpdMonth())==i){
                    MonthReport monthReport =  monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID",x.getId()));
                    if (monthReport!=null){
                        BigDecimal now = new BigDecimal(monthReport.getMrFinishInvestment());
                        total=total.add(now);
                    }

                }
            }
            i++;

        }
        return total;
    }

    @Override
    public Project getProjectByMrYpdId(Long mrYpdId) {
        YearPlanDetail yearPlanDetail = yearPlanDetailMapper.selectById(mrYpdId);
        YearPlan yearPlan = yearPlanMapper.selectById(yearPlanDetail.getYpdYpId());
        Project project = projectMapper.selectById(yearPlan.getYpPrjId());
        return project;
    }




/*    @Override
    public boolean updateMonthReport(MonthReport monthReport) {
        return monthReportMapper.updateMonthReport(monthReport);
    }*/
@Override
public Result<?> addMonthReport(JSONObject jsonObject) {
    String yearId = jsonObject.getString("yearId");
    String month = jsonObject.getString("month");
    Long detailId = monthReportMapper.getYearPlanDetailId(yearId, month);
    if (detailId == null) {
        return Result.ok("请先添加该月的投资计划！");
    } else {
        MonthReport vailate = monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID", detailId));
        if (vailate == null) {
            MonthReport monthReport = JSON.toJavaObject(jsonObject, MonthReport.class);
            monthReport.setMrYpdId(detailId);
            this.save(monthReport);
            return Result.ok("添加成功");
        } else {

            vailate.setMrFinishInvestment(jsonObject.getString("mrFinishInvestment"));
            vailate.setMrDescription(jsonObject.getString("mrDescription"));
            vailate.setMrNeedCoordinate(jsonObject.getString("mrNeedCoordinate"));
            vailate.setMrNextPlan(jsonObject.getString("nextYear"));
            vailate.setMrNeedCoordinateShow(jsonObject.getString("mrNeedCoordinateShow"));
            this.updateById(vailate);
            return Result.ok("已成功更新该月份的月报信息");
        }

    }


}
}
