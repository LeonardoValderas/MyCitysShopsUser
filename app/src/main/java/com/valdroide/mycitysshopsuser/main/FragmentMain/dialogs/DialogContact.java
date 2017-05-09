package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.common.android.FragmentCompat;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogContact {

    private static final int PERMISSION_OK = 123;
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

    private Fragment fragment;
    private AlertDialog alertDialog;
    private OnClickContact listener;
    private String data = "";
    private ProgressDialog pDialog;

    public DialogContact(final Fragment fragment, Shop shop, final OnClickContact listener) {
        this.fragment = fragment;
        this.listener = listener;
        Utils.writelogFile(fragment.getActivity(), "DialogMap y AlertDialog(Contact)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        Utils.writelogFile(fragment.getActivity(), "LayoutInflater(Contact)");
        LayoutInflater inflater = (LayoutInflater) fragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(fragment.getActivity(), "inflater(Contact)");
        View layout = inflater.inflate(R.layout.dialog_contact, null);
        Utils.writelogFile(fragment.getActivity(), "builder.setView(layout)(Contact)");
        builder.setView(layout);
        Utils.writelogFile(fragment.getActivity(), "Se inicia ButterKnife(Contact)");
        ButterKnife.bind(this, layout);
        //    initDialog();
        try {
            Utils.writelogFile(fragment.getActivity(), "fill componentes(Contact)");
            Utils.setPicasso(fragment.getActivity(), shop.getURL_LOGO(), R.drawable.ic_launcher, imageViewShop);
            textViewPhone.setText(shop.getPHONE());
            setOnClickTextView(textViewPhone);
            textViewWhat.setText(shop.getWHATSAAP());
            setCopyClipBoard(textViewWhat);
            setOnClickTextView(textViewWhat);
            textViewEmail.setText(shop.getEMAIL());
            setOnClickTextView(textViewEmail);
            textViewWeb.setText(shop.getWEB());
            setOnClickTextView(textViewWeb);
            textViewFace.setText(shop.getFACEBOOK());
            setOnClickTextView(textViewFace);
            textViewInsta.setText(shop.getINSTAGRAM());
            setOnClickTextView(textViewInsta);
            textViewTwi.setText(shop.getTWITTER());
            setOnClickTextView(textViewTwi);
            textViewSna.setText(shop.getSNAPCHAT());
            setOnClickTextView(textViewSna);
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
            Utils.writelogFile(fragment.getActivity(), " catch error " + e.getMessage() + "(Map)");
        }
    }

    private void setCopyClipBoard(final TextView textView) {
        final String textCopy = textView.getText().toString();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textCopy != null)
                    if (!textCopy.isEmpty()) {
                        setClipboard(fragment.getActivity(), textView.getText().toString());
                        Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_copy));
                    } else
                        Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_empty));
                else
                    Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_empty));
            }
        });

    }

    public void copyText(TextView textView) {
        if (textView.getText() != null)
            if (!textView.getText().toString().isEmpty()) {
                setClipboard(fragment.getActivity(), textView.getText().toString());
                Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_copy));
            } else
                Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_empty));
        else
            Utils.showToast(fragment.getActivity(), fragment.getActivity().getString(R.string.data_empty));
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

    private void setOnClickTextView(final TextView textView) {

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int view = v.getId();
                data = textView.getText().toString();
                switch (view) {
                    case R.id.textViewPhone:
                        if (!data.isEmpty()) {
                            listener.onClickField(data, null, 0);
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewWhat:
                        if (!data.isEmpty()) {
                            copyText(textView);
                            listener.onClickField(data, null, 1);
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewEmail:
                        if (!data.isEmpty()) {
                            //      pDialog.show();
                            listener.onClickField(data, null, 2);
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewWeb:
                        String web = "https://";
                        data = replaceSpace(data);
                        web = web + data;
                        if (!web.isEmpty()) {
                            if (URLUtil.isValidUrl(web)) {
                                //        pDialog.show();
                                listener.onClickField(web, null, 3);
                            } else
                                setError(fragment.getActivity().getString(R.string.url_invalid));
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewFace:
                        if (!data.isEmpty()) {
                            String face = "https://www.facebook.com/";
                            String faceApp = "fb://page/";
                            data = replaceSpace(data);
                            listener.onClickField(face + data, faceApp + data, 4);
                            if (URLUtil.isValidUrl(face + data)) {
                                //      pDialog.show();
                                listener.onClickField(face + data, faceApp + data, 4);
                            } else
                                setError(fragment.getActivity().getString(R.string.url_invalid));
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewInsta:
                        if (!data.isEmpty()) {
                            String insta = "https://www.instagram.com/_u/";
                            if (URLUtil.isValidUrl(insta + data)) {
                                //        pDialog.show();
                                listener.onClickField(insta + data, insta + data, 5);
                            } else
                                setError(fragment.getActivity().getString(R.string.url_invalid));
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewTwi:
                        if (!data.isEmpty()) {
                            String twiApp = "twitter://user?screen_name=";
                            String twiWeb = "https://twitter.com/#!/";
                            data = replaceAt(data);
                            String url = twiWeb + data;
                            if (URLUtil.isValidUrl(url)) {
                                //         pDialog.show();
                                listener.onClickField(url, twiApp + data, 6);
                            } else
                                setError(fragment.getActivity().getString(R.string.url_invalid));
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                    case R.id.textViewSna:
                        if (!data.isEmpty()) {
                            String snapApp = "snapchat://add/";
                            String snapWeb = "https://www.snapchat.com/add/";
                            data = replaceAt(data);
                            String url = snapWeb + data;
                            if (URLUtil.isValidUrl(url)) {
                                //         pDialog.show();
                                listener.onClickField(url, snapWeb + data, 6);
                            } else
                                setError(fragment.getActivity().getString(R.string.url_invalid));
                        } else
                            setError(fragment.getActivity().getString(R.string.data_empty));
                        break;
                }
            }
        });

    }

    private void setError(String error) {
        Utils.showSnackBar(conteiner, error);
    }

    private String replaceAt(String user) {
        user = user.trim();
        return user.replace("@", "");
    }

    private String replaceSpace(String user) {
        return user = user.trim();
    }

    private void initDialog() {
        pDialog = new ProgressDialog(fragment.getActivity());
        pDialog.setMessage(fragment.getActivity().getString(R.string.process));
        pDialog.setCancelable(false);
    }

    public void dismissDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
