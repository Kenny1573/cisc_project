package org.jeecg.modules.projectleader.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: BS_PROJECT_LEADER
 * @Author: jeecg-boot
 * @Date:   2020-10-19
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_project_leader")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="pjplat.bs_project_leader对象", description="BS_PROJECT_LEADER")
@KeySequence(value = "pjplat.SEQ_BS_PROJECT_LEADER",clazz = Long.class)
public class ProjectLeader implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProjectLeader(String prjlPrjId, String prjlDepartment, String prjlLeader) {
        this.prjlPrjId = prjlPrjId;
        this.prjlDepartment = prjlDepartment;
        this.prjlLeader = prjlLeader;
    }

    /**createTime*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "createTime")
    private java.util.Date createTime;
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
    @ApiModelProperty(value = "项目ID")
    private java.lang.String prjlPrjId;
	/**部门ID*/
	@Excel(name = "部门ID", width = 15)
    @ApiModelProperty(value = "部门ID")
    private java.lang.String prjlDepartment;
	/**人员ID*/
	@Excel(name = "人员ID", width = 15)
    @ApiModelProperty(value = "人员ID")
    private java.lang.String prjlLeader;

}
