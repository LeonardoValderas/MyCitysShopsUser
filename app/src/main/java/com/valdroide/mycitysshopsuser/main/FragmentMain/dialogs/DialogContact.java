package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs.adapters.DialogOfferAdapter;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogContact {

    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.textViewName)
    TextView textViewName;
    @Bind(R.id.textViewPhone)
    TextView textViewPhone;
    @Bind(R.id.textViewWhat)
    TextView textViewWhat;
    @Bind(R.id.textViewEmail)
    TextView textViewEmail;
    @Bind(R.id.textViewWeb)
    TextView textViewWeb;
    @Bind(R.id.textViewFace)
    TextView textViewFace;
    @Bind(R.id.textViewInsta)
    TextView textViewInsta;
    @Bind(R.id.textViewTwi)
    TextView textViewTwi;
    @Bind(R.id.textViewSna)
    TextView textViewSna;
    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;

    private Context context;
    public AlertDialog alertDialog;

    public DialogContact(Context context, Shop shop) {
        this.context = context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.dialog_contact, null);
        builder.setView(layout);
        ButterKnife.bind(this, layout);
        Utils.setPicasso(context, shop.getURL_LOGO(), R.mipmap.ic_launcher, imageViewShop);
        textViewName.setText(shop.getSHOP());

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        //alertDialog.getWindow().setLayout(550, 500);
        alertDialog.show();
    }
}
