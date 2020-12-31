package org.jeecg.modules.project.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.project.entity.YearPlan;
import org.jeecg.modules.project.mapper.YearPlanMapper;
import org.jeecg.modules.project.service.IYearPlanService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 年度考核计划
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Service
public class YearPlanServiceImpl extends ServiceImpl<YearPlanMapper, YearPlan> implements IYearPlanService {
	
	@Autowired
	private YearPlanMapper yearPlanMapper;
	
	@Override
	public List<YearPlan> selectByMainId(Long mainId) {
		return yearPlanMapper.selectByMainId(mainId);
	}
}
