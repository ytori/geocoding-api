package com.example.geocoding;

import com.example.geocoding.prefecture.PrefectureLookupService;
import com.example.geocoding.prefecture.PrefectureProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.zip.GZIPInputStream;

@Configuration
@EnableConfigurationProperties({PrefectureProperties.class})
public class GeocodingConfig {


    @Bean
    public PrefectureLookupService prefectureLookupService(PrefectureProperties prefectureProperties) throws Exception {
        var input = new GZIPInputStream(Objects.requireNonNull(getClass().getResourceAsStream(prefectureProperties.geojsonFile())));
        return new PrefectureLookupService(
                input,
                prefectureProperties.attributeName(),
                prefectureProperties.bufferDistance()
        );
    }
}





