package org.jeecg.modules.helmet.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.jeecg.common.util.RestUtil;
import org.jeecg.modules.helmet.entity.HelmetInfo;
import org.jeecg.modules.helmet.entity.HelmetList;
import org.jeecg.modules.helmet.entity.SafetyHelmetCfg;
import org.jeecg.modules.helmet.mapper.HelmetInfoMapper;
import org.jeecg.modules.helmet.mapper.UserHelmetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Component
public class DeviceTask {

    Logger logger = LoggerFactory.getLogger(DeviceTask.class);
    @Value(value = "${appKey}")
    private String appKey;
    @Value(value = "${appSecret}")
    private String appSecret;
    @Autowired
    RestTemplate restTemplate;

    private String token;

    @Autowired
    private HelmetInfoMapper helmetInfoMapper;
    @Autowired
    private UserHelmetMapper userHelmetMapper;

    //@Scheduled(fixedDelay = 3*60*1000)
    //获取安全帽的token
    public void getToken() {
        String url = "https://open.ys7.com/api/lapp/token/get";
        JSONObject headers = new JSONObject();
        headers.put("appKey", appKey);
        headers.put("appSecret", appSecret);
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:");
        ResponseEntity response = RestUtil.request(url, HttpMethod.POST, headers, null);
        JSONObject res = (JSONObject) response.getBody();
        logger.info("response: " + res.toString());
        res = res.getJSONObject("data");
        this.token= res.getString("accessToken");
    }

    //@Scheduled(fixedDelay = 1*60*1000)    //获取安全帽的列表
    public void updateList() {
        List<HelmetList> helmets = new ArrayList<>();
        try {
            String url = "https://open.ys7.com/api/lapp/device/list";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("accessToken", this.token);
            body.add("pageStart", 0);
            body.add("pageSize", 10);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
            logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:" + body.toString());
            ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            logger.info("response: " + res.toString());
            JSONArray jsonArray = res.getBody().getJSONArray("data");
            if(jsonArray==null)
                return;
            helmets = jsonArray.toJavaList(HelmetList.class);
            helmets.forEach(item->{
                JSONObject capabilities=configHelmetInfo(item.getDeviceSerial());
                HelmetInfo helmet = helmetInfoMapper.selectOne(new QueryWrapper<HelmetInfo>().eq("DEVICE_SERIAL",item.getDeviceSerial()));
                if (helmet!=null){
                    //存在即是以前添加过数据库的安全帽，只需要进行更新操作
                    helmet.setStatus(item.getStatus());
                    helmetInfoMapper.update(helmet,new QueryWrapper<HelmetInfo>().eq("DEVICE_SERIAL",item.getDeviceSerial()));
                }else{
                    //不存在则在数据库新增该安全帽的记录
                    helmet=new HelmetInfo();
                    helmet.setStatus(item.getStatus());
                    helmet.setDeviceSerial(item.getDeviceSerial());
                    helmetInfoMapper.insert(helmet);
                }

                helmetInfoMapper.updateCapabilities(item.getDeviceSerial(),capabilities.getJSONObject("takeoffReportInterval").getInteger("@max"),capabilities.getJSONObject("takeoffReportInterval").getInteger("@min"),
                        capabilities.getJSONObject("wearReportInterval").getInteger("@max"),capabilities.getJSONObject("wearReportInterval").getInteger("@min"),
                        capabilities.getJSONObject("GPSReportInterval").getInteger("@max"),capabilities.getJSONObject("GPSReportInterval").getInteger("@min"),
                        capabilities.getJSONObject("hitSensitivity").getInteger("@max"),capabilities.getJSONObject("hitSensitivity").getInteger("@min"),
                        capabilities.getJSONObject("eliminatingJitterTime").getInteger("@max"),capabilities.getJSONObject("eliminatingJitterTime").getInteger("@min"),
                        capabilities.getJSONObject("abnormalRestTime").getInteger("@max"),capabilities.getJSONObject("abnormalRestTime").getInteger("@min")
                        );
                SafetyHelmetCfg safe=getConfigInfo(item.getDeviceSerial());
                helmetInfoMapper.updateCapability(item.getDeviceSerial(),safe.getTakeoffReportInterval(),safe.getWearReportInterval(),safe.getGPSReportInterval(),safe.getHitSensitivity(),safe.getEliminatingJitterTime(),safe.getAbnormalRestTime());
            });
        } catch (Exception e) {
            logger.error("请求萤石接口网络或接口参数错误,数据从系统数据库调用");
            e.printStackTrace();
        }
    }






    //获取配置能力(min,max)信息
    public JSONObject configHelmetInfo(String deviceSerial) {

        String url = "https://open.ys7.com/api/hikvision/ISAPI/IoTGateway/safetyHelmetCfg/capabilities";
        JSONObject param = new JSONObject();
        param.put("format","json");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("EZO-AccessToken", this.token);
        headers.set("EZO-DeviceSerial", deviceSerial);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        headers.set("EZO-Date", df.format(date));
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:");
        ResponseEntity<String> response =RestUtil.request(url, HttpMethod.GET, headers, param,null,String.class);
        logger.info("response: " + response.toString());
        JSONObject object = JSON.parseObject(response.getBody()) ;
        return object;


    }

    //配置读取
    public SafetyHelmetCfg getConfigInfo(String deviceSerial) {

        //创建消费者Id
        String url = "https://open.ys7.com/hikvision/ISAPI/IoTGateway/safetyHelmetCfg?format=json";
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("EZO-AccessToken", this.token);
        headers.put("EZO-DeviceSerial", deviceSerial);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        headers.put("EZO-Date", df.format(date));
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:");
        ResponseEntity response = RestUtil.request(url, HttpMethod.GET, headers, null);
        logger.info("response: " + response.toString());
        JSONObject object = (JSONObject) response.getBody();
        SafetyHelmetCfg safetyHelmetCfg = object.getObject("SafetyHelmetCfg", SafetyHelmetCfg.class);
        return safetyHelmetCfg;


    }


}
