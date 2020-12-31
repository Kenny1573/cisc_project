package org.jeecg.modules.problemsheet.service.impl;

import java.util.List;

import org.jeecg.modules.problemsheet.entity.BsProblemSheet;
import org.jeecg.modules.problemsheet.entity.ProblemDeal;
import org.jeecg.modules.problemsheet.mapper.BsProblemSheetMapper;
import org.jeecg.modules.problemsheet.service.IBsProblemSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 检查整改单
 * @Author: jeecg-boot
 * @Date:   2020-11-04
 * @Version: V1.0
 */
@Service
public class BsProblemSheetServiceImpl extends ServiceImpl<BsProblemSheetMapper, BsProblemSheet> implements IBsProblemSheetService {

	@Autowired
	BsProblemSheetMapper bpsmapper;

	@Override
	public List<ProblemDeal> getAvmessagebyid(String sheetid, String stype) {
		// TODO 自动生成的方法存根
		
		List<ProblemDeal> tmp = bpsmapper.getAvmessagebyid(sheetid, stype);
		
		if(tmp != null && !tmp.isEmpty()) {
			
			tmp.forEach(item->{
				
				if(item.getAvmeg() == null) {
					item.setAvmeg("");
				}
				
				if(item.getDeakey() == null) {
					item.setDeakey("");
				}
				
				if(item.getDealsj() == null) {
					item.setDealsj("");
				}
				
				if(item.getDes() == null) {
					item.setDes("");
				}
				
				if(item.getTaskid() == null ) {
					item.setTaskid("");
				}
				
				if(item.getType() == null) {
					item.setType("");
				}
				
				if(item.getUname() == null) {
					item.setUname("");
				}
				
			});
		}
		
		return tmp;
	}

}
