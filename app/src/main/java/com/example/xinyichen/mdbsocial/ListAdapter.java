package com.example.xinyichen.mdbsocial;

/**
 * Created by xinyichen on 9/27/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Social> data;

    public ListAdapter(Context context, ArrayList<Social> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Social m = data.get(position);
        holder.title.setText(m.title);
        holder.date.setText(m.date);
        holder.numInterested.setText(m.numInterested);
        holder.email.setText(m.hostEmail);

        //haven't taught this yet but essentially it runs separately from the UI
        class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
            protected Bitmap doInBackground(String... strings) {
                try {return Glide.
                        with(context).
                        load(strings[0]).
                        asBitmap().
                        into(100, 100). // Width and height
                        get();}
                catch (Exception e) {return null;}
            }

            protected void onProgressUpdate(Void... progress) {}

            protected void onPostExecute(Bitmap result) {
                holder.eventImg.setImageBitmap(result);
            }
        }

        //Part 4: Load the image from the url. Use
        // new DownloadFilesTask().execute(uri.toString())
        // to get set the imageView using the resulting Uri. If it fails, log the exception

        //dw if it doesn't work, bc it didn't work for me :(
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * A card displayed in the RecyclerView
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView eventImg;
        TextView date;
        TextView numInterested;
        TextView email;

        public CustomViewHolder (View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.eventImg = (ImageView) view.findViewById(R.id.eventImg);
            this.date = (TextView) view.findViewById(R.id.date);
            this.numInterested = (TextView) view.findViewById(R.id.numInterest);
            this.email = (TextView) view.findViewById(R.id.email);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SocialDetails.class);
                    intent.putExtra("eventTitle", title.getText().toString().trim());
                    intent.putExtra("date", date.getText().toString().trim());
                    intent.putExtra("numInterested", numInterested.getText().toString().trim());
                    intent.putExtra("email", email.getText().toString().trim());
                }
            });
        }
    }
}
