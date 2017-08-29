package com.elastic.stack.demo.elkDemo.controller;

import com.elastic.stack.demo.elkDemo.domain.AccountInfo;
import com.elastic.stack.demo.elkDemo.domain.Book;
import com.elastic.stack.demo.elkDemo.domain.Employee;
import com.elastic.stack.demo.elkDemo.domain.LogRecord;
import com.elastic.stack.demo.elkDemo.repository.ElasticEmployeeRepository;
import com.elastic.stack.demo.elkDemo.repository.LogRecordRepository;
import com.elastic.stack.demo.elkDemo.service.BookService;
import com.elastic.stack.demo.elkDemo.service.ElasticAccountInfoService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by liyang on 2017/8/22.
 */
@RestController
@Controller
public class ElasticController {

    private final static Logger logger = LoggerFactory.getLogger(ElasticController.class);

    @Autowired
    private ElasticAccountInfoService elasticAccountInfoService;

    @Autowired
    private ElasticEmployeeRepository elasticEmployeeRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    private final static Gson gson = new Gson();

    @RequestMapping("/esAccountInfo")
    public String queryAccountInfo(String id, ModelMap modelMap){

        AccountInfo accountInfo = elasticAccountInfoService.queryAccountInfoById(id);
        modelMap.addAttribute("esAccountInfo",accountInfo);
        modelMap.addAttribute("test_elastic","Test the elasticsearch");

        return "accountInfo";
    }

    @RequestMapping("/esAccountInfoName")
    public String queryAccountInfoByAccountName(String accountName, ModelMap modelMap){

        List<AccountInfo> accountInfo = elasticAccountInfoService.queryAccountInfoBySource("'log'");
        modelMap.addAttribute("esAccountInfo",accountInfo);
        modelMap.addAttribute("test_elastic","Test the elasticsearch");

        return "accountInfo";
    }

    @RequestMapping(value = "employee",method = RequestMethod.GET)
    @ResponseBody
    public String employee() {
        Iterable<Employee> employee = elasticEmployeeRepository.findAll();
        return gson.toJson(employee);
    }

    @RequestMapping(value = "employee/one",method = RequestMethod.GET)
    @ResponseBody
    public String employeeOne(@RequestBody String id) {
        Employee employee = elasticEmployeeRepository.findOne(id);
        return gson.toJson(employee);
    }

    @RequestMapping(value = "employee/add",produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public String addEmpoleyy(@RequestBody Employee employee) {
        Employee ret = elasticEmployeeRepository.save(employee);
        return gson.toJson(ret);
    }

    @RequestMapping(value = "book/add",produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public Object addBook(@RequestBody Book book) {
        Book ret = bookService.save(book);

        //Book _b = bookService.findOne("001");
        //Iterable<Book> books = bookService.findAll();
        return ret;
    }

    @RequestMapping(value = "book/all", method = RequestMethod.GET)
    @ResponseBody
    public Object finaAllBook() {

        Iterable<Book> books = bookService.findAll();
        for(Book book: books) {
            logger.info(book.toString());
        }
        return books;
    }


    @RequestMapping(value = "log/all", method = RequestMethod.GET)
    @ResponseBody
    public Object finaAllLog() {

        Iterable<LogRecord> books = logRecordRepository.findAll();
        for(LogRecord book: books) {
            String message = book.getMessage();
            if(message.contains("梅梅")) {
                for(int i = 0; i < 100; i++) {
                    logger.info("{}爱{}梅梅", "我", "你");
                }
            }
            logger.info(book.toString());
        }
        return books;
    }

    //@IndexAnno
    @RequestMapping(value = "log/get", method = RequestMethod.GET)
    @ResponseBody
    public Object finaAllBook(@RequestBody String keyWord) {

        Iterable<LogRecord> logRecords = logRecordRepository.findLogRecordsByMessageContains(keyWord);
        List<LogRecord> list = Lists.newArrayList();
        logRecords.forEach(single -> list.add(single));

        return list;
    }

}
