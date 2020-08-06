package com.janus.newsapp;

public class NewsFormat {
    private String newsTitle, newsDesc, newsImage, newsURL;

    public String getNewsTitle() { return newsTitle;
    }

    public String getNewsDesc() {
        if(newsDesc == "null"){
            return "....";
        } else {
            return newsDesc;
        }
    }

    public String getNewsImage() {
        return newsImage;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public NewsFormat(String newsTitle, String newsDesc, String newsImage, String newsURL) {
        this.newsTitle = newsTitle;
        this.newsDesc = newsDesc;
        this.newsImage = newsImage;
        this.newsURL = newsURL;
    }
}
