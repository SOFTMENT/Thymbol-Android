package com.softment.thymbol.Model;

import java.io.Serializable;
import java.util.Date;

public class StoreModel implements Serializable {
    public String storeName = "";
    public String storeAddressTitle = "";
    public String storeAddress = "";
    public String storeId = "";
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String storeLogo = "";
    public Date date = new Date();
    public String category = "";
    public boolean isBest = false;
    public String outOf5  = "";
    public String totalRatings = "";

    public String storeCategory = "";

    public String countryCode = "";

    public String geoHash = "";

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddressTitle() {
        return storeAddressTitle;
    }

    public void setStoreAddressTitle(String storeAddressTitle) {
        this.storeAddressTitle = storeAddressTitle;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isBest() {
        return isBest;
    }

    public void setBest(boolean best) {
        isBest = best;
    }

    public String getOutOf5() {
        return outOf5;
    }

    public void setOutOf5(String outOf5) {
        this.outOf5 = outOf5;
    }

    public String getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(String totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
