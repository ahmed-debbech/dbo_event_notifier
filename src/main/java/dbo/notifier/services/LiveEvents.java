package dbo.notifier.services;

import dbo.notifier.logger.LogLiveEvents;
import dbo.notifier.services.firebase.AppNotificationService;
import dbo.notifier.services.firebase.IDatabaseApi;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiveEvents implements ILiveEvents {

    private LogLiveEvents out = new LogLiveEvents();


    private int apiReturnedValue = 0;

    @Value("${trust-store}")
    private Resource trustStore;

    @Value("${trust-store-password}")
    private String trustStorePassword;

    @Value("${telegram.url}")
    String urlTelegram;

    Map<String, Integer> eventNumber;

    @Autowired
    private IDatabaseApi database;
    @Autowired
    private AppNotificationService appNotificationService;

    int[] listOfEvents;

    @Override
    public List<Integer> getList() {
        List<Integer> list = new ArrayList<>();
        if(this.listOfEvents == null) return null;
        for(int i=0; i<=this.listOfEvents.length-1; i++)
            list.add(this.listOfEvents[i]);
        return list;
    }


    private void prepareDict(){
        this.eventNumber = new HashMap<>();
        eventNumber.put("EVENT_DB_SCRAMBLE", 2);
        eventNumber.put("EVENT_DOJO_WAR", 4);
        eventNumber.put("EVENT_BUDO_KID_SOLO", 8);
        eventNumber.put("EVENT_BUDO_KID_TEAM", 16);
        eventNumber.put("EVENT_BUDO_ADULT_SOLO", 32);
        eventNumber.put("EVENT_BUDO_ADULT_TEAM", 64);
        eventNumber.put("EVENT_DOUBLE_XP", 128);
        eventNumber.put("EVENT_DOUBLE_RANKED", 256);
        eventNumber.put("EVENT_DOUBLE_TOKEN", 512);
        eventNumber.put("EVENT_DOUBLE_DB", 1024);
        eventNumber.put("EVENT_DOUBLE_WAGU", 2048);
        eventNumber.put("EVENT_DOUBLE_UPGRADE", 4096);
        eventNumber.put("EVENT_DOUBLE_CRAFTING", 8192);
        eventNumber.put("EVENT_DOUBLE_GEMSTONE", 16384);
        eventNumber.put("EVENT_DOUBLE_BOX", 32768);
        eventNumber.put("EVENT_BEE", 65536);
        eventNumber.put("EVENT_FAIRY", 131072);
        eventNumber.put("EVENT_BUNNY", 262144);
        eventNumber.put("EVENT_SUMMER", 524288);
        eventNumber.put("EVENT_CHRISTMAS", 1048576);
        eventNumber.put("EVENT_HALLOWEEN", 2097152);
        eventNumber.put("EVENT_APRIL_FOOL", 4194304);
    }

    public void check(){
        out.log("checking all current events at: " + LocalDateTime.now());
        prepareDict();
        int ev = getCurrentEvents();
        int [] curr = getFromBinary(ev);
        this.listOfEvents = curr;
        if(ev != apiReturnedValue) {
            //TODO notify users about live events
            for(int i=0; i<=this.listOfEvents.length-1; i++){

            }
        }
        apiReturnedValue = ev;
    }
    private void notifyUsers(EventType event){
        out.log("Notifying users: FOR EVENT: " + event.name());
        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            url += URLEncoder.encode(event.name() + " event is starting now!", StandardCharsets.UTF_8.toString());
            restTemplate.getForEntity(url, String.class);
            database.addNewEvent(event.name(), String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) + "000");
            appNotificationService.sendNotif(ServiceType.EVENT, event);
        } catch (UnsupportedEncodingException e) {
            out.log("could not access telegram to notify for world boss");
        }
    }
    private int getCurrentEvents(){
        out.log("retreiving api of current events : " + LocalDateTime.now());

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
                .getForEntity("https://patch.dboglobal.to:5000/currentEvents", String.class);

        String resp = response.getBody();
        int d = Integer.parseInt(resp);
        out.log("done retreiving at " + LocalDateTime.now());
        return d;
    }

    /**
     * extracts current events from (int) number from api
     * and creates array mapping multiple two of current events
     * @param ev number of all events to be represtened in binary
     * @return int[] like [2,4,128,256] => the current live events to be mapped
     */
    private int[] getFromBinary(int ev){
        String binary = Integer.toBinaryString(ev);
        int iter = 1;
        binary = new StringBuilder().append(binary).reverse().toString();
        int [] currEvents = new int[this.eventNumber.size()];
        int h = 1;
        for(int i=0; i<=binary.length()-1; i++){
            if(binary.charAt(i) == '1'){
                out.log("event running: " + iter);
                currEvents[h] = iter;
                h++;
            }
            iter = iter * 2;
        }
        currEvents[0] = h;
        return currEvents;
    }

}
