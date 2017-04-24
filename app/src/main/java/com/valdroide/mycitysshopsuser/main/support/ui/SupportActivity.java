package com.valdroide.mycitysshopsuser.main.support.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.valdroide.mycitysshopsuser.MyCitysShopsUserApp;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.main.support.SupportActivityPresenter;
import com.valdroide.mycitysshopsuser.utils.Utils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupportActivity extends AppCompatActivity implements SupportActivityView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.editTextEmail)
    EditText editTextEmail;
    @Bind(R.id.content_navigation)
    RelativeLayout contentNavigation;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.editTextEmailHint)
    TextInputLayout editTextEmailHint;
    private ProgressDialog pDialog;
    @Inject
    SupportActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        Utils.writelogFile(this, "Se inicia ButterKnife(support)");
        ButterKnife.bind(this);
        Utils.writelogFile(this, "Se inicia Injection(support)");
        setupInjection();
        Utils.writelogFile(this, "Se inicia presenter Oncreate(support)");
        presenter.onCreate();
        Utils.writelogFile(this, "Se inicia toolbar Oncreate(support)");
        initToolBar();
        Utils.writelogFile(this, "Se inicia dialog Oncreate(support)");
        initDialog();
    }

    private void setupInjection() {
        MyCitysShopsUserApp app = (MyCitysShopsUserApp) getApplication();
        app.getSupportActivityComponent(this, this).inject(this);
    }

    public void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.support_title);
        Utils.applyFontForToolbarTitle(this, toolbar);
    }

    public void initDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.process));
        pDialog.setCancelable(false);
    }

    @Override
    @OnClick(R.id.fab)
    public void sendEmail() {
        try {
            if (editTextEmail.getText().toString().equals(""))
                setError(getString(R.string.insert_commentary));
            else {
                pDialog.show();
                presenter.sendEmail(this, editTextEmail.getText().toString());
            }
        } catch (Exception e) {
            Utils.writelogFile(this, " catch error " + e.getMessage() + "(support)");
            setError(e.getMessage());
        }
    }

    @Override
    public void sendEmailSuccess() {
        Utils.writelogFile(this, "sendEmailSuccess(support)");
        setText(editTextEmail, "");
        if (pDialog.isShowing())
            pDialog.dismiss();
        setError(getString(R.string.email_success));
    }

    @Override
    public void setError(String error) {
        Utils.writelogFile(this, " setError " + error + "(support)");
        if (pDialog.isShowing())
            pDialog.dismiss();
        Utils.showSnackBar(contentNavigation, error);
    }

    private void setText(final EditText editText,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText(value);
            }
        });
    }

    @Override
    protected void onDestroy() {
        Utils.writelogFile(this, "onDestroy(support)");
        presenter.onDestroy();
        pDialog.dismiss();
        super.onDestroy();
    }
}
