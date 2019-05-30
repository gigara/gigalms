package classes.libraryItem;// Created by Gigara on 10/17/2018

import java.time.LocalDate;

import io.ebean.Model;
import javax.persistence.*;
import io.ebean.Finder;

@MappedSuperclass
public abstract class LibraryItem extends Model{
    @Id
    private int isbn;
    private String title;
    private String sector;
    private LocalDate publicationDate;


    public LibraryItem(int isbn, String title, String sector, LocalDate publicationDate) {
        this.isbn = isbn;
        this.title = title;
        this.sector = sector;
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getSector() {
        return sector;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

}
