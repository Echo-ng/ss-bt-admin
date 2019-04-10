package com.echostack.project.dao;

import com.echostack.project.model.entity.SysLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/3/24 20:23
 * @Description:
 */

public interface SysLogMapper {

    @Insert("insert into t_sys_log(username,gmt_create,gmt_modified,op_description,time,method,param,ip,location) values(#{username},#{gmtCreate},#{gmtModified},#{opDescription},#{time},#{method},#{param},#{ip},#{location})")
    void add(SysLog log);

    @Select("select * from t_sys_log")
    List<SysLog> findAll();

    @Delete("delete from t_sys_log where id = #{id}")
    int deleteById(Long id);
}
