package com.redhat.rest.example.demorest;


import com.google.gson.Gson;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
@RestController
public class RecordsDashboard {

	public static void main(String[] args) {

		SpringApplication.run(RecordsDashboard.class, args);
	}


	public static final String BODY = "${body}";


	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {



			@Override
			public void configure() throws Exception {

				restConfiguration()
						.component("servlet")
						.bindingMode(RestBindingMode.auto)
						.producerComponent("http4").host("localhost:8097");

				rest("/documentListGenerator")
						.post()
						.enableCORS(true)
						.route()
						.removeHeaders("*") // strip all headers (for this example) so that the received message HTTP headers do not confuse the REST producer when POSTing
						.bean(DocumentCheckListGenerator.class,"process")
						.endRest();


		}


	};


}}
