package org.jeecg.modules.yearplandetail.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.yearplandetail.entity.MonthPlan;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 月度计划
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
public interface IYearPlanDetailService extends IService<YearPlanDetail> {
    JSONArray getMonthPlanList(Long prjId);
    JSONArray getTotalList(Long prjId);
    boolean addOrUpdateMonthPlanList(JSONObject jsonObject);

}
