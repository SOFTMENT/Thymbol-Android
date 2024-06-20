package com.softment.thymbol.Model;

import java.io.Serializable;
import java.util.Date;

public class FriendModel implements Serializable {

    public String fullName = "";
    public String email = "";
    public String profilePic = "";
    public String uid = "";
    public Date registredAt = new Date();
    public String regiType = "";
    public boolean isOrganizer = false;
    public int ticketPurchased = 0;
    public int likes = 0;
    public String supplierCode = "";
    public String userType = "";
    public boolean isAdmin = false;
    public String subscription_status = "";
    public String customer_id_stripe = "";
    public String client_secret_stripe = "";
    public String payment_method_id = "";
    public Date expireDate = new Date();
    public String subscription_id = "";
    public String interval = "";
    public double amount = 0;

    public static UserModel data  = new UserModel();



    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getRegistredAt() {
        return registredAt;
    }

    public void setRegistredAt(Date registredAt) {
        this.registredAt = registredAt;
    }

    public String getRegiType() {
        return regiType;
    }

    public void setRegiType(String regiType) {
        this.regiType = regiType;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public int getTicketPurchased() {
        return ticketPurchased;
    }

    public void setTicketPurchased(int ticketPurchased) {
        this.ticketPurchased = ticketPurchased;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(String subscription_status) {
        this.subscription_status = subscription_status;
    }

    public String getCustomer_id_stripe() {
        return customer_id_stripe;
    }

    public void setCustomer_id_stripe(String customer_id_stripe) {
        this.customer_id_stripe = customer_id_stripe;
    }

    public String getClient_secret_stripe() {
        return client_secret_stripe;
    }

    public void setClient_secret_stripe(String client_secret_stripe) {
        this.client_secret_stripe = client_secret_stripe;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }
}
