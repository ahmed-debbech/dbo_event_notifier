package dbo.notifier;

import dbo.notifier.services.IScrapper;
import dbo.notifier.services.ScheduledBudokaiService;
import dbo.notifier.services.SurpriseBudokaiService;
import dbo.notifier.services.WorldBossService;
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
	private ScheduledBudokaiService scheduledBudokaiService;

	@Autowired
	private WorldBossService worldBossService;

	@Autowired
	private SurpriseBudokaiService budokaiService;

	@Autowired
	private AppNotificationService appNotifierService;

	@Autowired
	private IScrapper scrapper;

	@PostConstruct
	private void postConstruct() {
		appNotifierService.init();
		scrapper.launch();
	}
	public static void main(String[] args) {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Scheduled(cron = "*/1 * * * * *") //every seconds
	public void checkScheduledBudoHasArrived() {
		//scheduledBudokaiService.notifyUsers();
	}

	@Scheduled(cron = "*/30 * * * * *") //every 30 secs
	public void scheduleWorldBoss() {
		//worldBossService.check();
	}

	@Scheduled(cron = "0 * * * * *") //every minute
	public void scheduleSurpriseBudokai() {
		//budokaiService.check();
	}
}
