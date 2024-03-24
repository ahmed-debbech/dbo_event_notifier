package dbo.notifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private String fileName = "log";


    public void write(String s, String channel){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append('\n');
            writer.append("[ "+ LocalDateTime.now() +" ]    => " + s);
            writer.close();

            writer = new BufferedWriter(new FileWriter(fileName + "_" + channel, true));
            writer.append('\n');
            writer.append("[ "+ LocalDateTime.now() +" ]    => " + s);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
