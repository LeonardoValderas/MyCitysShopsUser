package com.valdroide.mycitysshopsuser.main.place.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.place.City;
import com.valdroide.mycitysshopsuser.entities.place.Country;
import com.valdroide.mycitysshopsuser.entities.place.MyPlace;
import com.valdroide.mycitysshopsuser.entities.place.State;
import com.valdroide.mycitysshopsuser.main.place.PlaceActivityPresenter;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerCity;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerCountry;
import com.valdroide.mycitysshopsuser.main.place.ui.adapters.AdapterSpinnerState;
import com.valdroide.mycitysshopsuser.main.splash.ui.SplashActivity;
import com.valdroide.mycitysshopsuser.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceActivity extends AppCompatActivity implements PlaceActivityView {

    @Bind(R.id.textCountry)
    TextView textCountry;
    @Bind(R.id.spinnerCountry)
    Spinner spinnerCountry;
    @Bind(R.id.textState)
    TextView textState;
    @Bind(R.id.spinnerState)
    Spinner spinnerState;
    @Bind(R.id.textCity)
    TextView textCity;
    @Bind(R.id.spinnerCity)
    Spinner spinnerCity;
    @Bind(R.id.buttonInto)
    Button buttonInto;
    @Bind(R.id.activity_place)
    RelativeLayout conteiner;

    private List<Country> countries;
    private List<State> states;
    private List<City> cities;
    private Country country;
    private State state;
    private City city;
    private int id_country = 0, id_state = 0;
    private ProgressDialog pDialog;

    @Inject
    PlaceActivityPresenter presenter;
    @Inject
    AdapterSpinnerCountry adapterSpinnerCountry;
    @Inject
    AdapterSpinnerState adapterSpinnerState;
    @Inject
    AdapterSpinnerCity adapterSpinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        Utils.writelogFile(this, "Se inicia ButterKnife(Place)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(Place)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(Place)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se initDialog(Place)");
        initDialog();
        Utils.writelogFile(this, "dialog show(Place)");
        pDialog.show();
        initAdapterSpinner();
        setOnItemSelectedCountry();
        setOnItemSelectedState();
        Utils.writelogFile(this, "getCountries(Place)");
        presenter.getCountries(this);
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getPlaceActivityComponent(this, this).inject(this);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.process));
        pDialog.setCancelable(false);
    }

    public void initAdapterSpinner() {
        Utils.writelogFile(this, "initAdapterSpinner(Place)");
        try {
            spinnerCountry.setAdapter(adapterSpinnerCountry);
            spinnerState.setAdapter(adapterSpinnerState);
            spinnerCity.setAdapter(adapterSpinnerCity);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    public void setOnItemSelectedCountry() {
        Utils.writelogFile(this, "setOnItemSelectedCountry(Place)");
        try {
            pDialog.show();
            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    id_country = countries.get(position).getID_COUNTRY_KEY();
                    presenter.getStateForCountry(PlaceActivity.this, id_country);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    public void setOnItemSelectedState() {
        Utils.writelogFile(this, "setOnItemSelectedState(Place)");
        try {
            pDialog.show();
            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    id_state = states.get(position).getID_STATE_KEY();
                    presenter.getCitiesForState(PlaceActivity.this, id_state);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    @Override
    public void setCountry(List<Country> countries) {
        Utils.writelogFile(this, "setCountry: " + countries.size() + "(Place)");
        try {
            this.countries = countries;
            adapterSpinnerCountry.clear();
            adapterSpinnerCountry.addAll(countries);
            adapterSpinnerCountry.notifyDataSetChanged();
            validateDialog();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    private void validateDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void setState(List<State> states) {
        Utils.writelogFile(this, "setState: " + states.size() + "(Place)");
        try {
            this.states = states;
            adapterSpinnerState.clear();
            adapterSpinnerState.addAll(states);
            adapterSpinnerState.notifyDataSetChanged();
            if(states.size() > 0) {
                id_state = states.get(0).getID_STATE_KEY();
                presenter.getCitiesForState(PlaceActivity.this, id_state);
            }
            validateDialog();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    @Override
    public void setCity(List<City> cities) {
        Utils.writelogFile(this, "setCity: " + cities.size() + "(Place)");
        try {
            this.cities = cities;
            adapterSpinnerCity.clear();
            adapterSpinnerCity.addAll(cities);
            adapterSpinnerCity.notifyDataSetChanged();
            validateDialog();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    @Override
    public void setError(String mgs) {
        Utils.writelogFile(this, "setError: " + mgs + "(Place)");
        validateDialog();
        Utils.showSnackBar(conteiner, mgs);
    }

    @OnClick(R.id.buttonInto)
    @Override
    public void savePlace() {
        Utils.writelogFile(this, "savePlace onclick buttonInto (Place)");
        try {
            if (spinnerCountry.getSelectedItem() == null && spinnerState.getSelectedItem() == null && spinnerCity.getSelectedItem() == null)
                Utils.showSnackBar(conteiner, getString(R.string.error_fill_place));
            else {
                pDialog.show();
                country = (Country) spinnerCountry.getSelectedItem();
                state = (State) spinnerState.getSelectedItem();
                city = (City) spinnerCity.getSelectedItem();
                MyPlace myPlace = new MyPlace();
                myPlace.setID_PLACE_KEY(1);
                myPlace.setID_COUNTRY_FOREIGN(country.getID_COUNTRY_KEY());
                myPlace.setID_STATE_FOREIGN(state.getID_STATE_KEY());
                myPlace.setID_CITY_FOREIGN(city.getID_CITY_KEY());

                presenter.savePlace(this, myPlace);
            }
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    @Override
    public void saveSuccess(MyPlace place) {
        Utils.writelogFile(this, "saveSuccess(Place)");
        try {
            Utils.setIdCity(this, place.getID_CITY_FOREIGN());
            validateDialog();
            startActivitySplash();
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    public void startActivitySplash() {
        Utils.writelogFile(this, "startActivitySplash(Place)");
        try {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra("isPlace", true);
            startActivity(intent);
        } catch (Exception e) {
            setError(e.getMessage());
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(Place)");
        }
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(Place)");
        presenter.onDestroy();
        pDialog.dismiss();
        super.onDestroy();
    }
}
