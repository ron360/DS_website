package com.google.demo.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebTree {
    private WebPage root;
    private List<WebTree> children;
    private final int depth = 2;
    private int currentDepth;
    
    public WebTree(WebPage root) {
        this(root, 0);
    }
    
    public WebTree(WebPage root, int currentDepth){
    	this.root = root;
        this.children = new ArrayList<>();
        this.currentDepth = currentDepth;
    }

    public void addChild(WebTree child) {
        children.add(child);
    }
    
    public void buildTree(ArrayList<WebPage> childPages, ArrayList<Keyword> keywordList) throws IOException {
        if (currentDepth > depth) {
            return; 
        }

        for (WebPage child : childPages) {
            WebTree childTree = new WebTree(child, currentDepth + 1); 
            children.add(childTree); 
            if (currentDepth < depth) {
                ArrayList<WebPage> grandChildPages = child.extractLinks();
                if (!grandChildPages.isEmpty()) {
                    childTree.buildTree(grandChildPages, keywordList);
                }
            }
        }
    }
    
    public void postOrderTreeScore(ArrayList<Keyword> keywordList) throws IOException {
        postOrderTreeScore(root, keywordList);
        sumAllScore();
    }

    public void postOrderTreeScore(WebPage root, ArrayList<Keyword> keywordList) throws IOException {
        for (WebTree child : children) {
            child.postOrderTreeScore(child.root, keywordList);
        }
        root.setScore(keywordList);
    }

    public void sumAllScore() throws IOException {
        int childTotalScore = 0;
        for (WebTree child : children) {
            childTotalScore += child.root.getScore();
        }
        root.score += childTotalScore;
    }

    
    public WebPage getRoot() {
        return root;
    }

    public List<WebTree> getChildren() {
        return children;
    }

}
