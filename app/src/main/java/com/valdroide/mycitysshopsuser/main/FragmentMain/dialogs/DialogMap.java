package com.valdroide.mycitysshopsuser.main.FragmentMain.dialogs;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Shop;
import com.valdroide.mycitysshopsuser.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogMap implements OnMapReadyCallback {

    @Bind(R.id.imageViewShop)
    ImageView imageViewShop;
    @Bind(R.id.buttonCerrar)
    ImageView buttonCerrar;
    @Bind(R.id.textViewName)
    TextView textViewName;
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

    public DialogMap(Context context, final Fragment fragment, Shop shop) {
        this.context = context;
        this.fragment = fragment;
        this.shop = shop;

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.dialog_map, null);
        builder.setView(layout);
        ButterKnife.bind(this, layout);

        Utils.setPicasso(context, shop.getURL_LOGO(), R.mipmap.ic_launcher, imageViewShop);

        textViewName.setText(shop.getSHOP());
        if (shop.getLATITUD() != null && shop.getLONGITUD() != null) {
            latitudExtra = Double.valueOf(shop.getLATITUD());
            longitudExtra = Double.valueOf(shop.getLONGITUD());
        }
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) fragment.getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        buttonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFragmentId();
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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
    }

    public void deleteFragmentId(){
        fragmentTransaction = fragment.getActivity().getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.remove( fragment.getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map));
        fragmentTransaction.commit();
    }
}
