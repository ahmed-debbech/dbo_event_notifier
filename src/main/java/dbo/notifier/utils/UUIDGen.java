package dbo.notifier.utils;

import java.util.UUID;

public class UUIDGen {
    public static String  generate(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
