package com.example.quizappp2.Models;

public class UserModel {

    private String userName,userEmail,password,profile;
    private int score;
    private String category;
    private String userId;
    private String uid;

    public UserModel(String userName, String userEmail, String password, String profile) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.profile = profile;
    }

    public UserModel(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    public UserModel() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
