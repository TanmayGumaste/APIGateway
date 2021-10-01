package com.appsdeveloperblog.photoapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class GlobalFilterConfiguration {
	final Logger logger = LoggerFactory.getLogger(GlobalFilterConfiguration.class);
    @Order(1)
	@Bean
	public GlobalFilter secondPreFilter() {

		logger.info("My second Global filter executed ...........");
		return (exchange, chain) -> {
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				logger.info("My Third  post filter is executed....");

			}));
		};

	}
    @Order(2)
	@Bean
	public GlobalFilter thirdPreFilter() {

		logger.info("My Third Global filter executed ...........");
		return (exchange, chain) -> {
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				logger.info("My second  post filter is executed....");

			}));
		};

	}
    @Order(3)
	@Bean
	public GlobalFilter fourthPreFilter() {
   //executed last pre filter
		logger.info("My fourth Global filter executed ...........");
		return (exchange, chain) -> {
			//executed 1st post filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				logger.info("first  post filter is executed....");

			}));
		};

	}

}
