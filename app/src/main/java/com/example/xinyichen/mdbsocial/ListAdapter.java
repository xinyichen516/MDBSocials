package com.example.xinyichen.mdbsocial;

/**
 * Created by xinyichen on 9/27/17.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Social> data;

    ListAdapter(Context context, ArrayList<Social> data) {
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
        final Social m = data.get(position);
        holder.title.setText(m.title);
        holder.date.setText(m.date);
        holder.numInterest.setText(context.getResources().getString(R.string.pplInt, m.numInterested + ""));
        holder.email.setText(m.hostEmail);

        StorageReference pathReference = Utils.storageReference.child("/" + m.key + ".png");

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .into(holder.eventImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SocialDetails.class);
                intent.putExtra("social", m);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() { return data.size(); }

    /**
     * A card displayed in the RecyclerView
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView eventImg;
        TextView date;
        TextView numInterest;
        TextView email;

        CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.eventImg = (ImageView) view.findViewById(R.id.eventImg);
            this.date = (TextView) view.findViewById(R.id.date);
            this.numInterest = (TextView) view.findViewById(R.id.numInterest);
            this.email = (TextView) view.findViewById(R.id.email);
        }
    }
}