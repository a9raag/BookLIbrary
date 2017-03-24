package viit.com.libraryviit.user;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anurag on 23/3/17.
 */

public class User implements Serializable {
    private String username;
    private ArrayList<String> booksReserved;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getBooksReserved() {
        return booksReserved;
    }

    public void setBooksReserved(ArrayList<String> booksReserved) {
        this.booksReserved = booksReserved;
    }



}
