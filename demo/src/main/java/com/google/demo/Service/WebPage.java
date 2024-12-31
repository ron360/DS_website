package com.google.demo.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
public class WebPage{
	public String name;
	public String url;
	public WordCounter counter;
	public double score;

	public WebPage(String name, String url){
		this.name = name;
		this.url = url;
		this.counter = new WordCounter(url);
		score = 0;
	}
	public void setScore(ArrayList<Keyword> keywordList) throws IOException{
		for (Keyword k: keywordList) {
			score += counter.countKeyword(k.name) * k.weight;
		}
		score += counter.countWebsite() * 40;
	}
	public double getScore() {
		return score;
	}
	
	public ArrayList<WebPage> extractLinks() throws IOException {
        ArrayList<WebPage> childPages = new ArrayList<>();
        String content = counter.fetchContent();

        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.contains("href=\"https://")) { 
                int startIdx = line.indexOf("href=\"") + 6;
                int endIdx = line.indexOf("\"", startIdx);
                String foundUrl = line.substring(startIdx, endIdx);

                WebPage childPage = new WebPage("Child: " + foundUrl, foundUrl);
                childPages.add(childPage);
            }
        }

        return childPages;
    }
}
