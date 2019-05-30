package classes;// Created by Gigara on 10/17/2018

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Reader extends Model {
    @Id
    private Integer ID;
    private String name;
    private int mobNumber;
    private String email;

    public static Finder<Integer,Reader> find = new Finder<Integer, Reader>(Reader.class);

    public Reader(int ID, String name, int mobNumber, String email) {
        this.ID = ID;
        this.name = name;
        this.mobNumber = mobNumber;
        this.email = email;
    }

    public Reader(String name, int mobNumber, String email) {
        this.name = name;
        this.mobNumber = mobNumber;
        this.email = email;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getMobNumber() {
        return mobNumber;
    }

    public String getEmail() {
        return email;
    }
}
