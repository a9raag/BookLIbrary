package viit.com.libraryviit.user;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anurag on 23/3/17.
 */

public class User implements Serializable {
    private String username;
    private String id;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String department;
    private String emailName;
    private String memBegDate;
    private String memEndDate;
    private String mobileNumber;
    private ArrayList<String> reservedBooks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    public String getMemBegDate() {
        return memBegDate;
    }

    public void setMemBegDate(String memBegDate) {
        this.memBegDate = memBegDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private ArrayList<String> booksReserved;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getBooksReserved() {
        return reservedBooks;
    }

    public void setBooksReserved(ArrayList<String> booksReserved) {
        this.reservedBooks = booksReserved;
    }


    public String getMemEndDate() {
        return memEndDate;
    }

    public void setMemEndDate(String memEndDate) {
        this.memEndDate = memEndDate;
    }
}
