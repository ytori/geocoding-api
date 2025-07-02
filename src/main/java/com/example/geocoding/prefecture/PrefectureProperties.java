package com.example.geocoding.prefecture;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.prefecture")
@Validated
public record PrefectureProperties(@NotNull String geojsonFile,
                                   @NotNull String attributeName,
                                   Double bufferDistance
) {
}