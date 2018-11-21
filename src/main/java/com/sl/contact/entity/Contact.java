package com.sl.contact.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author 舒露
 */
@Entity(name = "c_item")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Contact implements Serializable {
    @Id
    private String id;
    private String name;
    private String tel;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
