package classes.borrowDetails;// Created by Gigara on 11/24/2018

import classes.DateTime;
import classes.Reader;
import io.ebean.Model;

import javax.persistence.*;

@MappedSuperclass
public abstract class BorrowItem extends Model {

    @Id
    @GeneratedValue
    private Integer borrowID;

    @ManyToOne
    private Reader reader;

    @Embedded
    //rename the basic mappings
    @AttributeOverrides({
            @AttributeOverride(name="date", column=@Column(name="BORROWED_DAY")),
            @AttributeOverride(name="month", column=@Column(name="BORROWED_MONTH")),
            @AttributeOverride(name="year", column=@Column(name="BORROWED_YEAR")),
            @AttributeOverride(name="minute", column=@Column(name="BORROWED_MINUTE")),
            @AttributeOverride(name="hour", column=@Column(name="BORROWED_HOUR"))
    })
    private DateTime borrowedTime;

    @Embedded
    // rename the basic mappings
    @AttributeOverrides({
            @AttributeOverride(name="date", column=@Column(name="RETURNED_DAY")),
            @AttributeOverride(name="month", column=@Column(name="RETURNED_MONTH")),
            @AttributeOverride(name="year", column=@Column(name="RETURNED_YEAR")),
            @AttributeOverride(name="minute", column=@Column(name="RETURNED_MINUTE")),
            @AttributeOverride(name="hour", column=@Column(name="RETURNED_HOUR"))
    })
    private DateTime returnedTime;


    public BorrowItem(Reader reader, DateTime borrowedTime, DateTime returnedTime) {
        this.reader = reader;
        this.borrowedTime = borrowedTime;
        this.returnedTime = returnedTime;
    }

    public Integer getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(Integer borrowID) {
        this.borrowID = borrowID;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public DateTime getBorrowedTime() {
        return borrowedTime;
    }

    public void setBorrowedTime(DateTime borrowedTime) {
        this.borrowedTime = borrowedTime;
    }

    public DateTime getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(DateTime returnedTime) {
        this.returnedTime = returnedTime;
    }
}
