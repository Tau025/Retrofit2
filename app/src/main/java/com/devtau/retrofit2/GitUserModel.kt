package com.devtau.retrofit2;

import com.google.gson.annotations.SerializedName;
/**
 * POJO class files can be generated from Json strings with help of http://www.jsonschema2pojo.org/
 */
public class GitModelPOJO {

    @SerializedName("login")
    private String login;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("blog")
    private String blog;

    @SerializedName("avatarUrl")
    private String avatarUrl;


    //setters
    public void setLogin(String login) {
        this.login = login;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setBlog(String blog) {
        this.blog = blog;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    //getters
    public String getLogin() {
        return login;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCompany() {
        return company;
    }
    public String getBlog() {
        return blog;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
