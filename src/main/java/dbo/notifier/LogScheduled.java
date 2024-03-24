package dbo.notifier;

public class LogScheduled extends Logger {

    public void log(String s){
        super.write(s, "sched");
    }
}
