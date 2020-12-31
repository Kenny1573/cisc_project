package org.jeecg.modules.problemsheet.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.problemsheet.entity.BsProblemSheet;
import org.jeecg.modules.problemsheet.entity.ProblemDeal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 检查整改单
 * @Author: jeecg-boot
 * @Date:   2020-11-04
 * @Version: V1.0
 */
public interface IBsProblemSheetService extends IService<BsProblemSheet> {
	
	/**
	 * 通过talename、sheetid
	 * @param sheetid
	 * @param stype
	 * @return
	 */
	public List<ProblemDeal> getAvmessagebyid(String sheetid,String stype);

}
