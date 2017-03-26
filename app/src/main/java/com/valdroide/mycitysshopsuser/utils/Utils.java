package com.valdroide.mycitysshopsuser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.valdroide.mycitysshopsuser.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String URL_IMAGE = "http://10.0.2.2:8080/my_citys_shops_adm/account/image_account/";
    //public static String URL_IMAGE = "http://myd.esy.es/myd/clothes/image_clothes/";

    public static String ERROR_DATA_BASE = "Error al guardar los datos.";
    public static String ERROR_INTERNET = "Verificar su conexión de Internet.";
    public static String ERROR_OFFER_VALIDATE = "Problemas al validar sus Promos.";

    //FECHAS
    public static String getFechaInit() {
        Date dateOficial = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateOficial);
    }

    public static String getFechaOficial() {
        Date dateOficial = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(dateOficial);
    }

    public static boolean oldPhones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return false;
        else
            return true;
    }

    public static String getLastDateWeek() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.DATE, 7);
        return df.format(cal.getTime());
    }

    public static String getLastDateMonth() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        cal.set(Calendar.DATE, Calendar.getInstance().getActualMaximum(Calendar.DATE));
        return df.format(cal.getTime());
    }

    public static boolean validateExpirateOffer(String dateExperate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(dateExperate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (strDate != null)
            if (System.currentTimeMillis() > strDate.getTime()) {
                return true;
            }
        return false;
    }

    public static void showSnackBar(View conteiner, String msg) {
        Snackbar.make(conteiner, msg, Snackbar.LENGTH_LONG).show();
    }

    public static byte[] readBytes(Uri uri, Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static void setPicasso(Context context, String url, final int resource, final ImageView imageView) {
        Picasso.with(context)
                .load(url).fit()
                .placeholder(resource)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(resource);
                    }
                });
    }

    public static void setPicasso(Context context, Uri uri, final int resource, final ImageView imageView) {
        Picasso.with(context)
                .load(uri.toString()).fit()
                .placeholder(resource)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        imageView.setImageResource(resource);
                    }
                });
    }

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isNetworkAvailableNonStatic(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static Map<Character, Character> MAP_NORM;

    public static String removeAccents(String value) {
        if (MAP_NORM == null || MAP_NORM.size() == 0) {
            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ñ', 'N');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            MAP_NORM.put('ñ', 'n');
            MAP_NORM.put('ç', 'c');
        }
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(value);
        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }
        return sb.toString();
    }

    public static void setIdCity(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_city), id);
        editor.commit();
    }

    public static int getIdCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_city), 0);
    }

    public static void resetIdCity(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_city_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_city), 0);
        editor.commit();
    }

/*
    public static void setIdUser(Context context, int id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_user), id);
        editor.commit();
    }

    public static int getIdUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_id_shared), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(context.getString(R.string.id_user), 0);
    }

    public static void resetIdUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_id_shared), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(context.getString(R.string.id_user), 0);
        editor.commit();
    }
    */
}
