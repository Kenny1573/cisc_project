package org.jeecg.modules.helmet.task;

import cn.hutool.core.io.resource.Resource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ys.product.ysmq.front.msg.AbstractConsumerMessage;
import com.ys.product.ysmq.front.msg.StandardConsumerMessage;
import org.apache.commons.io.IOUtils;
import org.jeecg.modules.helmet.entity.HelmetEventInfo;
import org.jeecg.modules.helmet.entity.HelmetHeartbeat;
import org.jeecg.modules.helmet.mapper.HelmetEventInfoMapper;
import org.jeecg.modules.helmet.mapper.HelmetHeartbeatMapper;
import org.jeecg.modules.helmet.mapper.HelmetInfoMapper;
import org.jeecg.modules.helmet.mapper.UserHelmetMapper;
import org.jeecg.modules.helmet.service.Impl.HelmetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class MessageQueueTask {
    Logger logger = LoggerFactory.getLogger(MessageQueueTask.class);
    private String  appKey="128187fbda8d46e59568b854ec661f20";
    private String appSecret="5cb76d47b25951dac1d90b9fa6073b10";

    @Autowired
    private HelmetInfoMapper helmetInfoMapper;
    @Autowired
    private UserHelmetMapper userHelmetMapper;
    @Autowired
    private HelmetHeartbeatMapper helmetHeartbeatMapper;
    @Autowired
    private HelmetEventInfoMapper helmetEventInfoMapper;

    private  String group = "group3";

    static int printListMaxSize = 100;

    // 异步处理消息的本地队列.
    private  BlockingQueue<List<Object>> msgQueue = new LinkedBlockingQueue<List<Object>>();

    public MessageQueueTask(){

        //this.subscribe();
    }

    public  void init() {
        // 开放平台的url,这是test环境的url https://test.ys7.com:65
        String path = "https://open.ys7.com";
        //
        // 设置你的appkey,appSecret,group
        StandardConsumerMessage consumerMessage = new StandardConsumerMessage(path, appKey, appSecret, group);
        // 设置调用接口的间隔时间,ms
        consumerMessage.setConsumeIntervalTime(1000);
        // 设置消费消息的回调接口,建议把消息放入本地队列,不要在回调函数里做业务处理.
        consumerMessage.setConsumerCallBack(new AbstractConsumerMessage.ConsumerCallBack() {
            public void consumerCall(List<Object> msg) {
                try {
                    // 使用队列异步处理消息
                    msgQueue.put(msg);
                } catch (InterruptedException e) {
                    // 用户这里需要处理异常
                    logger.error(String.format("放入队列失败,msgs:", msg), e);
                }
            }
        });
        try {
            consumerMessage.initClient();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


     class AsyncHandleThread extends Thread {
        AtomicBoolean isRunning = new AtomicBoolean(true);

        public AsyncHandleThread() {
            super("Async-HandleMsg-Thread");
        }

        @Override
        public void run() {
            while (isRunning.get()) {
                try {
                    List<Object> msgs = msgQueue.take();

                    logger.info("异步处理消息,消息数量:", msgs.size());
                    userProcessMsgFunction(msgs);
                } catch (InterruptedException e) {
                    // 用户这里需要处理异常
                    logger.error("线程中断异常", e);
                }
            }
        }

        // 关闭线程
        public void shutdown() {
            isRunning.set(false);
        }
    }


     public void userProcessMsgFunction(List<Object> msgs) {
         msgs.forEach(x->{
             JSONObject msg = (JSONObject)x;
             JSONObject header = msg.getJSONObject("header");

             JSONObject data = msg.getJSONObject("body").getJSONObject("payload");
             switch (data.getString("eventState")){

                 //inactive为心跳
                 case "inactive":{
                     JSONObject heartbeat=data.getJSONObject("FourGSafetyHelmetInfo").getJSONObject("Heartbeat");

                     //数据库记录心跳
                     helmetHeartbeatMapper.insert(
                             new HelmetHeartbeat(null,header.getString("deviceId"),header.getInteger("channelNo"),
                                     header.getString("messageId"),header.getDate("messageTime"),heartbeat.getBoolean("helmetStatus"),
                                     heartbeat.getString("beaconId"),heartbeat.getJSONObject("GPS").getString("longitude"),
                                     heartbeat.getJSONObject("GPS").getString("latitude"),heartbeat.getInteger("powerPercent"),
                                     heartbeat.getString("workStatus"),heartbeat.getString("RSSISignalStrength"),heartbeat.getBoolean("isAbnormalStationary")
                                     ,heartbeat.getBoolean("isSOS")));

                     //更新helmetInfo的电池电量
                     Integer status = heartbeat.getString("workStatus").equals("on")?1:0;
                     helmetInfoMapper.updatePower(header.getString("deviceId"),heartbeat.getInteger("powerPercent"),heartbeat.getBoolean("helmetStatus"),heartbeat.getJSONObject("GPS").getString("longitude"),heartbeat.getJSONObject("GPS").getString("latitude"));
                 };break;
                 //active为事件
                 case "active":{

                     JSONObject eventInfo=data.getJSONObject("FourGSafetyHelmetInfo").getJSONObject("EventInfo");
                     String eventType = eventInfo.keySet().iterator().next();
                     helmetEventInfoMapper.insert(new HelmetEventInfo(null,header.getString("deviceId"),header.getInteger("channelNo"),
                             header.getString("messageId"),header.getDate("messageTime"),eventType,eventInfo.getString(eventType)
                             ));


                 };break;
             }
         });
    }

    private void subscribe(){
/// 启动异步线程
        AsyncHandleThread asyncHandleThread = new AsyncHandleThread();
        asyncHandleThread.start();
        // 启动SDK消费消息
        init();
    }



}
