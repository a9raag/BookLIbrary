package viit.com.libraryviit.db;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.fragments.BookRecyclerViewAdapter;

/**
 * Created by anurag on 23/2/17.
 */

public class FirebaseDBHelper {
    final DatabaseReference mDatabase;
    public static List<Book> bookList;
    public FirebaseDBHelper() {
         this.mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    public void search(final String text){
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mFirebaseDatabaseReference.orderByChild("title");
        query = mFirebaseDatabaseReference.orderByChild("title")
                .startAt(text)
                .endAt(text+"\uf8ff");


        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Book b = postSnapshot.getValue(Book.class);
                    Log.v(this.getClass().toString(), b.toString());
                    if (b.title.contains(text)) {
                        Log.v(this.getClass().toString(), b.title);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        query.addValueEventListener(valueEventListener);
    }
    public void insertBook(Book book){
        mDatabase.child(book.title).setValue(book);
    }
    public List<Book> readData(final RecyclerView recyclerView, final BookRecyclerViewAdapter bookRecyclerViewAdapter){
//        Book b = new Book("","");
//        this.insertBook(b);
//        this.mDatabase.child("").removeValue();
        final List<String> titles = new ArrayList<>();
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Book book = dataSnapshot.getValue(Book.class);
//                Log.v("Book Title " , book.getTitle());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        bookList = new ArrayList<>();
        this.mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count =1;
                for(DataSnapshot bookSnapShot : dataSnapshot.getChildren()){
                    Book  b = bookSnapShot.getValue(Book.class);
                    b.id= bookSnapShot.getKey();
                    bookList.add(b);
                    System.out.println(b.title);
                    count++;
                }
                bookRecyclerViewAdapter.mValues= bookList;
                recyclerView.setAdapter(bookRecyclerViewAdapter);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return bookList;

    }
public List<Book> readData(){
    System.out.println("Calling out read data");
    bookList = new ArrayList<>();

    this.mDatabase.addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            int count =1;


            for (DataSnapshot bookSnapShot : dataSnapshot.getChildren()) {
                Book b = bookSnapShot.getValue(Book.class);
                b.id = bookSnapShot.getKey();
                bookList.add(b);
                count++;
            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    System.out.println("This is a list of books "+ bookList.toString());
    return bookList;

}
}

