package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogOfferAdapter extends RecyclerView.Adapter<DialogOfferAdapter.ViewHolder> {


    private List<Offer> offerList;
    private Context context;

    public DialogOfferAdapter(List<Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_offer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        Utils.setPicasso(context, offer.getURL_IMAGE(), R.drawable.ic_launcher, holder.imageViewOffer);
        holder.textViewTitle.setText(offer.getTITLE());
      //  holder.textViewTitle.setTypeface(Utils.setFontExoTextView(context));
        holder.textViewOffer.setText(offer.getOFFER());
       // holder.textViewOffer.setTypeface(Utils.setFontRalewatTextView(context));
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public void setShop(List<Offer> offers) {
        this.offerList = offers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageViewOffer)
        ImageView imageViewOffer;
        @Bind(R.id.textViewTitle)
        TextView textViewTitle;
        @Bind(R.id.textViewOffer)
        TextView textViewOffer;
//        @Bind(R.id.linearConteiner)
//        LinearLayout linearConteiner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
