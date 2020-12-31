package org.jeecg.modules.helmet.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RestUtil;
import org.jeecg.modules.helmet.entity.*;
import org.jeecg.modules.helmet.mapper.HelmetEventInfoMapper;
import org.jeecg.modules.helmet.mapper.HelmetHeartbeatMapper;
import org.jeecg.modules.helmet.mapper.HelmetInfoMapper;
import org.jeecg.modules.helmet.mapper.UserHelmetMapper;
import org.jeecg.modules.helmet.service.IHelmetService;
import org.jeecg.modules.helmet.task.DeviceTask;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.KeyValueValue;
import org.jeecg.modules.project.entity.SysUser;
import org.jeecg.modules.project.entity.UserProject;
import org.jeecg.modules.project.mapper.SysUserProjectMapper;
import org.jeecg.modules.project.mapper.UserProjectMapper;
import org.jeecg.modules.utils.MonthUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HelmetServiceImpl extends ServiceImpl<HelmetInfoMapper, HelmetInfo> implements IHelmetService {

    Logger logger = LoggerFactory.getLogger(HelmetServiceImpl.class);
    @Value(value = "${appKey}")
    private String appKey;
    @Value(value = "${appSecret}")
    private String appSecret;
    @Autowired
    RestTemplate restTemplate;


    @Autowired
    SysUserProjectMapper sysUserProjectMapper;
    @Autowired
    private HelmetInfoMapper helmetInfoMapper;
    @Autowired
    private UserHelmetMapper userHelmetMapper;
    @Autowired
    private HelmetHeartbeatMapper helmetHeartbeatMapper;
    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private HelmetEventInfoMapper helmetEventInfoMapper;

    @Autowired
    private DeviceTask deviceTask;

    //获取安全帽的token
    public String getToken() {
        String url = "https://open.ys7.com/api/lapp/token/get";
        JSONObject headers = new JSONObject();
        headers.put("appKey", appKey);
        headers.put("appSecret", appSecret);
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:");
        ResponseEntity response = RestUtil.request(url, HttpMethod.POST, headers, null);
        JSONObject res = (JSONObject) response.getBody();
        logger.info("response: " + res.toString());
        res = res.getJSONObject("data");
        return res.getString("accessToken");
    }

    //配置读取
    public SafetyHelmetCfg getConfigInfo(String deviceSerial) {

        //创建消费者Id
        String url = "https://open.ys7.com/hikvision/ISAPI/IoTGateway/safetyHelmetCfg?format=json";
        String token = getToken();
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("EZO-AccessToken", token);
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

    //配置下发
    @Override
    public Result<?> configHelmet(JSONObject safetyHelmetCfg) {

        String url = "https://open.ys7.com/hikvision/ISAPI/IoTGateway/safetyHelmetCfg?format=json";
        String token = getToken();
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("EZO-AccessToken", token);
        headers.put("EZO-DeviceSerial", safetyHelmetCfg.getString("deviceSerial"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        headers.put("EZO-Date", df.format(date));
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:" + safetyHelmetCfg.toString());
        ResponseEntity response = RestUtil.request(url, HttpMethod.PUT, headers, safetyHelmetCfg);
        logger.info("response: " + response.toString());
        JSONObject object = (JSONObject) response.getBody();
        if (object.getInteger("statusCode") == 1){
            helmetInfoMapper.updateCapability(safetyHelmetCfg.getString("deviceSerial"),null,null,safetyHelmetCfg.getInteger("gpsReportInterval"),safetyHelmetCfg.getInteger("hitSensitivity"),safetyHelmetCfg.getInteger("eliminatingJitterTime"),safetyHelmetCfg.getInteger("abnormalRestTime"));
            return Result.ok("修改成功");
        }else
            return Result.error("配置未成功");
    }

    //获取配置能力(min,max)信息
    public Result<?> configHelmetInfo(String deviceSerial) {
        String url = "https://open.ys7.com/hikvision/ISAPI/IoTGateway/safetyHelmetCfg/capabilities?format=json";
        String token = getToken();
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("EZO-AccessToken", token);
        headers.put("EZO-DeviceSerial", deviceSerial);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        headers.put("EZO-Date", df.format(date));
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:");
        ResponseEntity response = RestUtil.request(url, HttpMethod.GET, headers, null);
        logger.info("response: " + response.toString());
        JSONObject object = (JSONObject) response.getBody();
        return Result.ok(object.getJSONObject("SafetyHelmetCfg"));
    }



    //获取安全帽的列表
    public List<JSONObject> getList(Integer pageStart, Integer pageSize) {
        List<HelmetInfo> helmets = this.list(new QueryWrapper<HelmetInfo>().eq("DEL_FLAG","0"));
        List<JSONObject> res=helmets.stream().map(x->{
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(x));
            KeyValue<String ,String> keyValue = userHelmetMapper.getHelmetOwnerByDeviceSerial(x.getDeviceSerial());
            if (keyValue!=null){
                jsonObject.put("owner",keyValue.getValue());
                jsonObject.put("username",keyValue.getKey());
            }else{
                jsonObject.put("owner","未分配");
                jsonObject.put("username","");
            }
            return jsonObject;
        }).collect(Collectors.toList());
        return res;
    }
    //获取安全帽的列表
    public List<JSONObject> getLikeList(Integer pageStart, Integer pageSize,String deviceSerial) {
        deviceSerial=deviceSerial.replace("*","");
        List<HelmetInfo> helmets = this.list(new QueryWrapper<HelmetInfo>().eq("DEL_FLAG","0").like("DEVICE_SERIAL",deviceSerial));
        List<JSONObject> res=helmets.stream().map(x->{
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(x));
            KeyValue<String ,String> keyValue = userHelmetMapper.getHelmetOwnerByDeviceSerial(x.getDeviceSerial());
            if (keyValue!=null){
                jsonObject.put("owner",keyValue.getValue());
                jsonObject.put("username",keyValue.getKey());
            }else{
                jsonObject.put("owner","未分配");
                jsonObject.put("username","");
        }
            return jsonObject;
        }).collect(Collectors.toList());
        return res;
    }


    //用户绑定安全帽
    public Result<?> addMyHelmet(String deviceSerial, String validateCode, String username) {
        List<UserHelmet> userHelmets = userHelmetMapper.selectList(new QueryWrapper<UserHelmet>().eq("USERNAME", username));
        if (userHelmets.size() > 0) {
            return Result.error("已经拥有帽子啦，更换帽子请联系相关负责人员");
        }
        userHelmets = userHelmetMapper.selectList(new QueryWrapper<UserHelmet>().eq("DEVICE_SERIAL", deviceSerial));
        if (userHelmets.size() > 0) {
            return Result.error("帽子已经被其他用户绑定了，不能重复绑定");
        }
        try {
            String url = "https://open.ys7.com/api/lapp/device/add";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("accessToken", getToken());
            body.add("deviceSerial", deviceSerial);
            body.add("validateCode", validateCode);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
            logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:" + body.toString());
            ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            logger.info("response: " + res.toString());
            JSONObject jsonObject = res.getBody();
            Integer code = jsonObject.getInteger("code");
            //返回的code 200表示成功添加，20017表示设备之前已被自己添加,只需要在本地添加关联记录即可
            if (code == 200 || code == 20017) {
                deviceTask.updateList();
                userHelmetMapper.insert(new UserHelmet(null, username, deviceSerial));
                return Result.ok("绑定成功");
            } else {
                return Result.ok(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  Result.error("相关平台网络错误");
    }

    @Override
    public Result<?> getHelmetInfo(String username) {

        UserHelmet userHelmet = userHelmetMapper.selectOne(new QueryWrapper<UserHelmet>().eq("USERNAME", username));

        return Result.ok(helmetInfoMapper.selectOne(new QueryWrapper<HelmetInfo>().eq("DEVICE_SERIAL",userHelmet.getDeviceSerial()).eq("DEL_FLAG","0")));
    }

    @Override
    public Result<?> unlinkHelmet(String deviceSerial, String username) {

        userHelmetMapper.delete(new QueryWrapper<UserHelmet>().eq("USERNAME", username).eq("DEVICE_SERIAL", deviceSerial));

        return Result.ok("解绑成功");
    }

    @Override
    public Result<?> linkHelmet(String deviceSerial, String username) {
        List<UserHelmet> userHelmets = userHelmetMapper.selectList(new QueryWrapper<UserHelmet>().eq("DEVICE_SERIAL",deviceSerial));
        if (userHelmets.size()>0){
            return Result.error("该安全帽已经被使用,若是显示没有被使用，请刷新页面");
        }
        userHelmets = userHelmetMapper.selectList(new QueryWrapper<UserHelmet>().eq("USERNAME",username));
        if (userHelmets.size()>0){
            return Result.error("该用户已拥有安全帽，不能添加其他安全帽，请先解绑");
        }

        userHelmetMapper.insert(new UserHelmet(null, username, deviceSerial));

        return Result.ok("分配成功");
    }

    @Override
    public Result<?> dismissHelmet(String deviceSerial, String username) {

        try {
            String url = "https://open.ys7.com/api/lapp/device/delete";
            String token = getToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("accessToken", getToken());
            body.add("deviceSerial", deviceSerial);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
            logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:" + body.toString());
            ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
            logger.info("response: " + res.toString());
            return Result.ok(res.getBody());
        }catch(Exception e){
            e.printStackTrace();
        }
        unlinkHelmet(deviceSerial,username);
        helmetInfoMapper.delete(new QueryWrapper<HelmetInfo>().eq("DEVICE_SERIAL",deviceSerial));
        return Result.ok("删除成功");

    }

    @Override
    public Result<?> wearHelmetStatistics(String prjId, Date date1, Integer startTime, Integer endTime) {


        return null;
    }

    @Override
    public Result<?> errorStatistics(String prjId, String startDate, String endDate, Integer startTime, Integer endTime, String type) throws ParseException {
        Calendar calendar = new GregorianCalendar();

        startDate=startDate.replaceFirst("\"","");
        endDate=endDate.replaceFirst("\"","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date s = sdf.parse(startDate);
        Date e = sdf.parse(endDate) ;
        s.setHours(startTime);
        e.setHours(endTime);
        startDate=sdf2.format(s);
        endDate=sdf2.format(e);
        SimpleDateFormat hdf = new SimpleDateFormat("HH:mm:ss");

        List<KeyValueValue<String, BigDecimal,String>> keyValues = new ArrayList<>();
        Integer[] ydata = {0,0,0,0,0,0,0,0};

        switch (type){
            case "day":{
                String sTime = hdf.format(s);
                String eTime=hdf.format(e);
                keyValues=helmetEventInfoMapper.getCount(prjId,startDate,endDate,sTime,eTime);

            };break;
            case "month":{

                String sTime = hdf.format(s);
                String eTime=hdf.format(e);
                startDate= MonthUtil.getMonthFirstDay(startDate);
                endDate = MonthUtil.getMonthLastDay(endDate);
                keyValues=helmetEventInfoMapper.getCount(prjId,startDate,endDate,sTime,eTime);
            };break;
            case "week":{


                String sTime = hdf.format(s);
                String eTime=hdf.format(e);
                startDate=MonthUtil.getWeekMonDay(s);
                endDate=MonthUtil.getWeekSunDay(e);
                keyValues=helmetEventInfoMapper.getCount(prjId,startDate,endDate,sTime,eTime);

            };break;
            case "year":{
                s.setMonth(0);
                s.setDate(1);
                e.setMonth(11);
                e.setDate(31);
                startDate=sdf2.format(s);
                endDate=sdf2.format(e);
                String sTime = hdf.format(s);
                String eTime=hdf.format(e);
                keyValues=helmetEventInfoMapper.getCount(prjId,startDate,endDate,sTime,eTime);
            };break;
        }
            keyValues.forEach((x)->{
            switch (x.getKey()){

                //beaconId
                case "beaconId":ydata[2] =x.getValue().intValue();break;
                case "wearHelmetBehavior":{


                    if (Boolean.parseBoolean(x.getResult())){
                        //戴帽
                        ydata[1]=x.getValue().intValue();
                    }else {
                        //脱帽
                        ydata[0]=x.getValue().intValue();
                    }
                };break;
                case "isImpact":{
                    if (Boolean.parseBoolean(x.getResult())){
                        ydata[4]=x.getValue().intValue();
                    }
                };
                case "isAbnormalStationary":{
                    if (Boolean.parseBoolean(x.getResult())){
                        ydata[5]=x.getValue().intValue();
                    }
                };break;
                case "isSOS":{
                    if (Boolean.parseBoolean(x.getResult())){
                        ydata[3]=x.getValue().intValue();
                    }
                };
                case "workStatus":{
                    if (x.equals("on")){
                        //上班
                        ydata[6]=x.getValue().intValue();
                    }else if (x.equals("off")) {
                        //下班
                        ydata[7]=x.getValue().intValue();
                    }
                };break;
            }

        });
        return Result.ok(ydata);
    }


    //录入安全帽到公司token下
    public JSONObject addHelmet(String deviceSerial, String validateCode) {
        String url = "https://open.ys7.com/api/lapp/device/add";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("accessToken", getToken());
        body.add("deviceSerial", deviceSerial);
        body.add("validateCode", validateCode);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        logger.info("request: url:" + url + "   headers:" + headers.toString() + "  params:" + body.toString());
        ResponseEntity<JSONObject> res = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
        logger.info("response: " + res.toString());
        JSONObject jsonObject = res.getBody();
        return jsonObject;
    }


    //批量录入安全帽到公司平台下
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<HelmetExcelEntity> clazz) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<HelmetExcelEntity> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
                //update-begin-author:taoyan date:20190528 for:批量插入数据
                long start = System.currentTimeMillis();
                list.forEach(item -> {
                    this.addHelmet(item.getDeviceSerial(), item.getValidateCode());
                });
                //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
                //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
                logger.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
                //update-end-author:taoyan date:20190528 for:批量插入数据
                return Result.ok("文件导入成功！数据行数：" + list.size());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    public static void main(String[] args) {
        Date date = new Date();
        date.setHours(12);
        System.out.println(date.toString());
    }





}
