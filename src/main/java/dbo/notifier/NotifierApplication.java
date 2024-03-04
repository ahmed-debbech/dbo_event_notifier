package dbo.notifier;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalTime;

@EnableScheduling
@SpringBootApplication
public class NotifierApplication {

	@Autowired
	private Service service;

	@Autowired
	private WorldBossService worldBossService;


	public static void main(String[] args) {
		SpringApplication.run(NotifierApplication.class, args);
	}

	@Scheduled(cron = "0 0/15 * * * *")
	private void scheduleWithCron() {
		System.out.println("Cron Job is started at : " + LocalTime.now());
		service.run();
		System.out.println("Cron Job is ended at : " + LocalTime.now());
	}
	@Scheduled(cron = "*/1 * * * * *")
	public void scheduleTaskWithCronExpression() {
		System.out.println("Scheduled task is going to run at : " + LocalTime.now());
		service.notifyUsers();
	}

//	@Scheduled(cron = "0 0/10 * * * *")
	@Scheduled(cron = "*/30 * * * * *")

	public void scheduleWorldBoss() {
		System.err.println("Running world boss checked at: " + LocalTime.now());
		worldBossService.check();
	}
}
