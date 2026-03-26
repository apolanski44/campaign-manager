package com.ap.campaign_manager.static_data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/static-data")
@RequiredArgsConstructor
public class StaticDataController {
    private final StaticDataService staticDataService;

    @GetMapping("/keywords")
    public List<String> getKeywords(@RequestParam(required = false) String query) {
        return staticDataService.getKeywords(query);
    }

    @GetMapping("/towns")
    public List<String> getTowns() {
        return staticDataService.getTowns();
    }
}
