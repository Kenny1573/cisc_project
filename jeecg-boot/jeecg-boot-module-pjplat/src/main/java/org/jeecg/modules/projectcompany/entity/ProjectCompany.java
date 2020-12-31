package org.jeecg.modules.projectcompany.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: BS_PROJECT_COMPANY
 * @Author: jeecg-boot
 * @Date:   2020-10-15
 * @Version: V1.0
 */
@Data
@TableName("pjplat.bs_project_company")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bs_project_company对象", description="BS_PROJECT_COMPANY")
@KeySequence(value = "pjplat.SEQ_BS_PROJECT_COMPANY")

public class ProjectCompany implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private Long id;
	/**prjId*/
	@Excel(name = "prjId", width = 15)
    @ApiModelProperty(value = "prjId")
    private String prjId;
	/**comId*/
	@Excel(name = "comId", width = 15)
    @ApiModelProperty(value = "comId")
    private String comId;
    /**comType*/
    @Excel(name = "comType", width = 15)
    @ApiModelProperty(value = "comType")
    private String comType;

    public ProjectCompany(String o, String type,String mainId, String id) {
        if (o!=null)
            this.id=Long.parseLong(o);
        this.comId=id;
        this.comType=type;
        this.prjId=mainId;

    }
    public ProjectCompany( String mainId,String type, String id) {
        this.comType=type;
        this.comId=id;
        this.prjId=mainId;

    }
}
