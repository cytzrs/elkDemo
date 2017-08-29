package com.elastic.stack.demo.elkDemo.repository;

import com.elastic.stack.demo.elkDemo.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * Created by liyang on 2017/8/23.
 */
@Component
public interface ElasticEmployeeRepository extends ElasticsearchRepository<Employee, String> {
}
