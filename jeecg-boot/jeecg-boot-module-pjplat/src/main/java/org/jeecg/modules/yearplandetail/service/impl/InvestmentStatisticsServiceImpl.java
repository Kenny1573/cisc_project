package org.jeecg.modules.yearplandetail.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.jeecg.modules.monthreport.entity.MonthReport;
import org.jeecg.modules.monthreport.mapper.MonthReportMapper;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.service.IYearPlanService;
import org.jeecg.modules.utils.MonthUtil;
import org.jeecg.modules.yearplandetail.entity.InvestmentStatistics;
import org.jeecg.modules.yearplandetail.entity.MonthPlan;
import org.jeecg.modules.yearplandetail.entity.TotalInvestment;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import org.jeecg.modules.yearplandetail.mapper.YearPlanDetailMapper;
import org.jeecg.modules.yearplandetail.service.IYearPlanDetailService;
import org.jeecg.modules.yearplandetail.service.InvestmentStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class InvestmentStatisticsServiceImpl implements InvestmentStatisticsService {

    @Autowired
    IYearPlanDetailService yearPlanDetailService;
    @Autowired
    IYearPlanService yearPlanService;
    @Autowired
    YearPlanDetailMapper yearPlanDetailMapper;
    @Autowired
    MonthReportMapper monthReportMapper;

    @Override
    public TotalInvestment getList(Long yearPlanId) {
        List<YearPlanDetail> monthPlans = yearPlanDetailMapper.getMonthPlanList(yearPlanId);
        InvestmentStatistics total = new InvestmentStatistics();
        total.setMonth(0);
        Map<Integer,InvestmentStatistics> maps = new HashMap<>();
        Predicate<Long> predicate = x -> x == null;
        monthPlans.forEach(item -> {
            InvestmentStatistics investmentStatistics = new InvestmentStatistics();
            investmentStatistics.setMonth(Integer.parseInt(item.getYpdMonth()));
            Long detailId = item.getId();
            MonthReport report = new MonthReport();
            //还没有填写该月份的月度计划
            if (predicate.test(detailId)) {
                item.setYpdFinance("0");
                item.setYpdInvestment("0");
                report = new MonthReport("0", "0");
            }
            //填写了该月的月度计划
            else {
                report = monthReportMapper.selectOne(new QueryWrapper<MonthReport>().eq("MR_YPD_ID", item.getId()));
                //有月度计划但是还没有填月度进报
                if (report == null) {
                    report = new MonthReport("0", "0");
                }
            }
            investmentStatistics.setRealFinance(zeroReplaceNull(report.getMrFinishFinance()));
            investmentStatistics.setRealInvestment(zeroReplaceNull(report.getMrFinishInvestment()));
            investmentStatistics.setRealProcess(report.getMrDescription());
            investmentStatistics.setPlanFinance(zeroReplaceNull(item.getYpdFinance()));
            investmentStatistics.setPlanInvestment(zeroReplaceNull(item.getYpdInvestment()));
            investmentStatistics.setPlanProcess(item.getYpdPlan());
            BigDecimal planFinance=new BigDecimal(zeroReplaceNull(investmentStatistics.getPlanFinance()));
            BigDecimal totalPlanFinance=new BigDecimal(zeroReplaceNull(total.getPlanFinance()));

            total.setPlanFinance(totalPlanFinance.add(planFinance).toPlainString());
            BigDecimal planInvestment=new BigDecimal(zeroReplaceNull(investmentStatistics.getPlanInvestment()));
            BigDecimal totalPlanInvestment=new BigDecimal(zeroReplaceNull(total.getPlanInvestment()));
            total.setPlanInvestment(totalPlanInvestment.add(planInvestment).toPlainString());
            maps.put(investmentStatistics.getMonth(),investmentStatistics);
        });
        List<InvestmentStatistics> list =new ArrayList<>();
        maps.forEach((month, investmentStatistics) -> {
            Integer mon = month;
            //截至本月累计投资数/财务数
            BigDecimal totalNowMonthInvestment=new BigDecimal("0");
            BigDecimal totalNowMonthFinance=new BigDecimal("0");
            for(;mon>0;mon--){
                InvestmentStatistics temp = maps.get(mon);
                totalNowMonthInvestment=totalNowMonthInvestment.add(new BigDecimal(temp.getRealInvestment()));
                totalNowMonthFinance=totalNowMonthFinance.add(new BigDecimal(temp.getRealFinance()));
            }
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMaximumFractionDigits(2);
            investmentStatistics.setTotalInvestment(totalNowMonthInvestment.toPlainString());
            investmentStatistics.setTotalFinance(totalNowMonthFinance.toPlainString());
            String planInvestment=investmentStatistics.getPlanInvestment();
            String totalInvestment=total.getPlanInvestment();
            BigDecimal planB = new BigDecimal(planInvestment);
            BigDecimal totalB = new BigDecimal(totalInvestment);
            if(!totalInvestment.equals("0")) {
                investmentStatistics.setPerMonth(nf.format(planB.divide(totalB, 4, RoundingMode.HALF_UP)));
                BigDecimal totalIB= new BigDecimal (investmentStatistics.getTotalInvestment());
                investmentStatistics.setPerTotal(nf.format(totalIB.divide(totalB,4, RoundingMode.HALF_UP)));
            }else{
                investmentStatistics.setPerMonth("0");
                investmentStatistics.setPerTotal("0");

            }

            list.add(investmentStatistics);

        });



        InvestmentStatistics monthDec = maps.get(12);
        total.setRealInvestment(monthDec.getTotalInvestment());
        total.setRealFinance(monthDec.getTotalFinance());
        total.setTotalInvestment(monthDec.getTotalInvestment());
        total.setTotalFinance(monthDec.getTotalFinance());
        total.setPerMonth("100%");
        total.setPerTotal(monthDec.getPerTotal());
        return new TotalInvestment(total,list) ;
    }

    public String zeroReplaceNull(String s){
        if(s==null||s.equals(""))
            return "0";
        else
            return s;
    }
}
