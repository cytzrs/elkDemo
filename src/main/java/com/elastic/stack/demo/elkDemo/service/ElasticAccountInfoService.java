package com.elastic.stack.demo.elkDemo.service;

import com.elastic.stack.demo.elkDemo.domain.AccountInfo;

import java.util.List;

/**
 * Created by liyang on 2017/8/22.
 */
public interface ElasticAccountInfoService {

    AccountInfo queryAccountInfoById(String id);

    List<AccountInfo> queryAccountInfoBySource(String source);
}
