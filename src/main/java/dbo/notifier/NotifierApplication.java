package dbo.notifier;

import dbo.notifier.services.*;
import dbo.notifier.services.firebase.AppNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
@EnableScheduling
@SpringBootApplication
public class NotifierApplication {

	@Autowired
	private ILiveEvents liveEvents;

	@Autowired
	private AppNotificationService appNotifierService;

	@Autowired
	private IScrapper scrapper;

	@Autowired
	private IScheduledEvents eventsService;

	@Autowired
	private WorldBossService worldBossService;

	@PostConstruct
	private void postConstruct() {
		appNotifierService.init();
		scrapper.launch();
	}
	public static void main(String[] args) {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Scheduled(cron = "*/30 * * * * *") //every 30 secs
	public void checkingWorldBossProgress() {
		worldBossService.check();
	}

	@Scheduled(cron = "0 * * * * *") //every minute
	public void checkingLiveEvents() {
		liveEvents.check();
	}

	@Scheduled(cron = "*/1 * * * * *") //every second
	public void checkingScheduledEvents() {
		eventsService.checkScheduledEvents();
	}
}
