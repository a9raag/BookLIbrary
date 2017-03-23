package viit.com.libraryviit.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import viit.com.libraryviit.R;
import viit.com.libraryviit.book.Book;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Book book = getIntent().getParcelableExtra("Book");
        System.out.println("ScrollingActivity"+ book.imageLarge);
        ImageView v = (ImageView) findViewById(R.id.bgImage);
        picassoLoader(this,v, book.imageLarge);
        TextView title = (TextView) findViewById(R.id.book_title);
        TextView author = (TextView) findViewById(R.id.book_author);
        TextView sRating = (TextView) findViewById(R.id.book_s_rating);
        TextView fRating = (TextView) findViewById(R.id.book_f_rating);
        TextView isbn = (TextView) findViewById(R.id.book_isbn);
        title.setText(book.title);

        author.setText(book.author);
        sRating.setText("4.5");
        fRating.setText("3.5");
        isbn.setText(book.getIsbn());



    }
    public void updateRating(View v){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("0").child("rating").setValue("5 ");
    }
    public void picassoLoader(Context context, ImageView imageView, String url){
        Log.d("PICASSO", "loading imageSmall");
        Picasso.with(context)
                .load(url)
                //.resize(30,30)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
