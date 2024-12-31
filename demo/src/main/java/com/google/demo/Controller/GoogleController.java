package com.google.demo.Controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.demo.Service.GoogleService;

@Controller
public class GoogleController {

	@GetMapping("/")
	public String showSearchPage() {
		return "google"; // 對應到 google.html
	}

	@GetMapping("/google")
	public String search(@RequestParam String keyword,
			@RequestParam(required = false) String category, 
			Model model) {
		try {
			// 調用 GoogleService 的 query 方法
			GoogleService googleService = new GoogleService();
			HashMap<String, String> results = googleService.query(category, keyword);

			model.addAttribute("google", results); // 將查詢結果傳遞到前端
			model.addAttribute("keyword", keyword); // 將關鍵字傳遞到前端
			return "result"; // 映射到 result.html
		} catch (Exception e) {
			model.addAttribute("error", "搜索时发生错误: " + e.getMessage());
			return "error"; // 映射到 error.html
		}
	}
}
