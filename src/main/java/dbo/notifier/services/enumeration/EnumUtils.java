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
    public static ScheduledEventNames convertFrom(EventType event){
        ScheduledEventNames x = null;
        switch(event){
            case DB_SCRAMBLE: x = ScheduledEventNames.DB_SCRAMBLE;
                break;
            case DOJO_WAR: x = ScheduledEventNames.DOJO_WAR;
                break;
            case BUDO_KID_SOLO: x = ScheduledEventNames.KID_SOLO_BUDOKAI;
                break;
            case BUDO_KID_TEAM: x = ScheduledEventNames.KID_PARTY_BUDOKAI;
                break;
            case BUDO_ADULT_TEAM: x = ScheduledEventNames.ADULT_PARTY_BUDOKAI;
                break;
            case BUDO_ADULT_SOLO: x = ScheduledEventNames.ADULT_SOLO_BUDOKAI;
                break;
        }
        return x;
    }
}
