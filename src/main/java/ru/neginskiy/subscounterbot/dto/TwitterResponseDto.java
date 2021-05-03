package ru.neginskiy.subscounterbot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterResponseDto {
    private boolean following;
    private long id;
    private String screen_name;
    private String name;
    private long followers_count;
    private String formatted_followers_count;
    private boolean age_gated;
}
