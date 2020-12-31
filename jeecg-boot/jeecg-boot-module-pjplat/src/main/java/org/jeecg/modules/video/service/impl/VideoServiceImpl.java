package org.jeecg.modules.video.service.impl;

import org.jeecg.modules.video.entity.Video;
import org.jeecg.modules.video.mapper.VideoMapper;
import org.jeecg.modules.video.service.IVideoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 监控管理
 * @Author: jeecg-boot
 * @Date:   2020-12-03
 * @Version: V1.0
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}
