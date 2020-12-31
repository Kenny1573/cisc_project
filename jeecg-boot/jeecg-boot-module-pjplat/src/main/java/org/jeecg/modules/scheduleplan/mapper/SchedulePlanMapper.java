package org.jeecg.modules.scheduleplan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.scheduleplan.entity.SchedulePlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 里程碑计划
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface SchedulePlanMapper extends BaseMapper<SchedulePlan> {

    @Select(value = "select * from pjplat.bs_schedule_plan where SP_PRJ_ID=#{prjId}")
    public List<SchedulePlan> getInitMenu(Long prjId);



}
