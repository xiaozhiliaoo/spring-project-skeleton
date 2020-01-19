package org.lili.service;

import org.lili.common.entity.BaseUser;
import org.lili.common.entity.BaseUserExample;

import java.util.List;

/**
 * @author lili
 * @date 2020/1/18 18:03
 * @description
 */
public interface UserService {

    Integer add(BaseUser baseUser);


    Integer count(BaseUserExample example);


    List<BaseUser> findAll(BaseUserExample example);


    BaseUser findById(Integer id);

    int deleteById(Integer id);


    void updateById(BaseUser baseUser);

    int deleteByIds(List<Integer> ids);


    List<BaseUser> findByIds(List<Integer> ids);
}
