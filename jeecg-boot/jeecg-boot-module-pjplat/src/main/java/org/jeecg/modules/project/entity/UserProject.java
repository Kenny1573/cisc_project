package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("pjplat.bs_ser_project")
@KeySequence(value = "pjplat.SEQ_BS_USER_PROJECT",clazz = Long.class)
public class UserProject {

    private Long id;
    private String username;
    private Long prjId;
}
