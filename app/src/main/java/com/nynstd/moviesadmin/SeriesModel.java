package com.nynstd.moviesadmin;

public class SeriesModel {
    String title,image,video,categories;

    public SeriesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public SeriesModel(String title, String image, String video, String categories) {
        this.title = title;
        this.image = image;
        this.video = video;
        this.categories = categories;
    }
}
