package org.jeecg.modules.project.service;

import org.jeecg.modules.project.entity.YearPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 年度考核计划
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
public interface IYearPlanService extends IService<YearPlan> {

	public List<YearPlan> selectByMainId(Long mainId);
}
