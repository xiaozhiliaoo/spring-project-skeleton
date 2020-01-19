package org.lili.dao;

import org.apache.ibatis.annotations.Param;
import org.lili.common.dao.SqlMapper;
import org.lili.common.entity.BaseUser;
import org.lili.common.entity.BaseUserExample;

import java.util.List;


public interface BaseUserMapper extends SqlMapper {
    int countByExample(BaseUserExample example);

    int deleteByExample(BaseUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BaseUser record);

    int insertSelective(BaseUser record);

    List<BaseUser> selectByExample(BaseUserExample example);

    BaseUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BaseUser record, @Param("example") BaseUserExample example);

    int updateByExample(@Param("record") BaseUser record, @Param("example") BaseUserExample example);

    int updateByPrimaryKeySelective(BaseUser record);

    int updateByPrimaryKey(BaseUser record);
}