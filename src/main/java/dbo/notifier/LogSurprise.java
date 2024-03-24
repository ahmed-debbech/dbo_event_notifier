package dbo.notifier;

public class LogSurprise extends Logger{
    public void log(String s){
        super.write(s, "surp");
    }
}
