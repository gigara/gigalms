package classes.libraryItem;// Created by Gigara on 10/17/2018

import classes.borrowDetails.BorrowBook;
import classes.reserveDetails.ReserveBook;
import classes.reserveDetails.ReserveDVD;
import io.ebean.Finder;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

@Entity
public class Book extends LibraryItem{

    @ElementCollection
    @CollectionTable(
            name="author"
    )
    @Column(name="author")
    private List<String> authors;

    private String publisher;
    private int noOfPages;

    //Item borrow details
    @OneToMany(cascade = CascadeType.ALL)
    private List<BorrowBook> borrowDetails;

    //Item reservation details
    @OneToMany(cascade = CascadeType.ALL)
    private List<ReserveBook> reservationDetails;

    public Book(int isbn, String title, String sector, LocalDate publicationDate, List<String> authors, String publisher, int noOfPages) {
        super(isbn, title, sector, publicationDate);
        this.authors = authors;
        this.publisher = publisher;
        this.noOfPages = noOfPages;
    }

    public static Finder<Integer,Book> find = new Finder<Integer, Book>(Book.class);

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setBorrowDetails(List<BorrowBook> borrowDetails) {
        this.borrowDetails = borrowDetails;
    }

    public List<BorrowBook> getBorrowDetails() {
        return borrowDetails;
    }

    public void addBorrowDetail(BorrowBook borrowDetails) {
        this.borrowDetails.add(borrowDetails);
    }

    public void addReserveDetail(ReserveBook reserveDetails) {
        this.reservationDetails.add(reserveDetails);
    }

    public List<ReserveBook> getReservationDetails() {
        return reservationDetails;
    }
}
