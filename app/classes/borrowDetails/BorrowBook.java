package classes.borrowDetails;// Created by Gigara on 11/25/2018

import classes.DateTime;
import classes.Reader;

import javax.persistence.Entity;

@Entity
public class BorrowBook extends BorrowItem{

    public BorrowBook(Reader reader, DateTime borrowedTime, DateTime returnedTime) {
        super(reader, borrowedTime, returnedTime);
    }
}
