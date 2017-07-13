package com.valdroide.mycitysshopsuser.main.navigation.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogWelcome {

    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;

    private AlertDialog alertDialog;

    public DialogWelcome(Context context) {
        Utils.writelogFile(context, "DialogWelcome y AlertDialog(DialogWelcome)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Utils.writelogFile(context, "LayoutInflater(DialogWelcome)");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context, "inflater(DialogWelcome)");
        View layout = inflater.inflate(R.layout.dialog_welcome, null);
        Utils.writelogFile(context, "builder.setView(layout)(DialogWelcome)");
        builder.setView(layout);
        Utils.writelogFile(context, "Se inicia ButterKnife(DialogWelcome)");
        ButterKnife.bind(this, layout);
        try {
            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        alertDialog.dismiss();
                    }
                    return true;
                }
            });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } catch (Exception e) {
            Utils.writelogFile(context, "catch error " + e.getMessage() + "(DialogWelcome)");
        }
    }
}
