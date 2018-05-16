package com.croxx.hgwechat.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class A1 {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String openID;
    @Column
    private String content;
    @Column
    private String contect;
    @Column
    private Date createDate;

    public A1() {
    }

    public A1(String openID, String content, String contect, Date createDate) {
        this.openID = openID;
        this.content = content;
        this.contect = contect;
        this.createDate = createDate;
    }

    /*    Getters & Setters     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
