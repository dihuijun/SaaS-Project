package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface SysLogService {


    //查询全部日志文件
    PageInfo findAll(String companyId, int page, int size);

    //添加
    int save(SysLog log);

}
