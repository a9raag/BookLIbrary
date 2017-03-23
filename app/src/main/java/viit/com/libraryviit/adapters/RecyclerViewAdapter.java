package viit.com.libraryviit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import viit.com.libraryviit.R;
import viit.com.libraryviit.book.Book;
import viit.com.libraryviit.fragments.MyCardView;

/**
 * Created by anurag on 20/3/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Book> booksList;
    private ItemClick itemClick;
    public Context ctx;
    public RecyclerViewAdapter(ArrayList<Book> booksList, Context ctx) {
        this.ctx = ctx;
        this.booksList = booksList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = new MyCardView(parent.getContext());
//        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row, parent, false));
        itemClick = (ItemClick) parent.getContext();
        return new ViewHolder(v);

    }

    public void picassoLoader(Context context, ImageView imageView, String url){
        Picasso.with(context)
                .load(url)
                //.resize(30,30)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Book b = booksList.get(position);
        holder.name.setText(b.title);
        holder.author.setText(b.author);
        holder.rating.setText(b.getProfRating().toString());
        picassoLoader(ctx,holder.imageView,b.imageSmall);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemClick.onClick(view, holder.getAdapterPosition(), b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        public View mView;
        private TextView author;
        private TextView rating;
        private ImageView imageView;
        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            author = (TextView) itemView.findViewById(R.id.book_author);
            rating = (TextView) itemView.findViewById(R.id.book_s_rating);
            name = (TextView) itemView.findViewById(R.id.book_title);
            imageView = (ImageView) itemView.findViewById(R.id.book_image);

        }
    }

    public interface ItemClick{
        void onClick(View v, int position, Book b);
    }
}

