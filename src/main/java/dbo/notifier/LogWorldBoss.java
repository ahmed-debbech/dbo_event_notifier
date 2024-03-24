package dbo.notifier;

public class LogWorldBoss extends Logger{
    public void log(String s){
        super.write(s, "world");
    }
}
