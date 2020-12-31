package org.jeecg.modules.problemsheet.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 问题整改
 * @Author: jeecg
 * @Date:   2020-11-16
 * @Version: V1.0
 */
@Data
public class ProblemDeal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long id;
	private java.lang.String avmeg = "";
	private java.lang.String deakey = "";
	private java.lang.String des = "";
	private java.lang.String uname = "";
	private java.lang.String dealsj = "";
	private java.lang.String taskid = "";
	private java.lang.String type = "";
}
