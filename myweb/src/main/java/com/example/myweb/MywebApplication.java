package com.example.myweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MywebApplication {

	@RequestMapping("/")
	public String index() {
		return "index: Hello world!";
	}

	@RequestMapping("/myContext")
	public String myContext(@RequestParam(value="name", defaultValue = "World") String paramValue) {
		//Browser: http://localhost:8082/myContext?name=Rex
		return "myContext: Hello " + paramValue;
	}

	public static void main(String[] args) {
		SpringApplication.run(MywebApplication.class, args);
	}
}
