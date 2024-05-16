package dbo.notifier.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScheduledEvents {

    private String event_of_the_week;
    private String special_event;

    private List<Integer> current_events;

    private String adult_solo_budokai;
    private String past_adult_solo_budokai;

    private String adult_party_budokai;
    private String past_adult_party_budokai;

    private String kid_solo_budokai;
    private String past_kid_solo_budokai;

    private String kid_party_budokai;
    private String past_kid_party_budokai;

    private String dojo_war;
    private String past_dojo_war;

    private String db_scramble;
    private String past_db_scramble;

}
