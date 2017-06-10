package viit.com.libraryviit.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import viit.com.libraryviit.R;
import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.fragments.DepartmentFragment;
import viit.com.libraryviit.fragments.ImageFrame;
import viit.com.libraryviit.fragments.RatingsFrame;
import viit.com.libraryviit.user.User;
import viit.com.libraryviit.user.UserProfileActivity;

public class BookDetailActivity extends AppCompatActivity implements ImageFrame.OnFragmentInteractionListener,RatingsFrame.OnFragmentInteractionListener{
    public Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        book = (Book) getIntent().getSerializableExtra("Book");
        System.out.println(book.toString());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final boolean[] ratingLoaded = {false};
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.rateFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText()
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        launchFragment(book.imageSmall);
        TextView title = (TextView) findViewById(R.id.book_title);
        TextView author = (TextView) findViewById(R.id.book_author);
        TextView sRating = (TextView) findViewById(R.id.book_s_rating);
        TextView fRating = (TextView) findViewById(R.id.book_f_rating);
        TextView description = (TextView) findViewById(R.id.description_data);
        Button reserve = (Button) findViewById(R.id.reserve);
        TextView isbn = (TextView) findViewById(R.id.book_isbn);
        title.setText(book.title);
        author.setText(book.author);
        sRating.setText(book.getRating());
        fRating.setText(book.getRating());
        isbn.setText(book.getIsbn());
        description.setText(book.description);
        reserve.setText("Reserve Count ("+book.reserveCount +")");
        Button reserves = (Button)findViewById(R.id.reserve);
        ArrayList<String> userReserved  =  new ArrayList<>();
        String username= getCurrentUser().replace("@viitlib.ac.in","").trim();
        reserves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReserveCount(v);
            }
        });
    }

    public String getCurrentUser(){
         FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getEmail();

    }
    public ArrayList<String> getReservedBooks(){
        final ArrayList<String>[] books = new ArrayList[]{new ArrayList<>()};
        String username= getCurrentUser().replace("@viitlib.ac.in","").trim();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);
        final Query query = mDatabase;
        final ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User u = dataSnapshot.getValue(User.class);
                books[0] = u.getBooksReserved();
                System.out.println(books[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        query.addValueEventListener(valueEventListener);

        return  books[0];
    }
    public void deleteReserves(){
        String username= getCurrentUser().replace("@viitlib.ac.in","").trim();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);
        final ArrayList<String> arrayList = new ArrayList<>();


        mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);
        mDatabase.child("booksReserved").setValue(new ArrayList<>());
    }
    public void reserveBooks(DatabaseReference finalMDatabase, User u){
        finalMDatabase.child("booksReserved").setValue(u.getBooksReserved());
    }
    public void reserveBook(final String isbn){
        String username= getCurrentUser().replace("@viitlib.ac.in","").trim();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);



        mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);
        final DatabaseReference finalMDatabase = mDatabase;
        final Query query = mDatabase;
        final ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User u = dataSnapshot.getValue(User.class);
                try {
                    if (u.getBooksReserved().size() == 0) {
                        final ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(isbn);
                        finalMDatabase.child("booksReserved").setValue(arrayList);
                    }
                }catch (NullPointerException n){
                    final ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(isbn);
                    finalMDatabase.child("booksReserved").setValue(arrayList);
                }
                if( u.getBooksReserved() != null && u.getBooksReserved().size() < 4) {
                    if (!u.getBooksReserved().contains(isbn)) {
                        ArrayList<String> data = u.getBooksReserved();
                        data.add(isbn);
                        finalMDatabase.child("booksReserved").setValue(data);

                    }
                }
                else if(u.getBooksReserved() != null && u.getBooksReserved().size() == 4){
                    Toast.makeText(getApplicationContext(),"Maximum reserve limit reached 4",Toast.LENGTH_SHORT).show();
                    int count = Integer.parseInt(book.reserveCount);
                    Button b = (Button) findViewById(R.id.reserve);
                    count--;
                    book.reserveCount = String.valueOf(count);
                    b.setText("Reserve Count (" + count + ")");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        query.addValueEventListener(valueEventListener);


    }
    public static int randRagne(int min, int max){
        return new Random().nextInt((max - min) + 1) + min;
    }
    public boolean reserved = false;
    public void updateReserveCount(View v){

        if (reserved) {
            reserved =false;
            int count = Integer.parseInt(book.reserveCount);
            Button b = (Button) findViewById(R.id.reserve);
            count--;
            book.reserveCount = String.valueOf(count);
            b.setText("Reserve Count (" + count + ")");
        }
        else if (!reserved){

            reserved =true;
            int count = Integer.parseInt(book.reserveCount);
            Button b = (Button) findViewById(R.id.reserve);
            count++;
            book.reserveCount = String.valueOf(count);
            b.setText("Un-reserve (" + count + ")");
            reserveBook(book.getIsbn());
        }
    }
    public void launchFragment(String url){
        Fragment imageFrame = new ImageFrame().newInstance(url);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bgFrame, imageFrame);
        transaction.commit();
    }

    public void picassoLoader(Context context, ImageView imageView, String url){
        try {
            Picasso.with(context)
                    .load(url)
                    //.resize(30,30)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }catch (Exception e){
            Picasso.with(context)
                    .load("http://img.clipartall.com/boy-reading-a-book-clip-art-reading-book-clipart-436_500.png")
                    //.resize(30,30)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }

    }
    public void updateRating(View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("0").child("rating").setValue("5 ");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
