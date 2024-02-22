package dbo.notifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

@EnableScheduling
@SpringBootApplication
public class NotifierApplication {

	@Autowired
	private Service service;

	public static void main(String[] args) {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Scheduled(cron = "*/10 * * * * *")
	private void scheduleWithCron() {
		System.out.println("Cron Job is started at : " + LocalTime.now());
		service.run();
		System.out.println("Cron Job is ended at : " + LocalTime.now());
	}
}
