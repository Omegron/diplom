package com.example.diplom.Articles;

import java.util.List;

public class DataModel {

    private final List<Articles> articlesList;
    private final String itemText;
    private boolean isExpandable;

    public DataModel(List<Articles> itemList, String itemText) {
        this.articlesList = itemList;
        this.itemText = itemText;
        isExpandable = false;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    public String getItemText() {
        return itemText;
    }

    public boolean isExpandable() {
        return isExpandable;
    }
}
