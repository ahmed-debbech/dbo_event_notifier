package dbo.notifier.services;

import dbo.notifier.dto.BossProgress;
import dbo.notifier.logger.LogWorldBoss;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.services.firebase.IDatabaseApi;
import dbo.notifier.utils.ResultRetreiver;
import dbo.notifier.utils.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorldBossService {

    private LogWorldBoss out = new LogWorldBoss();

    private boolean isNotified =false;

    @Value("${trust-store}")
    private Resource trustStore;

    @Value("${trust-store-password}")
    private String trustStorePassword;

    @Autowired
    private IDatabaseApi database;

    @Autowired
    private INotifier notifierAgent;

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
        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.WORLD_BOSS);
    }
    private boolean is100(double per){
        if(per >= 95.0){
            return true;
        }else {
            return false;
        }
    }
    private double getPercentageValue() {
        out.log("retreiving api of worldboss : " + LocalDateTime.now());
        String resp = "-1";
        try{
            resp = SystemUtils.connectWithoutSSL("https://patch.dboglobal.to:5000/bossProgress");
        }catch (Exception e){
            out.log(e.getMessage());
            return -1;
        }
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