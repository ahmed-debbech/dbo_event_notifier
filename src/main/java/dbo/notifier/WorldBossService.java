package dbo.notifier;

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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class WorldBossService {

    @Autowired
    private Logger out;

    @Value("${trust-store}")
    private Resource trustStore;

    @Value("${trust-store-password}")
    private String trustStorePassword;

    @Value("${telegram.url}")
    String urlTelegram;

    public void check(){
        out.log("checking world boss progress at: " + LocalDateTime.now());
        double percentage = getPercentageValue();
        if(is100(percentage)) notifyUsers();
    }

    private void notifyUsers(){
        out.log("Notifying users: world boss is 100%");
        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            url += URLEncoder.encode("World boss is ready 100 percent", StandardCharsets.UTF_8.toString());
            restTemplate.getForEntity(url, String.class);
        } catch (UnsupportedEncodingException e) {
            out.log("could not access telegram to notify for world boss");
        }
    }
    private boolean is100(double per){
        if(per >= 99.99) return true;
        return false;
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

}
