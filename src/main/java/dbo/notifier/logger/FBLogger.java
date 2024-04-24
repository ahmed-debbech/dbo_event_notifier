package dbo.notifier.logger;

public class FBLogger extends Logger{
    public void log(String s){
        super.write(s, "firebase");
    }
}
