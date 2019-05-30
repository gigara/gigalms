package controllers;// Created by Gigara on 10/25/2018

import classes.DateTime;
import classes.LibraryManager;
import classes.Reader;
import classes.borrowDetails.BorrowBook;
import classes.borrowDetails.BorrowDVD;
import classes.libraryItem.Book;
import classes.libraryItem.DVD;
import classes.libraryItem.LibraryItem;
import classes.reserveDetails.Reserve;
import classes.reserveDetails.ReserveBook;
import classes.reserveDetails.ReserveDVD;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.time.LocalDate;
import java.util.*;

public class WestminsterLibraryManager extends Controller implements LibraryManager {

    // add new book
    @Override
    public Result addBook() {
        ObjectNode response = Json.newObject();

        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        //extract data from the post request
        int isbn = Integer.parseInt(body.get("isbn")[0]);
        String title = body.get("title")[0];
        String sector = body.get("sector")[0];
        LocalDate pubDate = LocalDate.parse(body.get("pubDate")[0]);
        List<String> authors = Arrays.asList(body.get("author"));
        String publisher = body.get("publisher")[0];
        int pages = Integer.parseInt(body.get("pages")[0]);

        //creating the book objects
        Book book = new Book(isbn, title, sector, pubDate, authors, publisher, pages);

        //check database
        if (isAvailable(isbn)) {
            response.put("error", "Item already available");

        } else if (Book.find.all().size() == 100) {
            response.put("error", "Size Limit Exceeds");


        } else {
            //saving item into the database
            book.save();
            response.put("success", "Book Added Successfully");
        }

        response.put("Books", 100 - Book.find.all().size());
        response.put("DVDs", 50 - DVD.find.all().size());
        return ok(response);
    }

    // add new dvd
    @Override
    public Result addDVD() {
        ObjectNode response = Json.newObject();
        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        int isbn = Integer.parseInt(body.get("isbn")[0]);
        String title = body.get("title")[0];
        String sector = body.get("sector")[0];
        LocalDate pubDate = LocalDate.parse(body.get("pubDate")[0]);
        String language = body.get("language")[0];
        List<String> subtitles = Arrays.asList(body.get("subtitle"));
        String producer = body.get("producer")[0];
        List<String> actors = Arrays.asList(body.get(("actors")));

        //creating the DVD object
        DVD dvd = new DVD(isbn, title, sector, pubDate, language, subtitles, producer, actors);

        //check database
        if (isAvailable(isbn)) {
            response.put("error", "Item already available");

        } else if (Book.find.all().size() == 50) {
            response.put("error", "Size Limit Exceeds");


        } else {
            //saving item into the database
            dvd.save();
            response.put("success", "DVD Added Successfully");
        }
        response.put("Books", 100 - Book.find.all().size());
        response.put("DVDs", 50 - DVD.find.all().size());
        return ok(response);
    }

    //delete item
    //@param isbn - isbn
    @Override
    public Result deleteItem(int isbn) {
        ObjectNode response = Json.newObject();

        if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {
            Book book = Book.find.byId(isbn);
            book.delete();

            response.put("status", "Book: " + book.getTitle() + " deleted successfully");

        } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {
            DVD dvd = DVD.find.byId(isbn);
            dvd.delete();

            response.put("status", "DVD: " + dvd.getTitle() + " deleted successfully");

        } else {
            response.put("status", "Item Not Found");
        }

        response.put("Books", 100 - Book.find.all().size());
        response.put("DVDs", 50 - DVD.find.all().size());
        return ok(response);
    }

    // list all
    @Override
    public Result listAll() {
        List<Book> books = Book.find.all();
        List<DVD> dvds = DVD.find.all();

        ArrayNode node1 = ((ArrayNode) Json.toJson(books));
        ArrayNode node2 = ((ArrayNode) Json.toJson(dvds));

        Map<String, ArrayNode> map = new HashMap<>();
        map.put("Books", node1);
        map.put("DVDs", node2);

        return ok(Json.toJson(map));
    }

