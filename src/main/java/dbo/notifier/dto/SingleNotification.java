package dbo.notifier.dto;


import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SingleNotification {
    private String fieldName;
    private Boolean activated;
    private String userId;
}
