package org.jeecg.modules.yearplandetail.service.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.monthreport.entity.MonthReport;
import org.jeecg.modules.monthreport.mapper.MonthReportMapper;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.mapper.YearPlanMapper;
import org.jeecg.modules.project.service.IYearPlanService;
import org.jeecg.modules.utils.MonthUtil;
import org.jeecg.modules.yearplandetail.entity.MonthPlan;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.mapper.YearPlanDetailMapper;
import org.jeecg.modules.yearplandetail.service.IYearPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Description: 月度计划
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
@Service
public class YearPlanDetailServiceImpl extends ServiceImpl<YearPlanDetailMapper, YearPlanDetail> implements IYearPlanDetailService {
    //年度考核计划的Service
    @Autowired
    IYearPlanService yearPlanService;
    @Autowired
    YearPlanMapper yearPlanMapper;
    //精确到月份的投资计划的Mapper
    @Autowired
    YearPlanDetailMapper yearPlanDetailMapper;

    @Autowired
    MonthReportMapper monthReportMapper;




    //由其前端传递的具体年份字串eg·2020 和项目id来获取该项目的该年的年度计划Id然后根据yearPlanDetail的id来判断数据库中是否已有该记录，有则更新，无则添加
    @Override
    public boolean addOrUpdateMonthPlanList(JSONObject jsonObject) {
        String year =jsonObject.getString("ypdYear");
        String prjId=jsonObject.getString("prjId");
        Long yearId=yearPlanService.getOne(new QueryWrapper<YearPlan>().eq("YP_YEAR",year).eq("YP_PRJ_ID",prjId)).getId();
        YearPlanDetail yearPlanDetail = new YearPlanDetail(jsonObject.getLong("id"),yearId, jsonObject.getString("ypdMonth"),jsonObject.getString("ypdInvestment"),jsonObject.getString("ypdFinance"),jsonObject.getString("ypdPlan"));
        YearPlanDetail flag = yearPlanDetailMapper.selectOne(new QueryWrapper<YearPlanDetail>().eq("YPD_YP_ID",yearId).eq("YPD_MONTH",jsonObject.getString("ypdMonth")));
        //yearPlanDetail.getId()!=null说明数据库此前已有该月的投资计划，执行更新操作
        if (flag!=null){
            yearPlanDetail.setId(flag.getId());
            yearPlanDetailMapper.update(yearPlanDetail,new QueryWrapper<YearPlanDetail>().eq("ID",yearPlanDetail.getId()));
        }
        //没有Id说明此前数据库没有该月份的数据需要新增该条记录
        else{
            yearPlanDetailMapper.insert(yearPlanDetail);
        }
        return true;
    }

    /*
    * 返回json示例
    *[{
            "year": "2020",
            "investment": {
                "Jul": null,
                "Oct": null,
                "Feb": "234",
                "Apr": null,
                "Jun": null,
                "Dec": null,
                "May": null,
                "Aug": null,
                "total": 468,
                "Nov": null,
                "Jan": "234",
                "Mar": null,
                "Sep": null
            },
            "id": {
                "Jul": null,
                "Oct": null,
                "Feb": 87,
                "Aug": null,
                "Apr": null,
                "Jun": null,
                "Dec": null,
                "Nov": null,
                "May": null,
                "Jan": 85,
                "Mar": null,
                "Sep": null
            },
            "finance": {
                "Jul": null,
                "Oct": null,
                "Feb": "23",
                "Apr": null,
                "Jun": null,
                "Dec": null,
                "May": null,
                "Aug": null,
                "total": 57,
                "Nov": null,
                "Jan": "34",
                "Mar": null,
                "Sep": null
            },
            "yearId": 102
        }]
    * */
    //选择项目id然后获取所有的monthPlan
    @Override
    public JSONArray getMonthPlanList(Long prjId) {
        List<YearPlan> yearPlanList= yearPlanService.selectByMainId(prjId);
        JSONArray jsonArray = new JSONArray();
        //遍历根据prjId获取的该项目的年度考核计划
        yearPlanList.forEach(item->{
            //jsonObject用来配合前端完成定制jsonArray的实例元素
            JSONObject jsonObject = new JSONObject();
            //此实体保存了年份以及该年1月到12月的信息
            List<YearPlanDetail> yearPlanDetails=yearPlanDetailMapper.getMonthPlanList(item.getId());
            MonthPlan monthPlan = new MonthPlan(item.getYpYear(),yearPlanDetails);
            jsonObject.put("year",monthPlan.getYear());
            JSONObject jobjFinance=new JSONObject();
            JSONObject jobjInvestment=new JSONObject();
            JSONObject jobjId=new JSONObject();
            JSONObject jobjPlan= new JSONObject();
            Predicate<String> predicate=x->x!=null;
            BigDecimal financeTotal= new BigDecimal("0");
            BigDecimal investmentTotal= new BigDecimal("0");
            for (YearPlanDetail detail:monthPlan.getList()) {
                if(predicate.test(detail.getYpdFinance()))
                    financeTotal= financeTotal.add(new BigDecimal(detail.getYpdFinance()));
                jobjFinance.put(detail.getYpdMonth(),detail.getYpdFinance());
                if (predicate.test(detail.getYpdInvestment()))
                    investmentTotal=investmentTotal.add(new BigDecimal(detail.getYpdInvestment()));
                jobjInvestment.put(detail.getYpdMonth(),detail.getYpdInvestment());
                jobjId.put(detail.getYpdMonth(),detail.getId());
                jobjPlan.put(detail.getYpdMonth(),detail.getYpdPlan());
            };

            jobjFinance.put("total",financeTotal);
            jobjInvestment.put("total",investmentTotal);
            jsonObject.put("plan",jobjPlan);
            jsonObject.put("finance",jobjFinance);
            jsonObject.put("investment",jobjInvestment);
            jsonObject.put("id",jobjId);
            jsonObject.put("yearId",item.getId());
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }
    //计划账台列表，带有计划财务数和投资数的合计,used by ProjectController's method named listYearPlanWithFinanceAndInvestmentByMainId()
    @Override
    public JSONArray getTotalList(Long prjId) {
        List<YearPlan> yearPlanList= yearPlanService.selectByMainId(prjId);
        JSONArray jsonArray = new JSONArray();
        //遍历根据prjId获取的该项目的年度考核计划
        yearPlanList.forEach(item->{
            //jsonObject用来配合前端完成定制jsonArray的实例元素
            JSONObject jsonObject = new JSONObject();
            //此实体保存了年份以及该年1月到12月的信息
            List<YearPlanDetail> yearPlanDetails=yearPlanDetailMapper.getMonthPlanList(item.getId());
            MonthPlan monthPlan = new MonthPlan(item.getYpYear(),yearPlanDetails);
            jsonObject.put("year",monthPlan.getYear());
            Predicate<String> predicate=x->x!=null;
            BigDecimal financeTotal= new BigDecimal("0");
            BigDecimal investmentTotal= new BigDecimal("0");
            for (YearPlanDetail detail:monthPlan.getList()) {
                if(predicate.test(detail.getYpdFinance()))
                    financeTotal= financeTotal.add(new BigDecimal(detail.getYpdFinance()));
                if (predicate.test(detail.getYpdInvestment()))
                    investmentTotal=investmentTotal.add(new BigDecimal(detail.getYpdInvestment()));
            };
            jsonObject.put("finance",financeTotal);
            jsonObject.put("investment",investmentTotal);
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }


    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("0");
        BigDecimal  b= new BigDecimal("1");
        a=a.add(b);
        System.out.println(a);
    }

}
