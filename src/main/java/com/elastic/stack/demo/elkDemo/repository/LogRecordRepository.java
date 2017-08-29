package com.elastic.stack.demo.elkDemo.repository;

import com.elastic.stack.demo.elkDemo.domain.LogRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by liyang on 2017/8/24.
 */
public interface LogRecordRepository extends ElasticsearchRepository<LogRecord, String> {

    Iterable<LogRecord> findLogRecordsByMessageContains(String keyWord);
}
