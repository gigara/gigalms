package classes.reserveDetails;// Created by Gigara on 11/25/2018

import classes.DateTime;
import classes.Reader;

import javax.persistence.Entity;

@Entity
public class ReserveDVD extends Reserve{

    public ReserveDVD(Reader reader, DateTime reserveTime) {
        super(reader, reserveTime);
    }
}
