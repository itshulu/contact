package com.sl.contact.entity;

import java.io.Serializable;

/**
 * @author 舒露
 */
public class JsonMsg implements Serializable {
    private Contact contact;
    private String msg;
    private int status;

    public JsonMsg() {
    }

    public JsonMsg(Contact contact, String msg, int status) {
        this.contact = contact;
        this.msg = msg;
        this.status = status;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
