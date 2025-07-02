package com.example.geocoding.prefecture;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PrefectureLookupController {

    private final PrefectureLookupService prefectureLookupService;

    @GetMapping("/prefecture")
    public ResponseEntity<PrefectureLookupResult> getPrefecture(
            @RequestParam("lat") @NotNull @NumberFormat(style = NumberFormat.Style.NUMBER) Double latitude,
            @RequestParam("lng") @NotNull @NumberFormat(style = NumberFormat.Style.NUMBER) Double longitude
    ) {
        var result = prefectureLookupService.lookup(latitude, longitude);
        return ResponseEntity.of(result);
    }
}
