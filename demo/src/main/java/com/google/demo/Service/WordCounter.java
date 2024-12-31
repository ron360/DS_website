package com.google.demo.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.*;
import java.util.zip.GZIPInputStream;

public class WordCounter
{
    private String urlStr;
    private String content;
    private static final String[] website = {
        "https://www.gamer.com.tw/",
        "https://www.op.gg/",
        "https://www.4gamers.com.tw/",
        "https://news.gamebase.com.tw/",
        "https://www.ign.com/",
        "https://www.gamespot.com/",
        "https://playmeow.com/"
    };

    public WordCounter(String urlStr)
    {
        this.urlStr = urlStr;
    }

    public String fetchContent() throws IOException {
        URL url = new URL(this.urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept-Encoding", "gzip");

        InputStream in = "gzip".equals(conn.getContentEncoding())
                ? new GZIPInputStream(conn.getInputStream())
                : conn.getInputStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder retVal = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                retVal.append(line.toUpperCase()).append("\n");
            }
            return retVal.toString();
        }
    }

    public int countKeyword(String keyword) throws IOException
    {
        if (content == null)
        {
            content = fetchContent();
        }

        
        keyword = keyword.toUpperCase();

        return BoyerMoore(content, keyword);
    }

    public int countWebsite() throws IOException
    {
        return countMultipleWebsites(website);
    }

    public int countMultipleWebsites(String[] keywords) throws IOException
    {
        if (content == null)
        {
            content = fetchContent();
        }

        int totalCount = 0;

        for (String keyword : keywords)
        {
            String upperKeyword = keyword.toUpperCase();
            int fromIdx = 0;
            int found;

            while ((found = content.indexOf(upperKeyword, fromIdx)) != -1)
            {
                totalCount++;
                fromIdx = found + upperKeyword.length();
            }
        }

        return totalCount;
    }
    
    public int BoyerMoore(String T, String P) {
        int n = T.length();
        int m = P.length();
        
        int i = m - 1;
        int j = m - 1;
        int count = 0;

        while (i < n) { 
            if (T.charAt(i) == P.charAt(j)) { 
                if (j == 0) { 
                    count++; 
                    i += m; 
                    j = m - 1; 
                } else {
                    i--; 
                    j--; 
                }
            } else { 
                int l = last(T.charAt(i), P); 
                i += m - Math.min(j, 1 + l); 
                j = m - 1;
            }
        }
        return count; 
    }
// http://soslab.nccu.edu.tw/Welcome.html

    public int last(char c, String P){
    	for(int i = P.length() - 1 ; i >= 0 ; i--) {
    		if(P.charAt(i) == c) {
    			return i;
    		}
    	}
        return -1;
    }
    
} 