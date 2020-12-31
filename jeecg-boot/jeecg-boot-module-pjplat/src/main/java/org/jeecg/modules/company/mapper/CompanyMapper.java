package org.jeecg.modules.company.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.company.entity.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.context.annotation.Bean;

/**
 * @Description: 单位信息
 * @Author: jeecg-boot
 * @Date:   2020-10-21
 * @Version: V1.0
 */
public interface CompanyMapper extends BaseMapper<Company> {
    @Update(value = "update pjplat.bs_company set del_flag= 1 where id=#{id}")
    boolean deleteById(String id);
}
