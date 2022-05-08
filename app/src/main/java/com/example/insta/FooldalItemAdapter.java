package com.example.insta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;

public class FooldalItemAdapter extends RecyclerView.Adapter<FooldalItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<FooldalItem> mFooldalData = new ArrayList<>();
    private ArrayList<FooldalItem> mFooldalDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;
    FooldalItemAdapter(Context context, ArrayList<FooldalItem> itemsData){
        this.mFooldalData = itemsData;
        this.mFooldalDataAll = itemsData;
        this.mContext = context;
    }




    @NonNull
    @Override
    public FooldalItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FooldalItemAdapter.ViewHolder holder, int position) {
        FooldalItem currentItem = mFooldalData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mFooldalData.size();
    }

    @Override
    public Filter getFilter() {
        return fooldalFilter;
    }
    private Filter fooldalFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<FooldalItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mFooldalDataAll.size();
                results.values = mFooldalDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(FooldalItem item : mFooldalDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mFooldalData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };
        class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mItemImage;
        private RatingBar mRatingBar;


        public ViewHolder(View itemView){
            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mRatingBar = itemView.findViewById(R.id.ratingBar);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FooldalActivity)mContext).updateAlertIcon();
                }
            });
        }

        public void bindTo(FooldalItem currentItem) {
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getInfo());
            mRatingBar.setRating(currentItem.getRatedInfo());

            // Load the images into the ImageView using the Glide library.
            //Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    };
}
