package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.icu.text.CompactDecimalFormat;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        return new ViewHolder(view, fragment, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Shop shop = shopsList.get(position);
        holder.imageViewOfferNew.setVisibility(View.INVISIBLE); // ver si podemos individualizar los cambios de offer
        Utils.setPicasso(fragment.getActivity(), shop.getURL_LOGO(), R.mipmap.ic_launcher, holder.imageViewShop);

        holder.textViewDescription.setText("Esta es una descripcion del local. Esta es una descripcion del local." +
                "Esta es una descripcion del local. Esta es una descripcion del local." +
                "Esta es una descripcion del local. Esta es una descripcion del local.");
        if (shop.getIS_FOLLOW() == 1)
            holder.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollowing));
        else
            holder.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollow));

        //if(shop.getFOLLOW() != null)
        holder.textViewFollow.setText(String.valueOf(shop.getFOLLOW()));
        holder.setOnItemClickListener(onItemClickListener, position, shop);
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public void setShop(List<Shop> shops) {
        this.shopsList = shops;
        notifyDataSetChanged();
    }

    public void setUpdateShop(int position, Shop shop) {
        this.shopsList.remove(position);
        this.shopsList.add(position, shop);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imageViewShop)
        ImageView imageViewShop;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.linearConteiner)
        ConstraintLayout linearConteiner;
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

        public ViewHolder(View view, Fragment fragment, OnItemClickListener onItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Shop shop) {

//            linearConteiner.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClick(v, position);
//                }
//            });
            imageViewFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shop.getIS_FOLLOW() == 0)
                        listener.onClickFollow(position, shop);
                    else
                        listener.onClickUnFollow(position, shop);

                }
            });
            imageViewOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickOffer(shop);
                }
            });
            imageViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickMap(shop);
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
