package com.valdroide.mycitysshopsuser.main.draw.dialogs;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Draw;
import com.valdroide.mycitysshopsuser.main.draw.ui.DrawFragment;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogDraw {

    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;
    @Bind(R.id.imageViewImage)
    ImageView imageViewImage;
    @Bind(R.id.textViewDescription)
    TextView textViewDescription;
    @Bind(R.id.textViewCondition)
    TextView textViewCondition;
    @Bind(R.id.textViewEndDate)
    TextView textViewEndDate;
    @Bind(R.id.buttonCancel)
    Button buttonCancel;
    @Bind(R.id.buttonOK)
    Button buttonOK;
    @Bind(R.id.linearConteiner)
    LinearLayout linearConteiner;
    @Bind(R.id.scroll)
    ScrollView scroll;
    @Bind(R.id.editTextDNI)
    EditText editTextDNI;
    @Bind(R.id.editTextNames)
    EditText editTextNames;
    @Bind(R.id.buttonCancelSub)
    Button buttonCancelSub;
    @Bind(R.id.buttonOKSub)
    Button buttonOKSub;
    @Bind(R.id.scrollInscription)
    ScrollView scrollInscription;
    @Bind(R.id.textViewName)
    TextView textViewName;
    @Bind(R.id.textViewInvited)
    TextView textViewInvited;
    private Fragment context;
    public AlertDialog alertDialog;

    public DialogDraw(final Fragment context, final Draw draw) {
        this.context= (DrawFragment) context;
        Utils.writelogFile(context.getActivity(), "DialogDraw y AlertDialog(Draw)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        Utils.writelogFile(context.getActivity(), "LayoutInflater(Draw)");
        LayoutInflater inflater = LayoutInflater.from(context.getActivity());
//        LayoutInflater inflater = (LayoutInflater) context.getActivity()
//                .getSystemService(context.getActivity().LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context.getActivity(), "inflater(Draw)");
        View layout = inflater.inflate(R.layout.dialog_draw, null);
        Utils.writelogFile(context.getActivity(), "builder.setView(layout)(Draw)");
        builder.setView(layout);
        Utils.writelogFile(context.getActivity(), "Se inicia ButterKnife(Draw)");
        ButterKnife.bind(this, layout);
        try {
            Utils.writelogFile(context.getActivity(), "fill componentes(Draw)");
            if (draw.getURL_SHOP() != null)
                Utils.setPicasso(context.getActivity(), draw.getURL_SHOP(), R.drawable.ic_launcher, imageViewShop);
            Utils.setPicasso(context.getActivity(), draw.getURL_LOGO(), R.drawable.ic_launcher, imageViewImage);
            textViewName.setText(draw.getSHOP_NAME());
            textViewInvited.setText(context.getActivity().getString(R.string.text_participate_draw));
            textViewDescription.setText(draw.getDESCRIPTION());
            Spanned fromHtml = Utils.fromHtml("<b>" + context.getActivity().getString(R.string.condition_text_draw) + "</b>" + " " + draw.getCONDITION());
            textViewCondition.setText(fromHtml);
            textViewEndDate.setText(context.getActivity().getString(R.string.date_end_draw_text) + " " + draw.getEND_DATE());
            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scroll.setVisibility(View.GONE);
                    scrollInscription.setVisibility(View.VISIBLE);
                }
            });

            buttonCancelSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            buttonOKSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editTextDNI.getText().toString().isEmpty() && !editTextNames.getText().toString().isEmpty()) {
                        ((DrawFragment) context).participateDraw(draw, editTextDNI.getText().toString(), editTextNames.getText().toString());
                    } else {
                        Utils.showSnackBar(linearConteiner, context.getActivity().getString(R.string.draw_empty_participate));
                    }
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
            Utils.writelogFile(context.getActivity(), "catch error " + e.getMessage() + "(Notification)");
        }
    }
}
