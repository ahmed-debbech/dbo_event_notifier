package dbo.notifier.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BossProgress {
    private String percentage;
    private String eta;
}
