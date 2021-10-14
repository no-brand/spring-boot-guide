package com.nobrand.springbootscheduletask;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;


@SpringBootTest
class ScheduleTaskApplicationTests {

	@SpyBean
	ScheduledTask task;

	@Test
	void testReportCurrentTimeVerifyRate() {
		await().atMost(Duration.FIVE_SECONDS)
			   .untilAsserted(() -> Mockito.verify(task, Mockito.times(1)).reportCurrentTime());
	}

}
