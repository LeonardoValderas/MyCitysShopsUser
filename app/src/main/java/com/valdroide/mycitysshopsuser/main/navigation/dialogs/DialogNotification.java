package com.valdroide.mycitysshopsuser.main.navigation.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogNotification {


    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;
    @Bind(R.id.linearConteiner)
    LinearLayout linearConteiner;
    @Bind(R.id.textViewTitle)
    TextView textViewTitle;
    @Bind(R.id.textViewMessage)
    TextView textViewMessage;
    private Context context;
    public AlertDialog alertDialog;

    public DialogNotification(final Context context, String name, String url, String message) {
        this.context = context;
        Utils.writelogFile(context, "DialogNotification y AlertDialog(Notification)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Utils.writelogFile(context, "LayoutInflater(Notification)");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context, "inflater(Notification)");
        View layout = inflater.inflate(R.layout.dialog_notification, null);
        Utils.writelogFile(context, "builder.setView(layout)(Notification)");
        builder.setView(layout);
        Utils.writelogFile(context, "Se inicia ButterKnife(Notification)");
        ButterKnife.bind(this, layout);
        try {
            Utils.writelogFile(context, "fill componentes(Notification)");
            Utils.setPicasso(context, url, R.drawable.ic_launcher, imageViewShop);
            textViewTitle.setText(name);
            textViewMessage.setText(message);
          //  textViewMessage.setTypeface(Utils.setFontExoTextView(context));
            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();

            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //alertDialog.getWindow().setLayout(550, 500);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        alertDialog.dismiss();
                    }
                    return true;
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Notification)");
        }
    }

}
