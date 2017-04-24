package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogContact {

    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    //    @Bind(R.id.textViewName)
//    TextView textViewName;
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
    @Bind(R.id.conteiner)
    LinearLayout conteiner;

    private Context context;
    public AlertDialog alertDialog;

    public DialogContact(Context context, Shop shop) {
        this.context = context;
        Utils.writelogFile(context, "DialogMap y AlertDialog(Contact)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Utils.writelogFile(context, "LayoutInflater(Contact)");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context, "inflater(Contact)");
        View layout = inflater.inflate(R.layout.dialog_contact, null);
        Utils.writelogFile(context, "builder.setView(layout)(Contact)");
        builder.setView(layout);
        Utils.writelogFile(context, "Se inicia ButterKnife(Contact)");
        ButterKnife.bind(this, layout);
        try {
            Utils.writelogFile(context, "fill componentes(Contact)");
            Utils.setPicasso(context, shop.getURL_LOGO(), R.drawable.ic_launcher, imageViewShop);
            // textViewName.setText(shop.getSHOP());
            textViewPhone.setText(shop.getPHONE());
            setCopyClipBoard(textViewPhone);
            textViewWhat.setText(shop.getWHATSAAP());
            setCopyClipBoard(textViewWhat);
            textViewEmail.setText(shop.getEMAIL());
            setCopyClipBoard(textViewEmail);
            textViewWeb.setText(shop.getWEB());
            setCopyClipBoard(textViewWeb);
            textViewFace.setText(shop.getFACEBOOK());
            setCopyClipBoard(textViewFace);
            textViewInsta.setText(shop.getINSTAGRAM());
            setCopyClipBoard(textViewInsta);
            textViewTwi.setText(shop.getTWITTER());
            setCopyClipBoard(textViewTwi);
            textViewSna.setText(shop.getSNAPCHAT());
            setCopyClipBoard(textViewSna);
            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            //alertDialog.getWindow().setLayout(550, 500);
            alertDialog.show();
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Map)");
        }
    }

    private void setCopyClipBoard(final TextView textView) {
        final String textCopy = textView.getText().toString();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textCopy != null)
                    if (!textCopy.isEmpty()) {
                        setClipboard(context, textView.getText().toString());
                        Utils.showSnackBar(conteiner, context.getString(R.string.data_copy));
                    } else
                        Utils.showSnackBar(conteiner, context.getString(R.string.data_empty));
                else
                    Utils.showSnackBar(conteiner, context.getString(R.string.data_empty));
            }
        });

    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            @SuppressWarnings("deprecation")
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

    }
}
