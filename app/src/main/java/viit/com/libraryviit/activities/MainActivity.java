package viit.com.libraryviit.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.common.primitives.Ints;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import viit.com.libraryviit.BuildConfig;
import viit.com.libraryviit.R;
import viit.com.libraryviit.adapters.RecyclerViewAdapter;
import viit.com.libraryviit.barcode.BarcodeCaptureActivity;
import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.book.BookFragment;
import viit.com.libraryviit.db.FirebaseDBHelper;
import viit.com.libraryviit.fragments.BookGridViewFragment;
import viit.com.libraryviit.fragments.DepartmentFragment;
import viit.com.libraryviit.notification.BookDueNotification;
import viit.com.libraryviit.user.User;
import viit.com.libraryviit.user.UserFragment;
import viit.com.libraryviit.user.UserProfileActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BookFragment.OnListFragmentInteractionListener,
        RecyclerViewAdapter.ItemClick, DepartmentFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener, BookGridViewFragment.OnFragmentInteractionListener{

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "MainActivity" ;
    DrawerLayout mDrawer;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isNetworkAvailable()){
            final RelativeLayout layout = (RelativeLayout) findViewById(R.id.no_internet);
            layout.setVisibility(View.VISIBLE);
            Button retry = (Button) findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        layout.setVisibility(View.GONE);
                    }
                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);   

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Add a list of books fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final FirebaseDBHelper dbHelper = new FirebaseDBHelper();
//        dbHelper.insertBook(new Book("Game of Thrones","RR Martin"));
//        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        mDatabase = FirebaseDatabase.getInstance().getReference("user/1302388");
//
//        final Query query = mDatabase;
//
//        ValueEventListener valueEventListener = new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                User u = dataSnapshot.getValue(User.class);
//                System.out.println(u.getFirstName());
//                ArrayList<String> booksReserved = new ArrayList<>();
////                booksReserved.add("0070722064");
////                u.setBooksReserved(booksReserved);
//                mDatabase.setValue(u);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//
//            }
//        };
//        query.addValueEventListener(valueEventListener);
        Fragment gridViewFragment = new BookGridViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flContent, gridViewFragment);
        transaction.commit();

        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        String username = shared.getString("username",null);
        String password =shared.getString("password",null);
        if(shared.contains("username") && shared.contains("password")) {
            signInUser(username, password);
        }

    }

    public List<Volume> search(String query){
        // If the query seems to be an ISBN we add the isbn special keyword https://developers.google.com/books/docs/v1/using#PerformingSearch
        if (Ints.tryParse(query) != null && (query.length() == 13 || query.length() == 10)) {
            query = query.concat("+isbn:" + query);
        }

        // Creates the books api client
        Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                .setApplicationName(BuildConfig.APPLICATION_ID)
                .build();

        try {
            // Executes the query
            Books.Volumes.List list = books.volumes().list(query).setProjection("LITE");
            return list.execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public void loadRegisterFragment(){

    }
    public void loadDepartmentFragment(){
        Fragment deptFragment = new DepartmentFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.flContent, deptFragment);
        transaction.commit();
    }
    public void signInUser(String username, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication Failed username or password incorrect try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
//
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                            finish();
                            Toast.makeText(getApplicationContext(),"Authentication Successful",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private SimpleCursorAdapter mAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final String[] from = new String[] {"BookName"};
        final int[] to = new int[] {R.id.search_item};
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.search_row,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment bookFragment = new BookFragment().newInstance(query);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, bookFragment);
                transaction.addToBackStack("BookList");
                transaction.commit();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return false;

            }
        });
        return true;
    }
    public String getCurrentUser(){
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getEmail();

    }
    public void loadProfile(View v){
        String username= getCurrentUser().replace("@viitlib.ac.in","").trim();
        BookDueNotification.notify(getApplicationContext(),"Book is due",0);
        mDrawer.closeDrawers();
        if(isNetworkAvailable()) {
            new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);

            final Query query = mDatabase;

            ValueEventListener valueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    User u = dataSnapshot.getValue(User.class);
//                booksReserved.add("0070722064");
//                u.setBooksReserved(booksReserved);
                    mDatabase.setValue(u);
//                    Fragment userFragment = new UserFragment().newInstance(u);
//
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.flContent, userFragment);
//                    transaction.commit();
                    Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.putExtra("user",u);
                    startActivity(i);
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };
            query.addValueEventListener(valueEventListener);

        }
    }
    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "BookName"});
        for (int i = 0; i < SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[]{i, SUGGESTIONS[i]});
        }
        mAdapter.changeCursor(c);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void selectDepartment(View v){
        Fragment bookFragment = new BookFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, bookFragment);
        transaction.addToBackStack("BookList");
        transaction.commit();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, true);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        } else if (id == R.id.booklist) {
            Fragment bookFragment = new BookFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flContent, bookFragment);
            transaction.addToBackStack("BookList");
            transaction.commit();

        } else if (id == R.id.departments) {
            SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.clear();
            editor.commit();

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v, int position, Book b) {
        Intent intent = new Intent(this, BookDetailActivity.class);

        intent.putExtra("Book", b);
        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this, v, getString(R.string.transition_item));
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
    @Override
    public void onListFragmentInteraction(Book item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class GetDataFromFirebase extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    statusMessage.setText(R.string.barcode_success);
//                    barcodeValue.setText(barcode.displayValue);
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
//                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Log.v(TAG,String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
