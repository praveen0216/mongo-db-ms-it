package com.demo.mongo_db_ms_it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MongoDbMsItApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoDbMsItApplication.class, args);
	}

}
