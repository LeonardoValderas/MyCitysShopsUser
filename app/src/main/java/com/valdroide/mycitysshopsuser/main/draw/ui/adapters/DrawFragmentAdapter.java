package com.valdroide.mycitysshopsuser.main.draw.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
        YoYo.with(Techniques.FlipInX).playOn(holder.card_view);

        if (draw.getURL_LOGO() != null)
            Utils.setPicasso(fragment.getActivity(), draw.getURL_LOGO(), R.drawable.ic_launcher, holder.imageViewImage);
        else
            Utils.setPicasso(fragment.getActivity(), "", R.drawable.ic_launcher, holder.imageViewImage);

        holder.textViewName.setText(draw.getSHOP_NAME());
        holder.textViewDescription.setText(draw.getDESCRIPTION());
        holder.textViewEndDate.setText(fragment.getActivity().getString(R.string.date_end_draw_text) + " " + Utils.formatDrawDate(draw.getEND_DATE(),"yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss"));
        if (draw.getIS_WINNER() != 0) {
            holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
            holder.textViewParticipation.setText(R.string.you_are_winner_adapter);
            holder.textViewWiner.setVisibility(View.VISIBLE);
            holder.textViewWiner.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
            holder.textViewWiner.setText(fragment.getActivity().getString(R.string.you_are_winner_adapter_limit) + " " + draw.getLIMITE_DATE() + " (inclusive).");
        } else {
            if (draw.getPARTICIPATION() == 0) {
                holder.textViewParticipation.setText(R.string.partipation_invited);
                holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorTextViewSub));
                holder.textViewWiner.setVisibility(View.GONE);
            } else {
                holder.textViewParticipation.setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.colorAccent));
                holder.textViewParticipation.setText(R.string.participation_ok);
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
        @Bind(R.id.card_view)
        CardView card_view;

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
        }
    }
}