    // borrow an item
    @Override
    public Result borrowItem() {
        ObjectNode response = Json.newObject();

        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        try {
            int isbn = Integer.parseInt(body.get("isbn")[0]);
            int readerID = Integer.parseInt(body.get("reader")[0]);

            int date = Integer.parseInt(body.get("date")[0]);
            int month = Integer.parseInt(body.get("month")[0]);
            int year = Integer.parseInt(body.get("year")[0]);
            int minute = Integer.parseInt(body.get("minute")[0]);
            int hour = Integer.parseInt(body.get("hour")[0]);
            DateTime time = new DateTime(date, month, year, minute, hour);

            Reader reader = Reader.find.byId(readerID);
            if (reader == null) {
                response.put("status", "Reader Not found");
            }
            //set return time into 0
            DateTime returnTime = new DateTime(0, 0, 0, 0, 0);

            //Book
            if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {

                BorrowBook borrowBook = new BorrowBook(reader, time, returnTime);

                Book book = Book.find.byId(isbn);

                //checking for book availability from the last borrow details
                List<BorrowBook> list = book.getBorrowDetails();

                if ((!list.isEmpty()) && (list.get(list.size() - 1).getReturnedTime().getDate() == 0)) {

                    int predictedTime = predict(isbn);
                    int passedTime = DateTime.hourDiffrence(list.get(list.size() - 1).getBorrowedTime());

                    String availableIn = "";

                    if (predictedTime == 0) {
                        availableIn = "soon";
                    } else {
                        availableIn = String.valueOf((predictedTime - passedTime)) + " hours";
                    }

                    response.put("status", "Book has been already borrowed by someone. It will be available in " + availableIn);
                } else {
                    book.addBorrowDetail(borrowBook);
                    book.update();

                    response.put("status", "Book: " + book.getTitle() + " has been successfully marked as borrowed by " + reader.getName());
                }

                //DVD
            } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {

                BorrowDVD borrowDVD = new BorrowDVD(reader, time, returnTime);
                DVD dvd = DVD.find.byId(isbn);

                //checking for book availability from the last borrow details
                List<BorrowDVD> list = dvd.getBorrowDetails();

                if ((!list.isEmpty()) && (list.get(list.size() - 1).getReturnedTime().getDate() == 0)) {

                    int predictedTime = predict(isbn);
                    int passedTime = DateTime.hourDiffrence(list.get(list.size() - 1).getBorrowedTime());

                    String availableIn = "";

                    if (predictedTime == 0) {
                        availableIn = "soon";
                    } else {
                        availableIn = String.valueOf((predictedTime - passedTime)) + " hours";
                    }

                    response.put("staus", "DVD has been already borrowed by someone It will be available in " + availableIn);
                } else {
                    dvd.addBorrowDetail(borrowDVD);
                    dvd.update();

                    response.put("status", "DVD: " + dvd.getTitle() + " has been successfully marked as borrowed by " + reader.getName());
                }
            } else {
                response.put("status", "Item Not Found");
            }


        } catch (Exception e) {
            response.put("status", e.getMessage());
            return ok(response);
        }

        return ok(response);
    }

