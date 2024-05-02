package dbo.notifier.model;

import java.util.Map;
import java.util.stream.Collectors;

public class NotifConfig {
    public boolean adult_solo;
    public boolean adult_party;
    public boolean kid_solo;
    public boolean kid_party;
    public boolean dojo_war;
    public boolean db_scramble;

    public NotifConfig(){
        adult_party = true;
        adult_solo = true;
        kid_party = true;
        kid_solo = true;
        dojo_war = true;
        db_scramble = true;
    }
    public NotifConfig(Map<String, Boolean> map){
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            switch (entry.getKey()){
                case "adult_solo": adult_solo = entry.getValue();
                break;
                case "adult_party": adult_party = entry.getValue();
                    break;
                case "kid_solo": kid_solo = entry.getValue();
                    break;
                case "kid_party": kid_party = entry.getValue();
                    break;
                case "dojo_war": dojo_war = entry.getValue();
                    break;
                case "db_scramble": db_scramble = entry.getValue();
                    break;
            }
        }
    }
}
