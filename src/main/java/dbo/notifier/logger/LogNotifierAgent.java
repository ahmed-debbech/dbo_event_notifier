package dbo.notifier.logger;

public class LogNotifierAgent extends Logger{
    public void log(String s){
        super.write(s, "notifier");
    }
}
