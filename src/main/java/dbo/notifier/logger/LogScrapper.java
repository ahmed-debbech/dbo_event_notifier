package dbo.notifier.logger;

public class LogScrapper extends Logger{
    public void log(String s){
        super.write(s, "scrap");
    }
}