    // return an item
    @Override
    public Result returnItem() throws Exception {
        ObjectNode response = Json.newObject();
        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        try {
            int isbn = Integer.parseInt(body.get("isbn")[0]);

            int date = Integer.parseInt(body.get("date")[0]);
            int month = Integer.parseInt(body.get("month")[0]);
            int year = Integer.parseInt(body.get("year")[0]);
            int minute = Integer.parseInt(body.get("minute")[0]);
            int hour = Integer.parseInt(body.get("hour")[0]);
            DateTime time = new DateTime(date, month, year, minute, hour);

            if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {

                Book book = Book.find.byId(isbn);

                //checking for book availability from the last borrow details
                List<BorrowBook> list = book.getBorrowDetails();

                if ((list.isEmpty()) || (list.get(list.size() - 1).getReturnedTime().getDate() != 0)) {

                    response.put("status", "Book is available in the library");
                } else {
                    //mark as returned
                    BorrowBook borrowDetail = list.get(list.size() - 1);
                    borrowDetail.setReturnedTime(time);
                    book.setBorrowDetails(list);
                    book.update();

                    int holdTime = DateTime.hourDiffrence(borrowDetail.getBorrowedTime(), time);
                    if (holdTime > 168) {
                        int extraHours = Math.toIntExact(holdTime - 168);
                        double fine;
                        //calculate the fee
                        if (extraHours <= 72) {
                            fine = 0.2 * extraHours;
                        } else {
                            fine = 14.4 + (0.5 * extraHours);
                        }

                        response.put("status", "You have to pay " + String.valueOf(fine));
                    } else if (holdTime < 0) {
                        response.put("status", "Invalid Date");
                    } else {
                        response.put("status", "Book Successfully returned");
                    }

                }

            } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {

                DVD dvd = DVD.find.byId(isbn);

                //checking for book availability from the last borrow details
                List<BorrowDVD> list = dvd.getBorrowDetails();

                if ((list.isEmpty()) || (list.get(list.size() - 1).getReturnedTime().getDate() != 0)) {

                    response.put("status", "Book is available in the library");
                } else {
                    //mark as returned
                    BorrowDVD borrowDetail = list.get(list.size() - 1);
                    borrowDetail.setReturnedTime(time);
                    dvd.setBorrowDetails(list);
                    dvd.update();

                    int holdTime = DateTime.hourDiffrence(borrowDetail.getBorrowedTime(), time);
                    if (holdTime > 72) {
                        int extraHours = Math.toIntExact(holdTime - 72);
                        double fine;
                        //calculate the fee
                        if (extraHours <= 72) {
                            fine = 0.2 * extraHours;
                        } else {
                            fine = 14.4 + (0.5 * extraHours);
                        }

                        response.put("status", "You have to pay " + String.valueOf(fine));
                    } else if (holdTime < 0) {
                        response.put("status", "Invalid Date");
                    } else {
                        response.put("status", "Book Successfully returned");


                    }
                }
            } else {
                response.put("status", "Item Not Found");
            }

        } catch (Exception e) {
            response.put("status", e.getMessage());
            return ok(response);
        }

        return ok(response);
    }

