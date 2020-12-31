package org.jeecg.modules.monthreport.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.monthreport.entity.MonthReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 进度月报
 * @Author: jeecg-boot
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MonthReportMapper extends BaseMapper<MonthReport> {
    @Select(value = "select ID from PJPLAT.BS_YEARPLAN_DETAIL where YPD_YP_ID in (select ID from PJPLAT.BS_YEAR_PLAN where ID=#{yearId}) and YPD_MONTH=#{month}")
    Long getYearPlanDetailId(String yearId,String month);

}
