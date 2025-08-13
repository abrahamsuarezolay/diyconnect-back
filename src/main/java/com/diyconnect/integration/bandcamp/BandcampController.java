package com.diyconnect.integration.bandcamp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/bandcamp")
public class BandcampController {

    private final BandcampService bandcampService;

    public BandcampController(BandcampService bandcampService) {
        this.bandcampService = bandcampService;
    }

    @GetMapping("/embed")
    public ResponseEntity<?> getBandcampEmbed(@RequestParam String url) {
        try {
            String iframe = bandcampService.getEmbedUrl(url);
            return ResponseEntity.ok(Map.of("iframe", iframe));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
