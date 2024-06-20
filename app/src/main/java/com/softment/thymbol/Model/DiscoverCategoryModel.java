package com.softment.thymbol.Model;

import java.io.Serializable;
import java.util.Date;

public class DiscoverCategoryModel implements Serializable {

    public String id = "";
    public String bannerImage = "";
    public Date date = new Date();
    public String name = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
