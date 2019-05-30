package classes.reserveDetails;// Created by Gigara on 11/25/2018

import classes.DateTime;
import classes.Reader;

import javax.persistence.*;

@MappedSuperclass
public abstract class Reserve
{
    @Id
    private Integer reserveID;

    @ManyToOne
    private Reader reader;

    @Embedded
    // rename the basic mappings
    @AttributeOverrides({
            @AttributeOverride(name="date", column=@Column(name="RESERVE_DAY")),
            @AttributeOverride(name="month", column=@Column(name="RESERVE_MONTH")),
            @AttributeOverride(name="year", column=@Column(name="RESERVE_YEAR")),
            @AttributeOverride(name="minute", column=@Column(name="RESERVE_MINUTE")),
            @AttributeOverride(name="hour", column=@Column(name="RESERVE_HOUR"))
    })
    private DateTime reserveTime;

    public Reserve(Reader reader, DateTime reserveTime) {
        this.reader = reader;
        this.reserveTime = reserveTime;
    }

    public Integer getReserveID() {
        return reserveID;
    }

    public void setReserveID(Integer reserveID) {
        this.reserveID = reserveID;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public DateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(DateTime reserveTime) {
        this.reserveTime = reserveTime;
    }
}
