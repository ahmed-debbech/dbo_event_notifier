package dbo.notifier;

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
	private AppNotifierService appNotifierService;
	@PostConstruct
	private void postConstruct() {
		appNotifierService.init();
	}
	public static void main(String[] args) {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Scheduled(cron = "*/1 * * * * *") //every seconds
	public void checkScheduledBudoHasArrived() {
		scheduledBudokaiService.notifyUsers();
	}

	@Scheduled(cron = "*/30 * * * * *") //every 30 secs
	public void scheduleWorldBoss() {
		//worldBossService.check();
	}

	@Scheduled(cron = "0 * * * * *") //every minute
	public void scheduleSurpriseBudokai() {
		budokaiService.check();
	}
}
