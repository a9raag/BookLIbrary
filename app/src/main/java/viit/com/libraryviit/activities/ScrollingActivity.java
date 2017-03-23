package viit.com.libraryviit.activities;

import android.content.Context;
import android.net.Uri;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Random;

import viit.com.libraryviit.R;
import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.fragments.DepartmentFragment;
import viit.com.libraryviit.fragments.ImageFrame;
import viit.com.libraryviit.fragments.RatingsFrame;

public class ScrollingActivity extends AppCompatActivity implements ImageFrame.OnFragmentInteractionListener,RatingsFrame.OnFragmentInteractionListener{
    public Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        book = getIntent().getParcelableExtra("Book");
        System.out.println(book.toString());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final boolean[] ratingLoaded = {false};
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.rateFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ratingFrame = new RatingsFrame().newInstance(book.id);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(!ratingLoaded[0]){

                    transaction.add(R.id.bgFrame, ratingFrame);

                    transaction.commit();
                    ratingLoaded[0] =true;
                }
                else{
                    launchFragment(book.imageLarge);
                    ratingLoaded[0]=false;
                }
            }
        });


        launchFragment(book.imageLarge);
        book.reserveCount=String.valueOf(randRagne(1, 10));
        TextView title = (TextView) findViewById(R.id.book_title);
        TextView author = (TextView) findViewById(R.id.book_author);
        TextView sRating = (TextView) findViewById(R.id.book_s_rating);
        TextView fRating = (TextView) findViewById(R.id.book_f_rating);
        Button reserve = (Button) findViewById(R.id.reserve);
        TextView isbn = (TextView) findViewById(R.id.book_isbn);
        title.setText(book.title);
        author.setText(book.author);
        sRating.setText(book.getRating());
        fRating.setText(book.getRating());
        isbn.setText(book.getIsbn());
        reserve.setText("Reserve Count ("+book.reserveCount +")");



    }
    public int randRagne(int min, int max){
        return new Random().nextInt((max - min) + 1) + min;
    }

    public void updateReserveCount(View v){
        int count = Integer.parseInt(book.reserveCount);
        Button b = (Button) findViewById(R.id.reserve);
        b.setEnabled(false);
        count++;
        book.reserveCount=String.valueOf(count);
        b.setText("Reserve Count ("+count+")");
    }
    public void launchFragment(String url){
        Fragment imageFrame = new ImageFrame().newInstance(url);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.bgFrame, imageFrame);
        transaction.commit();
    }

    public void updateRating(View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("0").child("rating").setValue("5 ");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
