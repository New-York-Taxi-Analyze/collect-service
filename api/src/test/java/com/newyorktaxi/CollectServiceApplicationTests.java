package com.newyorktaxi;

import static org.assertj.core.api.Assertions.assertThat;

import com.newyorktaxi.controller.ReportController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CollectServiceApplicationTests {

	@Autowired
	ReportController reportController;

	@Test
	void contextLoads() {
		assertThat(reportController).isNotNull();
	}
}
