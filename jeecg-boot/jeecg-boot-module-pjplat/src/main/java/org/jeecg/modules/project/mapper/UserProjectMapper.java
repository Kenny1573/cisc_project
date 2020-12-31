package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.company.entity.Company;
import org.jeecg.modules.project.entity.KeyValue;
import org.jeecg.modules.project.entity.Project;
import org.jeecg.modules.project.entity.UserProject;

import java.util.List;

/**
 * @Description: 项目信息
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
public interface UserProjectMapper extends BaseMapper<UserProject> {

}
