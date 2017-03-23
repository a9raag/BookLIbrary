package viit.com.libraryviit.book;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anurag on 23/2/17.
 */

public class Book implements Parcelable{
    public String imageSmall;
    public String imageLarge;
    public String id;
    public String title;
    public String author;
    public String studentRating;
    public String profRating;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(this.title);
        b.append("\n");
        b.append(this.author);
        b.append("\n");
        b.append(this.isbn);
        b.append("\n");
        b.append(this.pubYear);
        b.append("\n");
        b.append(this.profRating);
        return b.toString();
    }

    public Book(){}

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }

    public String pubYear;
    public String isbn;

    protected Book(Parcel in) {
        imageSmall = in.readString();
        imageLarge = in.readString();
        id = in.readString();
        title = in.readString();
        author = in.readString();
        pubYear = in.readString();
        studentRating = in.readString();
        profRating = in.readString();
        isbn = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
        this.title = title;
        this.author = author;
        this.studentRating ="4.0";
        this.profRating = "5.0";
        this.isbn = "1234567890";
        this.imageSmall = "";
        this.imageLarge= "";
        this.pubYear ="";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageSmall);
        parcel.writeString(imageLarge);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(studentRating);
        parcel.writeString(profRating);
        parcel.writeString(isbn);
        parcel.writeString(pubYear);

    }
}