    // generate a report
    @Override
    public Result report() {
        ObjectNode response = Json.newObject();
        try {
            //all
            List<Book> books = Book.find.all();
            List<DVD> dvds = DVD.find.all();

            //not returned
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode bookArray = mapper.createArrayNode();
            ArrayNode dvdArray = mapper.createArrayNode();

            //find books
            books.forEach(book -> {
                List<BorrowBook> list = book.getBorrowDetails();

                if ((!list.isEmpty()) && (list.get(list.size() - 1).getReturnedTime().getDate() == 0)) {
                    BorrowBook lstBorrwDetails = list.get(list.size() - 1);
                    try {

                        if (DateTime.hourDiffrence(lstBorrwDetails.getBorrowedTime()) > 168) {
                            //add into the list
                            ObjectNode objectNode = mapper.createObjectNode();
                            objectNode.put("isbn", book.getIsbn());
                            objectNode.put("title", book.getTitle());
                            objectNode.put("reader", lstBorrwDetails.getReader().getName());
                            objectNode.put("borrowedDate", lstBorrwDetails.getBorrowedTime().getDate() + "-" + lstBorrwDetails.getBorrowedTime().getMonth() + "-" + lstBorrwDetails.getBorrowedTime().getYear());

                            //calculate fee
                            int holdTime = DateTime.hourDiffrence(lstBorrwDetails.getBorrowedTime());

                            int extraHours = Math.toIntExact(holdTime - 168);
                            double fine;
                            //calculate the fee
                            if (extraHours <= 72) {
                                fine = 0.2 * extraHours;
                            } else {
                                fine = 14.4 + (0.5 * extraHours);
                            }
                            objectNode.put("fine", fine);

                            bookArray.add(objectNode);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            //find dvds
            dvds.forEach(dvd -> {
                List<BorrowDVD> list = dvd.getBorrowDetails();

                if ((!list.isEmpty()) && (list.get(list.size() - 1).getReturnedTime().getDate() == 0)) {
                    BorrowDVD lstBorrwDetails = list.get(list.size() - 1);
                    try {

                        if (DateTime.hourDiffrence(lstBorrwDetails.getBorrowedTime()) > 72) {
                            //add into the list
                            ObjectNode objectNode = mapper.createObjectNode();
                            objectNode.put("isbn", dvd.getIsbn());
                            objectNode.put("title", dvd.getTitle());
                            objectNode.put("reader", lstBorrwDetails.getReader().getName());
                            objectNode.put("borrowedDate", lstBorrwDetails.getBorrowedTime().getDate() + "-" + lstBorrwDetails.getBorrowedTime().getMonth() + "-" + lstBorrwDetails.getBorrowedTime().getYear());

                            //calculate fee
                            int holdTime = DateTime.hourDiffrence(lstBorrwDetails.getBorrowedTime());

                            int extraHours = Math.toIntExact(holdTime - 72);
                            double fine;
                            //calculate the fee
                            if (extraHours <= 72) {
                                fine = 0.2 * extraHours;
                            } else {
                                fine = 14.4 + (0.5 * extraHours);
                            }
                            objectNode.put("fine", fine);

                            dvdArray.add(objectNode);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            Map<String, ArrayNode> json = new HashMap<>();
            /*json.put("Books",bookArray);
            json.put("DVDs",dvdArray);*/
            ArrayNode all = mapper.createArrayNode();
            all.addAll(bookArray);
            all.addAll(dvdArray);

            json.put("all", all);

            //return the list
            return ok(Json.toJson(json));

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status","Some error occured");
        }
        response.put("status","No Items Found");
        return ok(response);
    }

    //add new reader
    @Override
    public Result addReader() {
        ObjectNode response = Json.newObject();

        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        try {
            String name = body.get("name")[0];
            int mobNumber = Integer.parseInt(body.get("mobNumber")[0]);
            String email = body.get("email")[0];

            //create reader obj
            Reader reader = new Reader(name, mobNumber, email);
            reader.save();

            List<Reader> readers = Reader.find.all();

            //return reader ID
            response.put("status", "User " + name + " registed successfully\nUser ID: " + readers.get(readers.size() - 1).getID());

        } catch (Exception e) {
            response.put("status", e.getMessage());
            return ok(response);
        }
        return ok(response);
    }

    @Override
    public Result find(int id) {
        int book = Book.find.query().where().eq("isbn", id).findCount();

        return ok(String.valueOf(book));
    }

    @Override
    public Result reserve() {
        ObjectNode response = Json.newObject();

        // get data from the post request
        Map<String, String[]> body = request().body().asFormUrlEncoded();

        try {
            int isbn = Integer.parseInt(body.get("isbn")[0]);
            int readerID = Integer.parseInt(body.get("reader")[0]);

            int date = Integer.parseInt(body.get("date")[0]);
            int month = Integer.parseInt(body.get("month")[0]);
            int year = Integer.parseInt(body.get("year")[0]);
            int minute = Integer.parseInt(body.get("minute")[0]);
            int hour = Integer.parseInt(body.get("hour")[0]);
            DateTime time = new DateTime(date, month, year, minute, hour);

            Reader reader = Reader.find.byId(readerID);
            if (reader == null) {
                response.put("status","Reader not found");
            }

            if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {
                Book book = Book.find.byId(isbn);

                ReserveBook reserve = new ReserveBook(reader, time);

                book.addReserveDetail(reserve);
                book.update();
                response.put("status", book.getTitle()+ " Reserved to the "+reader.getName());

            } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {
                DVD dvd = DVD.find.byId(isbn);

                ReserveDVD reserve = new ReserveDVD(reader, time);

                dvd.addReserveDetail(reserve);
                dvd.update();
                response.put("status", dvd.getTitle()+ " Reserved to the "+reader.getName());

            } else {
                response.put("status","Item not found");
            }

        } catch (Exception e) {
            response.put("status", e.getMessage());
            return ok(response);
        }

        return ok(response);
    }

    //search item in the database
    @Override
    public Result search(String title) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode bookArray = mapper.createArrayNode();
        ArrayNode dvdArray = mapper.createArrayNode();

        List<Book> books = Book.find.query().where().raw("LOWER(title) like ?", title + "%").findList();
        if (books.size() > 0) {
            books.forEach( book -> {
                //add into the list
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("isbn", book.getIsbn());
                objectNode.put("title", book.getTitle());
                objectNode.put("sector", book.getSector());
                objectNode.put("pubDate", book.getPublicationDate().toString());

                bookArray.add(objectNode);
            });
        }

        List<DVD> dvds = DVD.find.query().where().raw("LOWER(title) like ?", title + "%").findList();
        if (dvds.size() > 0) {
            dvds.forEach( dvd -> {
                //add into the list
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("isbn", dvd.getIsbn());
                objectNode.put("title", dvd.getTitle());
                objectNode.put("sector", dvd.getSector());
                objectNode.put("pubDate", dvd.getPublicationDate().toString());

                dvdArray.add(objectNode);
            });
        }

        Map<String, ArrayNode> json = new HashMap<>();
        ArrayNode all = mapper.createArrayNode();
        all.addAll(bookArray);
        all.addAll(dvdArray);

        json.put("all", all);

        //return the list
        return ok(Json.toJson(all));
    }

    public static LibraryItem displayItem(int isbn) throws Exception {
        LibraryItem item;

        if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {
            item = Book.find.byId(isbn);

        } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {
            item = DVD.find.byId(isbn);

        } else {
            throw new Exception("Book Not Found");
        }

        return item;
    }





    //check availability of a item in the database
    public boolean isAvailable(int isbn) {
        if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {
            return true;

        } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {
            return true;

        } else {
            return false;
        }
    }


    //calculate the estimate of when book available by calculating the average of past hold on times
    public int predict(int isbn) throws Exception {

        if (Book.find.query().where().eq("isbn", isbn).findCount() == 1) {
            Book book = Book.find.byId(isbn);

            //past borrwed details
            List<BorrowBook> borrowDetails = book.getBorrowDetails();

            //return 0 if not found enough data to calculate the average
            if (borrowDetails.size() == 1) return 0;

            int allHours = 0;
            for (int i = 0; i < borrowDetails.size() - 1; i++) {
                allHours += DateTime.hourDiffrence(book.getBorrowDetails().get(i).getBorrowedTime(), book.getBorrowDetails().get(i).getReturnedTime());
            }

            //calculate avg
            int avg = (allHours / (borrowDetails.size() - 1));

            int reservations = 1;
            //check for reservations
            if (book.getReservationDetails().size() > 0) {
                for (int i = 0; i < book.getReservationDetails().size(); i++) {
                    //get only future reservations
                    if (DateTime.hourDiffrence(book.getReservationDetails().get(i).getReserveTime()) < 0) {
                        reservations++;
                    }
                }
            }
            return (avg * reservations);

        } else if (DVD.find.query().where().eq("isbn", isbn).findCount() == 1) {
            DVD dvd = DVD.find.byId(isbn);

            //past borrwed details
            List<BorrowDVD> borrowDetails = dvd.getBorrowDetails();

            //return 0 if not found enough data to calculate the average
            if (borrowDetails.size() == 1) return 0;

            int allHours = 0;
            for (int i = 0; i < borrowDetails.size() - 1; i++) {
                allHours += DateTime.hourDiffrence(dvd.getBorrowDetails().get(i).getBorrowedTime(), dvd.getBorrowDetails().get(i).getReturnedTime());
            }

            //calculate avg
            int avg = (allHours / (borrowDetails.size() - 1));

            int reservations = 1;
            //check for reservations
            if (dvd.getReservationDetails().size() > 0) {
                for (int i = 0; i < dvd.getReservationDetails().size(); i++) {
                    //get only future reservations
                    if (DateTime.hourDiffrence(dvd.getReservationDetails().get(i).getReserveTime()) < 0) {
                        reservations++;
                    }
                }
            }
            return (avg * reservations);

        } else return 0;
    }
}
