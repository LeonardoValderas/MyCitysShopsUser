package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    //    @Bind(R.id.textViewName)
//    TextView textViewName;
    @Bind(R.id.textViewEmpty)
    TextView textViewEmpty;


    private DialogOfferAdapter adapter;
    private Context context;
    public AlertDialog alertDialog;

    public DialogOffer(Context context, List<Offer> offers, Shop shop) {
        this.context = context;
        Utils.writelogFile(context, "DialogOffer y AlertDialog(Offer)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Utils.writelogFile(context, "LayoutInflater(Offer)");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context, "inflater(Offer)");
        View layout = inflater.inflate(R.layout.dialog_offer, null);
        Utils.writelogFile(context, "builder.setView(layout)(Offer)");
        builder.setView(layout);
        Utils.writelogFile(context, "Se inicia ButterKnife(Offer)");
        ButterKnife.bind(this, layout);

        try {
            Utils.writelogFile(context, "fill componentes(Offer)");
            Utils.setPicasso(context, shop.getURL_LOGO(), R.drawable.ic_launcher, imageViewShop);
            // textViewName.setText(shop.getSHOP());
            if (offers.size() > 0) {
                textViewEmpty.setVisibility(View.GONE);
                adapter = new DialogOfferAdapter(offers, context);
                recyclerViewOffer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
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
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //  alertDialog.getWindow().setLayout(1200, 500);
            alertDialog.show();
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Offer)");
        }
    }
}
