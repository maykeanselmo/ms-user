package com.compassuol.challenge.msuser.application.dto;

import com.compassuol.challenge.msuser.application.enums.Event;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DataNotification {
    @JsonProperty("email")
    private String email;

    @JsonProperty("event")
    private Event event;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

}
