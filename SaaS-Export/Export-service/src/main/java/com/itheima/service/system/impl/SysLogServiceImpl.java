package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.SysLogDao;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;


    @Override
    public PageInfo findAll(String companyId,int page,int size) {
        PageHelper.startPage(page,size);
        List<SysLog> list = sysLogDao.findAll(companyId);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public int save(SysLog log) {
        log.setId(UUID.randomUUID().toString());
        return sysLogDao.save(log);
    }
}
