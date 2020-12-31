package org.jeecg.modules.yearplandetail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.yearplandetail.entity.MonthPlan;
import org.jeecg.modules.yearplandetail.entity.YearPlanDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 月度计划
 * @Author: jeecg-boot
 * @Date:   2020-10-26
 * @Version: V1.0
 */
public interface YearPlanDetailMapper extends BaseMapper<YearPlanDetail> {

    List<YearPlanDetail> getMonthPlanList(Long yearId);

    @Select(value = "select YP_YEAR from PJPLAT.BS_YEAR_PLAN where ID = (select YPD_YP_ID from PJPLAT.BS_YEARPLAN_DETAIL where ID =#{id} ) ")
    Integer getYear(Long id);



}
