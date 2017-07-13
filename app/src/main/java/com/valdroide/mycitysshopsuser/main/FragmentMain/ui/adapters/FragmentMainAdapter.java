package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMain;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentMainAdapter extends RecyclerView.Adapter<FragmentMainAdapter.ViewHolder> {

    private List<Shop> shopsList;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;

    public FragmentMainAdapter(List<Shop> shopsList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.shopsList = shopsList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = (FragmentMain) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shop shop = shopsList.get(position);
        holder.textViewName.setText(shop.getSHOP());

//        holder.textViewName.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));

        if (shop.getURL_LOGO() != null && !shop.getURL_LOGO().isEmpty())
            Utils.setPicasso(fragment.getActivity(), shop.getURL_LOGO(), R.drawable.ic_launcher, holder.imageViewShop);
        else
            Utils.setPicasso(fragment.getActivity(), "sin logo", R.drawable.ic_launcher, holder.imageViewShop);

        holder.textViewDescription.setText(shop.getDESCRIPTION());
       // holder.textViewDescription.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));
        if (shop.getWORKING_HOURS() != null)
            if (!shop.getWORKING_HOURS().equals("null"))
                holder.textViewWorking.setText(shop.getWORKING_HOURS());
        if (shop.getIS_FOLLOW() == 1)
            holder.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollowing));
        else
            holder.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollow));

        if (shop.getIS_OFFER_UPDATE() == 1)
            holder.imageViewOfferNew.setVisibility(View.VISIBLE);
        else
            holder.imageViewOfferNew.setVisibility(View.INVISIBLE);

        holder.textViewFollow.setText(String.valueOf(shop.getFOLLOW()));
        holder.setOnItemClickListener(onItemClickListener, position, shop);
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public void setShop(List<Shop> shops) {
        shopsList = shops;
        notifyDataSetChanged();
    }

    public void setUpdateShop(int position, Shop shop) {
        shopsList.remove(shop);
        shopsList.add(position, shop);
        notifyDataSetChanged();
    }

    public void setUpdateShop(int position) {
        if (shopsList.get(position).getIS_OFFER_UPDATE() != 0) {
            shopsList.get(position).setIS_OFFER_UPDATE(0);
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageViewShop)
        ImageView imageViewShop;
        @Bind(R.id.textViewName)
        TextView textViewName;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.textViewWorking)
        TextView textViewWorking;
        @Bind(R.id.imageViewFollow)
        ImageView imageViewFollow;
        @Bind(R.id.textViewFollow)
        TextView textViewFollow;
        @Bind(R.id.imageViewOffer)
        ImageView imageViewOffer;
        @Bind(R.id.imageViewOfferNew)
        ImageView imageViewOfferNew;
        @Bind(R.id.imageViewContact)
        ImageView imageViewContact;
        @Bind(R.id.imageViewMap)
        ImageView imageViewMap;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Shop shop) {

            imageViewFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shop.getIS_FOLLOW() == 0)
                        listener.onClickFollowOrUnFollow(position, shop, true);
                    else
                        listener.onClickFollowOrUnFollow(position, shop, false);

                }
            });
            imageViewOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickOffer(position, shop);
                }
            });
            imageViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    listener.onClickMap(shop, v);
                }
            });
            imageViewContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickContact(shop);
                }
            });
        }
    }
}
