package com.softment.thymbol.Model;

import java.io.Serializable;
import java.util.Date;

public class Voucher implements Serializable {

    public String voucherCode = "";
    public String voucherId = "";
    public String voucherRedeemId = "";
    public Date voucherRedeemDate = new Date();
    public Date voucherGenerateDate = new Date();
    public String userId = "";
    public Date voucherCreateDate = new Date();
    public String shopName = "";
    public String voucherCategory = "";
    public Double latitude = 0.0;
    public Double longitude = 0.0;
    public String addressName = "";
    public String address = "";
    public String voucherConditions = "";
    public String shopLogo = "";
    public int discount = 0;
    public boolean isFree = false;
    public String freeMessage = "";
    public Date voucherExpireDate = new Date();
    public String shopId = "";
    public String catId = "";
    public boolean isForLocals = false;
    public boolean isForHealthcare = false;
    public boolean isForTourist = false;
    public boolean isForBackpacker = false;


    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherRedeemId() {
        return voucherRedeemId;
    }

    public void setVoucherRedeemId(String voucherRedeemId) {
        this.voucherRedeemId = voucherRedeemId;
    }

    public Date getVoucherRedeemDate() {
        return voucherRedeemDate;
    }

    public void setVoucherRedeemDate(Date voucherRedeemDate) {
        this.voucherRedeemDate = voucherRedeemDate;
    }

    public Date getVoucherGenerateDate() {
        return voucherGenerateDate;
    }

    public void setVoucherGenerateDate(Date voucherGenerateDate) {
        this.voucherGenerateDate = voucherGenerateDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getVoucherCreateDate() {
        return voucherCreateDate;
    }

    public void setVoucherCreateDate(Date voucherCreateDate) {
        this.voucherCreateDate = voucherCreateDate;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
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

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVoucherConditions() {
        return voucherConditions;
    }

    public void setVoucherConditions(String voucherConditions) {
        this.voucherConditions = voucherConditions;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getFreeMessage() {
        return freeMessage;
    }

    public void setFreeMessage(String freeMessage) {
        this.freeMessage = freeMessage;
    }

    public Date getVoucherExpireDate() {
        return voucherExpireDate;
    }

    public void setVoucherExpireDate(Date voucherExpireDate) {
        this.voucherExpireDate = voucherExpireDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public boolean isForLocals() {
        return isForLocals;
    }

    public void setForLocals(boolean forLocals) {
        isForLocals = forLocals;
    }

    public boolean isForHealthcare() {
        return isForHealthcare;
    }

    public void setForHealthcare(boolean forHealthcare) {
        isForHealthcare = forHealthcare;
    }

    public boolean isForTourist() {
        return isForTourist;
    }

    public void setForTourist(boolean forTourist) {
        isForTourist = forTourist;
    }

    public boolean isForBackpacker() {
        return isForBackpacker;
    }

    public void setForBackpacker(boolean forBackpacker) {
        isForBackpacker = forBackpacker;
    }

    public static Voucher data  = new Voucher();

    public Voucher() {
        data = this;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }
}
