package dbo.notifier.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDGen {
    public static String  generate(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public static int fourNumbers(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(9000) + 1;
        return randomNumber;
    }
}
