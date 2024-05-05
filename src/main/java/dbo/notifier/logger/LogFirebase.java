package dbo.notifier.logger;

public class LogFirebase extends Logger{
    public void log(String s){
        super.write(s, "firebase");
    }
}
