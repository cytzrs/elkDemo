package com.elastic.stack.demo.elkDemo.repository;

import com.elastic.stack.demo.elkDemo.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Page<Book> findByAuthor(String author, Pageable pageable);

    List<Book> findByTitle(String title);//无实现类，是通过反射实现的，title是Book的一个属性

    Book findById(String id);
}