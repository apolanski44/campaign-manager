package com.ap.campaign_manager.static_data;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaticDataService {
    private static final List<String> KEYWORDS = List.of(
            "sport",
            "sportswear",
            "shoes",
            "swimming",
            "tennis",
            "kids",
            "fitness",
            "outdoor",
            "running",
            "training",
            "cycling",
            "football",
            "basketball",
            "accessories",
            "equipment"
    );

    private static final List<String> TOWNS = List.of(
            "Warszawa",
            "Kraków",
            "Wrocław",
            "Poznań",
            "Gdańsk",
            "Łódź",
            "Szczecin",
            "Lublin",
            "Katowice",
            "Białystok",
            "Myślenice"
    );

    public List<String> getTowns() {
        return TOWNS;
    }

    public List<String> getKeywords(String queryParam) {
        if (queryParam.isBlank()) {
            return KEYWORDS;
        }

        String normalizedQueryParam = queryParam.toLowerCase();

        return KEYWORDS.stream()
                .filter(keyword -> keyword.toLowerCase().startsWith(normalizedQueryParam))
                .toList();
    }
}
