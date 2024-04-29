package dbo.notifier.logger;

public class LogLiveEvents extends Logger{
    public void log(String s){
        super.write(s, "live");
    }
}
