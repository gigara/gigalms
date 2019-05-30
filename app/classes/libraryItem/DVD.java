package classes.libraryItem;// Created by Gigara on 10/17/2018

import classes.borrowDetails.BorrowBook;
import classes.borrowDetails.BorrowDVD;
import classes.reserveDetails.ReserveDVD;
import io.ebean.Finder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class DVD extends LibraryItem{
    private String language;

    @ElementCollection
    @CollectionTable(
            name="subtitle"
    )
    @Column(name="subtitle")
    private List<String> subtitles;

    private String producer;

    @ElementCollection
    @CollectionTable(
            name="actor"
    )
    @Column(name="actor")
    private List<String> actors;

    //Item borrow details
    @OneToMany(cascade = CascadeType.ALL)
    private List<BorrowDVD> borrowDetails;

    //Item reservation details
    @OneToMany(cascade = CascadeType.ALL)
    private List<ReserveDVD> reservationDetails;

    public static Finder<Integer,DVD> find = new Finder<Integer, DVD>(DVD.class);

    public DVD(int isbn, String title, String sector, LocalDate publicationDate, String language, List<String> subtitles, String producer, List<String> actors) {
        super(isbn, title, sector, publicationDate);
        this.language = language;
        this.subtitles = subtitles;
        this.producer = producer;
        this. actors = actors;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public String getProducer() {
        return producer;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<BorrowDVD> getBorrowDetails() {
        return borrowDetails;
    }

    public void setBorrowDetails(List<BorrowDVD> borrowDetails) {
        this.borrowDetails = borrowDetails;
    }

    public void addBorrowDetail(BorrowDVD borrowDetails) {
        this.borrowDetails.add(borrowDetails);
    }

    public void addReserveDetail(ReserveDVD reserveDetails) {
        this.reservationDetails.add(reserveDetails);
    }

    public List<ReserveDVD> getReservationDetails() {
        return reservationDetails;
    }
}
