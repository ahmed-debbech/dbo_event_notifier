package dbo.notifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class Logger {

    @Value("${filename}")
    private String fileName;

    public void log(String s){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append('\n');
            writer.append(s);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
