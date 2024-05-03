package dbo.notifier.services.enumeration;

public class EnumUtils {
    public static boolean eventTypeContains(String ev){
        for (EventType direction : EventType.values()) {
            if (direction.name().equalsIgnoreCase(ev)) {
                return true;
            }
        }
        return false;
    }

}
