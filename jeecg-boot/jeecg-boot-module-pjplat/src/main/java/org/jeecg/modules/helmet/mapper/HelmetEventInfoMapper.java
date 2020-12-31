package org.jeecg.modules.helmet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.jeecg.modules.helmet.entity.HelmetEventInfo;
import org.jeecg.modules.helmet.entity.HelmetHeartbeat;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.KeyValueValue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HelmetEventInfoMapper extends BaseMapper<HelmetEventInfo> {

    public List<KeyValueValue<String, BigDecimal,String>> getCount(String prjId, String startDate, String endDate, String startTime, String endTime);
}
