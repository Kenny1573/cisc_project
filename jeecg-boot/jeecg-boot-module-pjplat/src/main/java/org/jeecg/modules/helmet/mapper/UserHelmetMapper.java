package org.jeecg.modules.helmet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.helmet.entity.UserHelmet;
import org.jeecg.modules.project.entity.KeyValue;

public interface UserHelmetMapper extends BaseMapper<UserHelmet> {

    //根据序列号查找安全帽拥有者的realname
    public KeyValue<String,String> getHelmetOwnerByDeviceSerial(String deviceSerial);
}
