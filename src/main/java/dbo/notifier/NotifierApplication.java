package dbo.notifier;

import dbo.notifier.services.*;
import dbo.notifier.services.firebase.AppNotificationService;
import org.checkerframework.checker.units.qual.A;
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
	public void scheduleWorldBoss() {
		worldBossService.check();
	}

	@Scheduled(cron = "0 * * * * *") //every minute
	public void scheduleSurpriseBudokai() {
		liveEvents.check();
	}
}
