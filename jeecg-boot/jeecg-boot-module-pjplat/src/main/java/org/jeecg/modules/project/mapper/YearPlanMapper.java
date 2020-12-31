package org.jeecg.modules.project.mapper;

import java.util.List;
import org.jeecg.modules.project.entity.YearPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 年度考核计划
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
public interface YearPlanMapper extends BaseMapper<YearPlan> {

	public boolean deleteByMainId(@Param("mainId") Long mainId);
    
	public List<YearPlan> selectByMainId(@Param("mainId") Long mainId);

}
