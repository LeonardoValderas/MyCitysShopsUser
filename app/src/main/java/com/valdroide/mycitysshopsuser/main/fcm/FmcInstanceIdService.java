package com.valdroide.mycitysshopsuser.main.fcm;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.Operator;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.api.APIService;
import com.valdroide.mycitysshopsuser.api.ShopClient;
import com.valdroide.mycitysshopsuser.entities.response.ResponseWS;
import com.valdroide.mycitysshopsuser.entities.shop.Token;
import com.valdroide.mycitysshopsuser.entities.shop.Token_Table;
import com.valdroide.mycitysshopsuser.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FmcInstanceIdService extends FirebaseInstanceIdService {
    ShopClient client = new ShopClient();
    APIService service = client.getAPIService();
    private ResponseWS responseWS;

    @Override
    public void onTokenRefresh() {
        Utils.writelogFile(getApplicationContext(), "onTokenRefresh(FmcInstanceIdService)");
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Utils.writelogFile(getApplicationContext(), "FirebaseInstanceId.getInstance().getToken()(FmcInstanceIdService)");
        if (recent_token != null) {
            Utils.writelogFile(getApplicationContext(), "recent_token != null(FmcInstanceIdService)");
            if (!recent_token.isEmpty()) {
                Utils.writelogFile(getApplicationContext(), "!recent_token.isEmpty()(FmcInstanceIdService)");
                Token token = getToken();
                if (token != null) {
                    Utils.writelogFile(getApplicationContext(), "token != null(FmcInstanceIdService)");
                    String current_token = token.getTOKEN();
                    if (current_token.compareTo(recent_token) != 0) {
                        Utils.writelogFile(getApplicationContext(), "update token(FmcInstanceIdService)");
                        token.setTOKEN(recent_token);
                        //UPDATE
                        validateToken(getApplicationContext(), token, false);
                    } else {
                        Utils.writelogFile(getApplicationContext(), "insert token(FmcInstanceIdService)");
                        if (Utils.getIdCity(getApplicationContext()) != 0) {
                            token = setTokenDB(recent_token);
                            //INSERT
                            validateToken(getApplicationContext(), token, true);
                        } else {
                            Utils.writelogFile(getApplicationContext(), "id_city is 0. No selecciono una ciudad(FmcInstanceIdService)");
                        }
                    }
                } else {
                    Utils.writelogFile(getApplicationContext(), "token == null(FmcInstanceIdService)");
                    if (Utils.getIdCity(getApplicationContext()) != 0) {
                        token = setTokenDB(recent_token);
                        //INSERT
                        validateToken(getApplicationContext(), token, true);
                    } else {
                        Utils.writelogFile(getApplicationContext(), "id_city is 0. No selecciono una ciudad(FmcInstanceIdService)");
                    }
                }
            }
        }
    }

    private Token setTokenDB(String recent_token) {
        Token token = new Token();
        token.setID_TOKEN_KEY(0);
        token.setTOKEN(recent_token);
        token.setID_CITY_FOREIGN(Utils.getIdCity(getApplicationContext()));
        return token;
    }

    public Token getToken() {
        Utils.writelogFile(getApplicationContext(), "getToken(FmcInstanceIdService)");
        return SQLite.select().from(Token.class).where(OperatorGroup.clause()
                .and(Token_Table.ID_CITY_FOREIGN.is(Utils.getIdCity(getApplicationContext())))).querySingle();
    }

    public void validateToken(final Context context, final Token token, final boolean isInsert) {
        Utils.writelogFile(context, "Metodo validateToken y Se valida conexion a internet(FmcInstanceIdService)");
        if (Utils.isNetworkAvailable(context)) {
            Utils.writelogFile(context, "processToken FirebaseInstanceId.getInstance().getToken()(FmcInstanceIdService)");
            try {
                Call<ResponseWS> tokenService = service.setToken(token.getID_TOKEN_KEY(), token.getTOKEN(),
                        token.getID_CITY_FOREIGN(), isInsert);
                tokenService.enqueue(new Callback<ResponseWS>() {
                    @Override
                    public void onResponse(Call<ResponseWS> call, Response<ResponseWS> response) {
                        if (response.isSuccessful()) {
                            Utils.writelogFile(context, "isSuccessful(FmcInstanceIdService)");
                            responseWS = response.body();
                            if (responseWS != null) {
                                Utils.writelogFile(context, "responseWS != null(FmcInstanceIdService)");
                                if (responseWS.getSuccess().equals("0")) {
                                    Utils.writelogFile(context, "responseWS.getSuccess().equals(0)(FmcInstanceIdService)");
                                    if (isInsert) {
                                        int id = responseWS.getId();
                                        if (id != 0) {
                                            Utils.writelogFile(context, "id != 0 y save(FmcInstanceIdService)");
                                            token.setID_TOKEN_KEY(id);
                                            token.save();
                                        } else {
                                            Toast.makeText(getApplicationContext(), context.getString(R.string.error_data_base), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Utils.writelogFile(context, "update(FmcInstanceIdService)");
                                        token.update();
                                    }
                                } else {
                                    Utils.writelogFile(context, "responseWS error: " + responseWS.getMessage() + "(FmcInstanceIdService)");
                                    Toast.makeText(getApplicationContext(), responseWS.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Utils.writelogFile(context, "responseWS == null(FmcInstanceIdService)");
                                Toast.makeText(getApplicationContext(), context.getString(R.string.error_data_base), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Utils.writelogFile(context, "!response.isSuccessful()(FmcInstanceIdService)");
                            Toast.makeText(getApplicationContext(), context.getString(R.string.error_data_base), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseWS> call, Throwable t) {
                        Utils.writelogFile(context, "Call error: " + t.getMessage() + "(FmcInstanceIdService)");
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Utils.writelogFile(context, "catch error: " + e.getMessage() + "(FmcInstanceIdService)");
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Utils.writelogFile(context, "internt error(FmcInstanceIdService)");
            Toast.makeText(getApplicationContext(), context.getString(R.string.error_internet), Toast.LENGTH_LONG).show();
        }
    }
}
