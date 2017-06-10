package viit.com.libraryviit.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import viit.com.libraryviit.R;
import viit.com.libraryviit.activities.BookDetailActivity;
import viit.com.libraryviit.book.Book;

/**
 * Created by anurag on 1/6/17.
 */

public class BookGridViewAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Book> data = new ArrayList();

    public BookGridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.bookTitle);
            holder.image = (ImageView) row.findViewById(R.id.bookImage);
            holder.details = (Button) row.findViewById(R.id.details);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final Book item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);

                intent.putExtra("Book", item);
                context.startActivity(intent);
            }
        });
        picassoLoader(context,holder.image,item.getImageSmall());
        return row;
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
    static class ViewHolder {
        Button details;
        TextView imageTitle;
        ImageView image;
    }
}
