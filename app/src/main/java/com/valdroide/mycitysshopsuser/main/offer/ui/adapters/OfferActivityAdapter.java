package com.valdroide.mycitysshopsuser.main.offer.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivityAdapter extends RecyclerView.Adapter<OfferActivityAdapter.ViewHolder> {

    private List<Offer> offerList;
    private Activity activity;

    public OfferActivityAdapter(List<Offer> offerList, Activity activity) {
        this.offerList = offerList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_offer, parent, false);

        return new ViewHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        //Utils.setPicasso(activity, offer.getURL_LOGO(), R.mipmap.ic_launcher, holder.imageViewShop);
        holder.textViewTitle.setText(offer.getTITLE());
        holder.textViewOffer.setText(offer.getOFFER());
       // holder.setOnItemClickListener(onItemClickListener, position, shop);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public void setOffer(List<Offer> offers) {
        this.offerList = offers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageViewOffer)
        ImageView imageViewOffer;
        @Bind(R.id.textViewOffer)
        TextView textViewOffer;
        @Bind(R.id.linearConteiner)
        LinearLayout linearConteiner;
        @Bind(R.id.textViewTitle)
        TextView textViewTitle;

        public ViewHolder(View view, Activity activity) {
            super(view);
            ButterKnife.bind(this, view);
        }

//        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Shop shop) {
//
//            buttonFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (shop.getIS_FOLLOW() == 0)
//                        listener.onClickFollow(position, shop);
//                    else
//                        listener.onClickUnFollow(position, shop);
//
//                }
//            });
//            buttonOffer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickOffer(shop);
//                }
//            });
//            buttonMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickMap(shop);
//                }
//            });
//            buttonCont.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickContact(shop);
//                }
//            });
//
//        }
    }
}
