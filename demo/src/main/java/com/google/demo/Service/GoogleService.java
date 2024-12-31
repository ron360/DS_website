package com.google.demo.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class GoogleService {

    private static final int TIMEOUT = 10_000; // 10秒超时
    private static final int MAX_RETRIES = 3; // 最大重试次数

    public HashMap<String, String> query(String filter, String keyword) throws IOException {
        HashMap<String, String> results = new HashMap<>();
        PageHeap heap = new PageHeap();
        KeywordList kl = new KeywordList(filter, keyword);

        // 构建关键字
        if (filter.isEmpty()) {
            keyword += " game";
        } else {
            switch (filter) {
                case "brawl_stars":
                    keyword += " brawl stars";
                    break;
                case "arena_of_valor":
                    keyword += " arena of valor";
                    break;
                case "league_of_legends":
                    keyword += " league of legends";
                    break;
                case "valorant":
                    keyword += " valorant";
                    break;
                default:
                    break;
            }
        }

        System.out.println("Keyword: " + keyword);
        String url = "https://www.google.com/search?q=" + keyword.replaceAll(" ", "+");

        // 获取 HTML 内容，带有重试机制
        Document doc = fetchDocumentWithRetry(url);

        // 提取搜索结果
        Elements lis = doc.select("div").select(".kCrYT");
        System.out.println("Fetched " + lis.size() + " results");

        for (Element li : lis) {
            try {
                Elements links = li.select("a");
                if (links.isEmpty()) {
                    continue;
                }

                String citeUrl = links.get(0).attr("href").replace("/url?q=", "").split("&")[0];
                String decodedUrl = URLDecoder.decode(citeUrl, StandardCharsets.UTF_8);

                String title = li.select(".vvjwJb").text();
                if (title.isEmpty()) {
                    continue;
                }

                WebPage wp = new WebPage(title, decodedUrl);
                
                ArrayList<WebPage> childPages = wp.extractLinks();
                if(!childPages.isEmpty()) {
                	WebTree tree = new WebTree(wp);
                	tree.buildTree(childPages, kl.getList());
                	tree.postOrderTreeScore(kl.getList());
                	heap.add(tree.getRoot());
                }else {
                	wp.setScore(kl.getList());
                	heap.add(wp);
                }

            } catch (Exception e) {
                System.out.println("Error parsing result: " + e.getMessage());
                e.printStackTrace();
            }
        }

        while (!heap.getHeap().isEmpty()) {
            WebPage wp = heap.poll();
            results.put(wp.name, wp.url);
        }

        return results;
    }

    private Document fetchDocumentWithRetry(String url) throws IOException {
        int attempts = 0;
        IOException lastException = null;

        while (attempts < MAX_RETRIES) {
            try {
                // 尝试连接
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(TIMEOUT) // 设置超时时间
                        .get();
            } catch (IOException e) {
                attempts++;
                lastException = e;
                System.err.println("Attempt " + attempts + " failed: " + e.getMessage());
                if (attempts < MAX_RETRIES) {
                    try {
                        // 延迟 2 秒后重试
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", interruptedException);
                    }
                }
            }
        }

        // 如果所有尝试均失败，抛出最后的异常
        throw new IOException("Failed to fetch document after " + MAX_RETRIES + " attempts", lastException);
    }
}
