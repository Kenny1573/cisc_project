package org.jeecg.modules.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.project.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserProjectMapper extends BaseMapper<SysUser> {


	@Select(value = "select id from  htplat.sys_user  where username = #{username} and del_flag = 0")
	public List<String> getUserIdByName(@Param("username") String username);

}
