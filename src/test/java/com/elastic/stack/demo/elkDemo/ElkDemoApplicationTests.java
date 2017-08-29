package com.elastic.stack.demo.elkDemo;

import com.elastic.stack.demo.elkDemo.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElkDemoApplication.class)
public class ElkDemoApplicationTests {

	private final static Logger logger = LoggerFactory.getLogger(ElkDemoApplicationTests.class);

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void createIndex() {
		elasticsearchTemplate.createIndex(Book.class);
	}

	@Test
	public void testEnv() {
		System.out.println("----------------------------");
	}

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}

	private ExecutorService executorService = Executors.newFixedThreadPool(50);


	@Test
	public void testOnce() {
		String ret = restTemplate.getForObject("https://test.bldz.com/", String.class);
		logger.info(ret);
	}

	@Test
	public void testRedis() throws InterruptedException {
		for(int i = 0; i < 10; i++) {
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					for(int j = 0; j < 10000000; j++) {
						Date date = new Date();
						//String json = restTemplate.getForObject("https://test.bldz.com/basic/login/validateCode?time="+date.getTime(), String.class);
						//logger.info("{}: {}", java.lang.Thread.currentThread().getName(), json);
						restTemplate.getForObject("https://test.bldz.com/", String.class);
						//logger.info("{}: {}", java.lang.Thread.currentThread().getName(), json);
					}
				}
			});
		}

		Thread.sleep(Long.MAX_VALUE);
	}

}
