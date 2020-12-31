package org.jeecg.modules.problemsheet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.problemsheet.entity.BsProblemSheet;
import org.jeecg.modules.problemsheet.entity.ProblemDeal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 检查整改单
 * @Author: jeecg-boot
 * @Date:   2020-11-04
 * @Version: V1.0
 */
public interface BsProblemSheetMapper extends BaseMapper<BsProblemSheet> {
	
	/**
	 * 通过talename,sheetid,stype
	 * @param sheetid
	 * @param stype
	 * @return
	 */
	public List<ProblemDeal> getAvmessagebyid(@Param("sheetid") String sheetid,@Param("stype") String stype);

}
