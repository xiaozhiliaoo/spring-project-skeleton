package org.lili.service.impl;

import org.lili.common.entity.BaseUser;
import org.lili.common.entity.BaseUserExample;
import org.lili.dao.BaseUserMapper;
import org.lili.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lili
 * @date 2020/1/18 20:11
 * @description
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private BaseUserMapper baseUserMapper;

    @Override
    public Integer add(BaseUser baseUser) {
        return baseUserMapper.insertSelective(baseUser);
    }


    @Override
    public void updateById(BaseUser baseUser) {
        baseUserMapper.updateByPrimaryKeySelective(baseUser);
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        BaseUserExample example = new BaseUserExample();
        example.createCriteria().andIdIn(ids);
        return baseUserMapper.deleteByExample(example);
    }

    @Override
    public Integer count(BaseUserExample example) {
        return baseUserMapper.countByExample(example);
    }

    @Override
    public List<BaseUser> findAll(BaseUserExample example) {
        return baseUserMapper.selectByExample(example);
    }

    @Override
    public BaseUser findById(Integer id) {
        return baseUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteById(Integer id) {
        return baseUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BaseUser> findByIds(List<Integer> ids) {
        BaseUserExample example = new BaseUserExample();
        example.createCriteria().andIdIn(ids);
        return baseUserMapper.selectByExample(example);
    }
}
