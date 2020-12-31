package org.jeecg.modules.helmet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.helmet.entity.HelmetInfo;
import org.jeecg.modules.helmet.entity.HelmetList;
import org.jeecg.modules.helmet.entity.UserHelmet;

public interface HelmetInfoMapper extends BaseMapper<HelmetInfo> {
    //根据序列号更新电量
    @Update(value = "update pjplat.bs_helmet_info set BATTERY_STATUS=#{power},WEAR_STATUS=#{wearStatus} ,LON=#{lon},LAT=#{lat} where DEVICE_SERIAL=#{deviceSerial}")
    public void updatePower(String deviceSerial,int power,boolean wearStatus,String lon,String lat);

    //更新数据能力集
    @Update(value = "update pjplat.bs_helmet_info set ELIMINATING_JITTER_TIME_MAX=#{elMax},ELIMINATING_JITTER_TIME_MIN=#{elMin},HIT_SENSITIVITY_MAX=#{hitMax},HIT_SENSITIVITY_MIN=#{hitMin},TAKEOFF_REPORT_INTERVAL_MIN=#{takeMin}," +
        "TAKEOFF_REPORT_INTERVAL_MAX=#{takeMax},GPS_REPORT_INTERVAL_MAX=#{GPSMax},GPS_REPORT_INTERVAL_MIN=#{GPSMin},WEAR_REPORT_INTERVAL_MIN=#{wearMin},WEAR_REPORT_INTERVAL_MAX=#{wearMax},ELIMINATING_JITTER_TIME_MAX=#{abnormalMax},ELIMINATING_JITTER_TIME_MIN=#{abnormalMin} where DEVICE_SERIAL=#{deviceSerial}")
    public void updateCapabilities(String deviceSerial, Integer takeMax,Integer takeMin,Integer wearMax,Integer wearMin,Integer GPSMax,Integer GPSMin,Integer hitMax,Integer hitMin,Integer elMax,Integer elMin,Integer abnormalMax,Integer abnormalMin);


    //更新数据能力集
    public void updateCapability(String deviceSerial, Integer take,Integer wear,Integer GPS,Integer hit,Integer el,Integer abnormal);

}
