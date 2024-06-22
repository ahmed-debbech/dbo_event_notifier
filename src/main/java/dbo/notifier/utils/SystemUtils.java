package dbo.notifier.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;

public class SystemUtils {
    public static boolean tryStartChromeBrowser(String url) throws Exception{
        Process p = null;
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder builder = new ProcessBuilder();
            if(os.contains("windows")) {
                builder.command("cmd.exe", "/c", "start chrome \"" + url+ "\"");
                p = builder.start();
            }else{
                if(os.contains("linux")) {
                    builder.command("bash","-c", "google-chrome \"" + url + "\"");
                    p = builder.start();
                    System.err.println(p);
                    // p.destroyForcibly();
                    while(p.waitFor() < 0);
                }
            }
        }catch(Exception e){
            throw new Exception("Couldnt open browser");
        }
        return false;
    }
    public static String intToBinaryString(int number, int fixedLength) {
        // Convert the integer to binary string
        String binary = Integer.toBinaryString(number);

        // Calculate the number of zeros to pad
        int zerosToPad = fixedLength - binary.length();

        // Create a StringBuilder to construct the padded binary string
        StringBuilder paddedBinary = new StringBuilder();

        // Append zeros to the binary string
        for (int i = 0; i < zerosToPad; i++) {
            paddedBinary.append('0');
        }

        // Append the original binary string
        paddedBinary.append(binary);

        // Return the padded binary string
        return paddedBinary.toString();
    }

    public static String connectWithoutSSL(String url) throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        String resp  = "";
        try{
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();
            // Create a request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            resp = response.body();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new Exception("error while connecting to " + url);
        }
        return resp;
    }
}
