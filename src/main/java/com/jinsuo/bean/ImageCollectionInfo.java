package com.jinsuo.bean;

import java.util.List;

/**
 * 自定义图集类:
 * title 图集标题
 * urlList 图集中图片的url集合
 */
public class ImageCollectionInfo {
    public String title;
    public List<String> urlList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    @Override
    public String toString() {
        return "ImageCollectionInfo{" +
                "title='" + title + '\'' +
                ", urlList=" + urlList +
                '}';
    }
}
