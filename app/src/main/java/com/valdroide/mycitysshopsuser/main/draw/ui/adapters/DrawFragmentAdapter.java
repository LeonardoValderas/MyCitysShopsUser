package com.valdroide.mycitysshopsuser.main.draw.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragment;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawFragmentAdapter extends RecyclerView.Adapter<DrawFragmentAdapter.ViewHolder> {

    private List<Draw> drawList;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;

    public DrawFragmentAdapter(List<Draw> drawList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.drawList = drawList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = (DrawFragment) fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_draw_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Draw draw = drawList.get(position);
        if (draw.getURL_LOGO() != null)
            Utils.setPicasso(fragment.getActivity(), draw.getURL_LOGO(), R.drawable.ic_launcher, holder.imageViewImage);
        else
            Utils.setPicasso(fragment.getActivity(), "", R.drawable.ic_launcher, holder.imageViewImage);
        holder.textViewName.setText(draw.getSHOP_NAME());
        //holder.textViewName.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));

        holder.textViewDescription.setText(draw.getDESCRIPTION());
        //holder.textViewEndDate.setText(fragment.getActivity().getString(R.string.end_date_recycler) + " " + draw.getEND_DATE());

        if (draw.getIS_WINNER() != 0) {
            holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
            holder.textViewParticipation.setText(R.string.you_are_winner_adapter);
            //   holder.textViewParticipation.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));
            holder.textViewWiner.setVisibility(View.VISIBLE);
            holder.textViewWiner.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
            holder.textViewWiner.setText(fragment.getActivity().getString(R.string.you_are_winner_adapter_limit) + " " + draw.getLIMITE_DATE() + " (inclusive).");
        } else {
            if (draw.getPARTICIPATION() == 0) {
                holder.textViewParticipation.setText(R.string.partipation_invited);
                //      holder.textViewParticipation.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));
                holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorTextViewSub));
                holder.textViewWiner.setVisibility(View.GONE);
            } else {
                holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
                holder.textViewParticipation.setText(R.string.participation_ok);
                //      holder.textViewParticipation.setTypeface(Utils.setFontGoodDogTextView(fragment.getActivity()));
                holder.textViewWiner.setVisibility(View.GONE);
            }
        }
        holder.setOnItemClickListener(onItemClickListener, position, draw);
    }

    @Override
    public int getItemCount() {
        return drawList.size();
    }

    public void setDraw(List<Draw> draws) {
        drawList = draws;
        notifyDataSetChanged();
    }

//    public void setUpdateDraw(int position, Draw draw) {
//        drawList.remove(draw);
//        drawList.add(position, draw);
//        notifyDataSetChanged();
//    }

    public void setUpdateDraw(int position) {
        if (drawList.get(position).getPARTICIPATION() == 0) {
            drawList.get(position).setPARTICIPATION(1);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textViewName)
        TextView textViewName;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.textViewEndDate)
        TextView textViewEndDate;
        @Bind(R.id.textViewParticipation)
        TextView textViewParticipation;
        @Bind(R.id.textViewWiner)
        TextView textViewWiner;
        @Bind(R.id.imageViewImage)
        ImageView imageViewImage;

        private View v;

        public ViewHolder(View view) {
            super(view);
            this.v = view;
            ButterKnife.bind(this, view);
        }

        public void setOnItemClickListener(final OnItemClickListener listener, final int position, final Draw draw) {

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickDraw(position, draw);
                }
            });

//            imageViewFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (shop.getIS_FOLLOW() == 0)
//                        listener.onClickFollowOrUnFollow(position, shop, true);
//                    else
//                        listener.onClickFollowOrUnFollow(position, shop, false);
//
//                }
//            });
//            imageViewOffer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickOffer(position, shop);
//                }
//            });
//            imageViewMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    v.setEnabled(false);
//                    listener.onClickMap(shop, v);
//                }
//            });
//            imageViewContact.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickContact(shop);
//                }
//            });
        }
    }
}
