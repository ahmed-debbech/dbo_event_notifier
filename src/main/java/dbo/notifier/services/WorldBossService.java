package dbo.notifier.services;

import dbo.notifier.dto.BossProgress;
import dbo.notifier.logger.LogWorldBoss;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.services.firebase.AppNotificationService;
import dbo.notifier.services.firebase.IDatabaseApi;
import dbo.notifier.utils.ResultRetreiver;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class WorldBossService {

    private LogWorldBoss out = new LogWorldBoss();

    private boolean isNotified =false;

    @Value("${trust-store}")
    private Resource trustStore;

    @Value("${trust-store-password}")
    private String trustStorePassword;

    @Value("${telegram.url}")
    String urlTelegram;

    @Autowired
    private IDatabaseApi database;
    @Autowired
    private AppNotificationService appNotificationService;
    public double percentageProgress = -1;

    public void check(){
        out.log("checking world boss progress at: " + LocalDateTime.now());
        double percentage = getPercentageValue();
        this.percentageProgress = percentage;

        if(is100(percentage)) {
            if(!isNotified) {
                notifyUsers();
                isNotified = true;
            }
        }else{
            isNotified = false;
        }
    }

    private void notifyUsers(){
        out.log("Notifying users: world boss is 95%");
        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            url += URLEncoder.encode("Hurry up World boss is 95 percent. get ready.", StandardCharsets.UTF_8.toString());
            restTemplate.getForEntity(url, String.class);
            database.addNewWorldBoss(String.valueOf(new Date().getTime()));
            appNotificationService.sendNotif(ServiceType.WORLD_BOSS);
        } catch (Exception e) {
            out.log("could not access telegram to notify for world boss");
        }
    }
    private boolean is100(double per){
        if(per >= 95.0){
            return true;
        }else {
            return false;
        }
    }
    private double getPercentageValue(){
        out.log("retreiving api of worldboss : " + LocalDateTime.now());

        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(
                            trustStore.getURL(),
                            trustStorePassword.toCharArray()
                    ).build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        SSLConnectionSocketFactory socketFactory =
                new SSLConnectionSocketFactory(sslContext);
        HttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory).build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplateWithTrustStore = new RestTemplate(factory);

        ResponseEntity<String> response = restTemplateWithTrustStore
                .getForEntity("https://patch.dboglobal.to:5000/bossProgress", String.class);

        String resp = response.getBody();
        double d = Double.parseDouble(resp);
        out.log("done retreiving and converting at " + LocalDateTime.now());
        return d;
    }

    public List<String> getAllBoss(){
        int pid = database.allBoss();
        return (List<String>) ResultRetreiver.getInstance().waitFor(pid);
    }

    public BossProgress getProgress() {
        BossProgress bp = new BossProgress();
        bp.setPercentage(String.valueOf(this.percentageProgress));
        bp.setEta(null);
        return bp;
    }
}