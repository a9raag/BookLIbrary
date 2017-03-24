package viit.com.libraryviit.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import viit.com.libraryviit.R;
import viit.com.libraryviit.adapters.RecyclerViewAdapter;
import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.db.FirebaseDBHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private  static final String ARG_QUERY = "query";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String query;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookFragment newInstance(int columnCount) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public static BookFragment newInstance(String query) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            query = getArguments().getString(ARG_QUERY);
        }
    }
    public static BookRecyclerViewAdapter adapter;
    public static List<Book> bookList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        final View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new GetDataFromFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        final FirebaseDBHelper dbHelper = new FirebaseDBHelper();
        if(query != null){
            System.out.println("Searching for "+query);

            dbHelper.search(query,recyclerView,getContext());

        }
        else {
            dbHelper.setRecyclerView(recyclerView, getContext());
        }
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                        dbHelper.setRecyclerView(recyclerView,getContext());
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public int randRagne(int min, int max){

        return new Random().nextInt((max - min) + 1) + min;
    }
//    @Override
//    public void onClick(View v, int position, String s) {
//        BookFragment bookFragment = BookFragment.newInstance(position);
//        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
//        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
//        // ARE available in the support library (though they don't do anything on API < 21)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            bookFragment.setSharedElementEnterTransition(new BookTransition());
//            bookFragment.setEnterTransition(new Fade());
//            setExitTransition(new Fade());
//            bookFragment.setSharedElementReturnTransition(new BookTransition());
//        }
//
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .addSharedElement(v, s)
//                .replace(R.id.flContent, bookFragment)
//                .addToBackStack(null)
//                .commit();
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Book item);
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
}
