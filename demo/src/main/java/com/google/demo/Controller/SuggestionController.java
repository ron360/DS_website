package com.google.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuggestionController {

    @GetMapping("/suggestions")
    public Map<String, Object> getSuggestions(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        List<String> suggestions = new ArrayList<>();

        try {
            // 確保輸入的關鍵字長度有效
            if (keyword != null && keyword.trim().length() > 0) {
                boolean isEnglish = keyword.matches("^[a-zA-Z0-9 ]+$");

                if (isEnglish) {
                    // 英文推薦內容
                    suggestions.add(keyword + " guide");
                    suggestions.add(keyword + " update");
                    suggestions.add(keyword + " events");
                    suggestions.add(keyword + " quests");
                    suggestions.add(keyword + " tips");
                } else {
                    // 中文推薦內容
                    suggestions.add(keyword + " 最新攻略");
                    suggestions.add(keyword + " 更新");
                    suggestions.add(keyword + " 活動");
                    suggestions.add(keyword + " 任務");
                    suggestions.add(keyword + " 教學");
                }

                // 靜態補充推薦內容
                suggestions.add(keyword + (isEnglish ? " official site" : " 官網"));
                suggestions.add(keyword + (isEnglish ? " how to play" : " 怎麼玩"));
            } else {
                response.put("error", "Keyword is too short or invalid.");
                return response;
            }

            response.put("suggestions", suggestions);
        } catch (Exception e) {
            response.put("error", "Failed to fetch suggestions: " + e.getMessage());
        }

        return response;
    }
}