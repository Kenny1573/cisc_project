package org.jeecg.modules.scheduleplan.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.scheduleplan.entity.SchedulePlan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.scheduleplan.entity.ModalTree;

import java.util.List;

/**
 * @Description: 里程碑计划
 * @Author: jeecg-boot
 * @Date:   2020-11-06
 * @Version: V1.0
 */
public interface ISchedulePlanService extends IService<SchedulePlan> {
    public List<ModalTree> getMenu(Long prjId);
    public JSONObject getGantt(Long prjId);

}
