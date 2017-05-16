package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.main.FragmentMain.ui.FragmentMain;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogMap implements OnMapReadyCallback {

    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;
    @Bind(R.id.linearConteiner)
    LinearLayout linearConteiner;
    private Context context;
    public AlertDialog alertDialog;
    private SupportMapFragment mapFragment;
    private GoogleMap mapa;
    private Handler touchScreem;
    private double longitudExtra = 0.00;
    private double latitudExtra = 0.00;
    private Fragment fragment;
    private Shop shop;
    private FragmentTransaction fragmentTransaction;

    public DialogMap(Context context, final Fragment fragment, Shop shop, final View view) {
        this.context = context;
        this.fragment = fragment;
        this.shop = shop;
        Utils.writelogFile(context, "DialogMap y AlertDialog(Map)");
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Utils.writelogFile(context, "LayoutInflater(Map)");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Utils.writelogFile(context, "inflater(Map)");
        View layout = inflater.inflate(R.layout.dialog_map, null);
        Utils.writelogFile(context, "builder.setView(layout)(Map)");
        builder.setView(layout);
        Utils.writelogFile(context, "Se inicia ButterKnife(Map)");
        ButterKnife.bind(this, layout);
        try {
            Utils.writelogFile(context, "fill componentes(Map)");
            Utils.setPicasso(context, shop.getURL_LOGO(), R.drawable.ic_launcher, imageViewShop);
            // textViewName.setText(shop.getSHOP());
            if (shop.getLATITUD() != null && shop.getLONGITUD() != null) {
                if (!shop.getLATITUD().isEmpty() && !shop.getLONGITUD().isEmpty()) {
                    latitudExtra = Double.valueOf(shop.getLATITUD());
                    longitudExtra = Double.valueOf(shop.getLONGITUD());
                }
            }
            if (mapFragment == null) {
                mapFragment = (SupportMapFragment) fragment.getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }

            buttonCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close(view);
                }
            });

            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        close(view);
                    }
                    return true;
                }
            });
            alertDialog.show();

        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Map)");
        }
    }

    public void close(View view) {
        deleteFragmentId();
        alertDialog.dismiss();
        view.setEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Utils.writelogFile(context, "onMapReady(Map)");
        try {
            mapa = googleMap;
            touchScreem = new Handler();
            touchScreem.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapa.getUiSettings().setAllGesturesEnabled(true);
                }
            }, 3000);
            mapa.getUiSettings().setAllGesturesEnabled(false);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitudExtra, longitudExtra)).zoom(15).build();
            CameraUpdate cu = CameraUpdateFactory
                    .newCameraPosition(cameraPosition);
            mapa.animateCamera(cu);
            mapa.addMarker(new MarkerOptions().position(
                    new LatLng(latitudExtra, longitudExtra)).title(shop.getSHOP()));
            mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker info) {
                    Utils.writelogFile(context, "onInfoWindowClick(Map)");
                    if (info != null) {
                        Utils.writelogFile(context, "info != null(Map)");
                        if (info.getPosition() != null) {
                            Utils.writelogFile(context, "info.getPosition() != null(Map)");
                            if (info.getPosition().latitude != 0.00 && info.getPosition().longitude != 0.00) {
                                Utils.writelogFile(context, "info.getPosition().latitude != 0.00 && info.getPosition().longitude != 0.00(Map)");
                                intentInfoWindow(info);
                            } else {
                                Utils.writelogFile(context, "latitude == 0.00 || longitude == 0.00(Map)");
                                Utils.showSnackBar(linearConteiner, context.getString(R.string.position_not_found));
                            }
                        } else {
                            Utils.writelogFile(context, "info.getPosition() == null(Map)");
                            Utils.showSnackBar(linearConteiner, context.getString(R.string.position_not_found));
                        }
                    } else {
                        Utils.writelogFile(context, "info == null(Map)");
                        Utils.showSnackBar(linearConteiner, context.getString(R.string.position_not_found));
                    }
                }

            });
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Map)");
            Utils.showSnackBar(linearConteiner, e.getMessage());
        }
    }

    private void deleteFragmentId() {
        Utils.writelogFile(context, "deleteFragmentId(Map)");
        try {
            fragmentTransaction = fragment.getActivity().getSupportFragmentManager()
                    .beginTransaction();

            fragmentTransaction.remove(fragment.getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map));
            fragmentTransaction.commit();
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Map)");
            Utils.showSnackBar(linearConteiner, e.getMessage());
        }
    }

    private void intentInfoWindow(Marker info) {
        Utils.writelogFile(context, "intentInfoWindow(Map)");
        try {
            Uri gmmIntentUri = Uri.parse("geo:" + info.getPosition().latitude + "," + info.getPosition().longitude + "?q=" + info.getTitle().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            fragment.getActivity().startActivity(mapIntent);
        } catch (Exception e) {
            Utils.writelogFile(context, " catch error " + e.getMessage() + "(Map)");
            Utils.showSnackBar(linearConteiner, e.getMessage());
        }
    }
}
