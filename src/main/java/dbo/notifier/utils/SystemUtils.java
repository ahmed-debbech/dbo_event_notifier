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
}
