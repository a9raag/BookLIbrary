package viit.com.libraryviit.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import viit.com.libraryviit.adapters.RecyclerViewAdapter;
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
    public void search(final String text, final RecyclerView recyclerView, final Context context){
        final ArrayList<Book> bookArrayList = new ArrayList<>();
        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final Query query = mFirebaseDatabaseReference.orderByChild("title");
//                .startAt(text)
//                .endAt(text+"\uf8ff");


        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Book b = postSnapshot.getValue(Book.class);

                    if (b.title.toLowerCase().contains(text)) {

                        bookArrayList.add(b);
                    }

                }
                if(bookArrayList.size()==0)
                    Toast.makeText(context,"No results found of query "+query,Toast.LENGTH_SHORT);
                recyclerView.setAdapter(new RecyclerViewAdapter(bookArrayList, context));


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
    public int randRange(int min, int max){

        return new Random().nextInt((max - min) + 1) + min;
    }
    public void setRecyclerView(final RecyclerView recyclerView, final Context context){

        this.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*for (DataSnapshot alert: dataSnapshot.getChildren()) {
                    System.out.println(alert.getValue());
                }*/
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Book> values =new ArrayList<Book>();
                int count =0 ;
                for(DataSnapshot bookSnapShot : dataSnapshot.getChildren()){
                    Book b = bookSnapShot.getValue(Book.class);
                    if(b.title.length()<=25) {
                        b.id = bookSnapShot.getKey();
                        b.setReserveCount( String.valueOf(randRange(1, 10)));
                        Log.v("ReserveCount",b.reserveCount);
                        values.add(b);
                        count++;
                    }
                    if(count>20)
                        break;
                }
                recyclerView.setAdapter(new RecyclerViewAdapter(values, context));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("Failed to read value." + error.toException());
            }
        });

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

