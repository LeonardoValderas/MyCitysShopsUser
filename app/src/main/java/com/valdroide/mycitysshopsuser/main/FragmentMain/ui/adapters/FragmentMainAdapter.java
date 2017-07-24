package com.valdroide.mycitysshopsuser.main.FragmentMain.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.NativeExpressAdView;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMain;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> shopsList;
    private OnItemClickListener onItemClickListener;
    private FragmentMain fragment;

    private static final int SHOP_ITEM_VIEW_TYPE = 0;
    private static final int AD_ITEM_VIEW_TYPE = 1;

    public FragmentMainAdapter(List<Object> shopsList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.shopsList = shopsList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = (FragmentMain) fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case SHOP_ITEM_VIEW_TYPE:
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
                return new ViewHolder(view);
            case AD_ITEM_VIEW_TYPE:
                View view_ad_native = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_shop_ad_native, parent, false);
                return new NavitveExpressAdViewHolder(view_ad_native);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case SHOP_ITEM_VIEW_TYPE:
            default:
                Shop shop = (Shop) shopsList.get(position);
                ViewHolder holderItem = (ViewHolder) holder;
                YoYo.with(Techniques.FadeIn).playOn(holderItem.card_view);

                holderItem.textViewName.setText(shop.getSHOP());

                if (shop.getURL_LOGO() != null && !shop.getURL_LOGO().isEmpty())
                    Utils.setPicasso(fragment.getActivity(), shop.getURL_LOGO(), R.drawable.ic_launcher, holderItem.imageViewShop);
                else
                    Utils.setPicasso(fragment.getActivity(), "sin logo", R.drawable.ic_launcher, holderItem.imageViewShop);

                holderItem.textViewDescription.setText(shop.getDESCRIPTION());
                if (shop.getWORKING_HOURS() != null)
                    if (!shop.getWORKING_HOURS().equals("null"))
                        holderItem.textViewWorking.setText(shop.getWORKING_HOURS());
                if (shop.getIS_FOLLOW() == 1)
                    holderItem.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollowing));
                else
                    holderItem.imageViewFollow.setColorFilter(ContextCompat.getColor(fragment.getActivity(), R.color.colorFollow));

                if (shop.getIS_OFFER_UPDATE() == 1)
                    holderItem.imageViewOfferNew.setVisibility(View.VISIBLE);
                else
                    holderItem.imageViewOfferNew.setVisibility(View.INVISIBLE);

                holderItem.textViewFollow.setText(String.valueOf(shop.getFOLLOW()));
                holderItem.setOnItemClickListener(onItemClickListener, position, shop);
                break;

            case AD_ITEM_VIEW_TYPE:


                NavitveExpressAdViewHolder navitveExpressAdViewHolder = (NavitveExpressAdViewHolder) holder;
                NativeExpressAdView adView = (NativeExpressAdView) shopsList.get(position);

                ViewGroup adCardView = (ViewGroup) navitveExpressAdViewHolder.itemView;
                if (adCardView.getChildCount() > 0)
                    adCardView.removeAllViews();

                if (adView.getParent() != null)
                    ((ViewGroup) adView.getParent()).removeView(adView);
                YoYo.with(Techniques.RubberBand).playOn(adCardView);
                adCardView.addView(adView);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!fragment.isSearch) {

            int size = shopsList.size();
            if (size > 0 && size < 4)
                return (position == size - 1) ? AD_ITEM_VIEW_TYPE : SHOP_ITEM_VIEW_TYPE;
            else if (position != 0 && position == 3)
                return AD_ITEM_VIEW_TYPE;
            else
                return SHOP_ITEM_VIEW_TYPE;
        } else {
            return SHOP_ITEM_VIEW_TYPE;
        }
    }

    public void setShop(List<Object> shops) {
        shopsList = shops;
        notifyDataSetChanged();
    }

    public void setUpdateShop(int position, Shop shop) {
        shopsList.remove(shop);
        shopsList.add(position, shop);
        notifyDataSetChanged();
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
        @Bind(R.id.card_view)
        CardView card_view;

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

    public static class NavitveExpressAdViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.card_view_ad_native)
        CardView card_view_ad_native;

        NavitveExpressAdViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
