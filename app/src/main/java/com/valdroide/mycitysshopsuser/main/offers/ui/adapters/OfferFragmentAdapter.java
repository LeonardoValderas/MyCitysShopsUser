package com.valdroide.mycitysshopsuser.main.offers.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.main.offers.ui.OfferFragment;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferFragmentAdapter extends RecyclerView.Adapter<OfferFragmentAdapter.ViewHolder> {

    private List<Offer> offerList;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;

    public OfferFragmentAdapter(List<Offer> offerList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.offerList = offerList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = (OfferFragment) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_offer_all, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.textViewShopName.setText(offer.getSHOP());
       // holder.textViewShopName.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));
        if (offer.getURL_IMAGE() != null)
            Utils.setPicasso(fragment.getActivity(), offer.getURL_IMAGE(), R.drawable.ic_launcher, holder.imageViewOffer);
        else
            Utils.setPicasso(fragment.getActivity(), "", R.drawable.ic_launcher, holder.imageViewOffer);
        holder.textViewTitle.setText(offer.getTITLE());
       // holder.textViewTitle.setTypeface(Utils.setFontExoTextView(fragment.getActivity()));
        holder.textViewOffer.setText(offer.getOFFER());
      //  holder.textViewOffer.setTypeface(Utils.setFontRalewatTextView(fragment.getActivity()));
        holder.setOnItemClickListener(onItemClickListener, position, offer);
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public void setDraw(List<Offer> offers) {
        offerList = offers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewShopName)
        TextView textViewShopName;
        @Bind(R.id.imageViewOffer)
        ImageView imageViewOffer;
        @Bind(R.id.textViewTitle)
        TextView textViewTitle;
        @Bind(R.id.textViewOffer)
        TextView textViewOffer;
        @Bind(R.id.linearConteiner)
        LinearLayout linearConteiner;

        private View v;

        public ViewHolder(View view) {
            super(view);
            this.v = view;
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Offer offer) {

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickOffer(position, offer);
                }
            });
        }
    }
}
