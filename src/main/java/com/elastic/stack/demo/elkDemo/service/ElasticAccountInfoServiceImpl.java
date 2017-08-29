package com.elastic.stack.demo.elkDemo.service;

import com.elastic.stack.demo.elkDemo.domain.AccountInfo;
import com.elastic.stack.demo.elkDemo.repository.ElasticAccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liyang on 2017/8/22.
 */
@Service
public class ElasticAccountInfoServiceImpl implements ElasticAccountInfoService {

    @Autowired
    private ElasticAccountInfoRepository elasticAccountInfoRepository;

    public AccountInfo queryAccountInfoById(String id) {
        return elasticAccountInfoRepository.findOne(id);
    }

    @Override
    public List<AccountInfo> queryAccountInfoBySource(String source) {
        return elasticAccountInfoRepository.findBySourceContains(source);
    }
}