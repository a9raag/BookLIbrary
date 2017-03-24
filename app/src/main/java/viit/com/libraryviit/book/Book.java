package viit.com.libraryviit.book;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by anurag on 23/2/17.
 */

public class Book implements Serializable{
    public String imageSmall;
    public String imageLarge;
    public String id;
    public String title;
    public String author;
    private String studentRating;
    private String profRating;
    private String pubYear;
    private String isbn;
    public String reserveCount;
    public ArrayList<String> reservers;
    public String getReserveCount() {
        return reserveCount;
    }

    public void setReserveCount(String reserveCount) {
        this.reserveCount = reserveCount;
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Title :"+this.title);
        b.append("\n");
        b.append("Author :"+this.author);
        b.append("\n");
        b.append("ISBN :"+this.isbn);
        b.append("\n");
        b.append("Publication Year :"+this.pubYear);
        b.append("\n");
        b.append("Professor's rating :"+this.profRating);
        b.append("\n");
        b.append("Reserve Count :"+this.reserveCount);
        return b.toString();
    }

    public Book(){}

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }



    protected Book(Parcel in) {
        imageSmall = in.readString();
        imageLarge = in.readString();
        id = in.readString();
        title = in.readString();
        author = in.readString();
        studentRating = in.readString();
        profRating = in.readString();
        isbn = in.readString();
        pubYear = in.readString();






    }

//    public static final Creator<Book> CREATOR = new Creator<Book>() {
//        @Override
//        public Book createFromParcel(Parcel in) {
//            return new Book(in);
//        }
//
//        @Override
//        public Book[] newArray(int size) {
//            return new Book[size];
//        }
//    };

    public String getImageSmall() { return imageSmall; }

    public void setImageSmall(String imageSmall) {this.imageSmall = imageSmall; }
    public String getRating() {
        return studentRating;
    }

    public void setRating(String studentRating) {
        this.studentRating = studentRating;
        this.profRating = studentRating;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProfRating() {
        return profRating;
    }

    public void setProfRating(String profRating) {
        this.profRating = profRating;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public Book(String title, String author, String studentRating, String profRating, String isbn) {
        this.title = title;
        this.author = author;
        this.studentRating = studentRating;
        this.profRating = profRating;
        this.isbn = isbn;
    }

    public Book(String title, String author) {
        this.id= "0";
        this.title = title;
        this.author = author;
        this.studentRating ="4.0";
        this.profRating = "5.0";
        this.isbn = "1234567890";
        this.imageSmall = "";
        this.imageLarge= "";
        this.pubYear ="";
        this.reserveCount="0";
        this.reservers=new ArrayList<>();
        reservers.add("anurag");
        reservers.add("krushika");
    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(imageSmall);
//        parcel.writeString(imageLarge);
//        parcel.writeString(id);
//        parcel.writeString(title);
//        parcel.writeString(author);
//        parcel.writeString(studentRating);
//        parcel.writeString(profRating);
//        parcel.writeString(isbn);
//        parcel.writeString(pubYear);
//
//    }
}
