package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Offer;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.adapters.DialogOfferAdapter;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogOffer {

    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;
    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.recyclerViewOffer)
    RecyclerView recyclerViewOffer;
    @Bind(R.id.textViewName)
    TextView textViewName;
    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;

    private DialogOfferAdapter adapter;
    private Context context;
    public AlertDialog alertDialog;

    public DialogOffer(Context context, List<Offer> offers, Shop shop) {
        this.context = context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.dialog_offer, null);
        builder.setView(layout);
        ButterKnife.bind(this, layout);
        Utils.setPicasso(context, shop.getURL_LOGO(), R.mipmap.ic_launcher, imageViewShop);
        textViewName.setText(shop.getSHOP());
        if (offers.size() > 0) {
            textViewEmpty.setVisibility(View.GONE);
            adapter = new DialogOfferAdapter(offers, context);
            recyclerViewOffer.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewOffer.setHasFixedSize(true);
            recyclerViewOffer.setAdapter(adapter);
        } else {
            recyclerViewOffer.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        }
        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
       // alertDialog.getWindow().setLayout(550, 500);
        alertDialog.show();
    }
}
