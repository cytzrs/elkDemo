package com.elastic.stack.demo.elkDemo.repository;

import com.elastic.stack.demo.elkDemo.domain.AccountInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by liyang on 2017/8/22.
 */
@Component
public interface ElasticAccountInfoRepository extends ElasticsearchRepository<AccountInfo,String> {
    //TODO define the search
    List<AccountInfo> findBySourceContains(String source);

}
