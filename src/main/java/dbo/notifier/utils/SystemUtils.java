package dbo.notifier.utils;

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
}
