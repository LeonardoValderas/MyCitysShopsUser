package com.valdroide.mycitysshopsuser.main.legal;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Support;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.legal.events.LegalActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

public class LegalActivityRepositoryImpl implements LegalActivityRepository {
    EventBus eventBus;
    Support support;

    public LegalActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void getDataLegal(Context context) {
        Utils.writelogFile(context, "Metodo getDataLegal y getSupport(legal, Repository)");
        try {
            support = SQLite.select().from(Support.class).querySingle();
            if (support != null) {
                Utils.writelogFile(context, "support != null(legal, Repository)");
                String about = support.getABOUT();
                String legal = support.getLEGAL();
                if (about != null && legal != null) {
                    Utils.writelogFile(context, "about != null && legal != null(legal, Repository)");
                    if (!about.isEmpty() && !legal.isEmpty()) {
                        Utils.writelogFile(context, "!about.isEmpty() && !legal.isEmpty() y post(legal, Repository)");
                        post(LegalActivityEvent.DATASUCCESS, about, legal);
                    } else {
                        Utils.writelogFile(context, "about.isEmpty() || legal.isEmpty()(legal, Repository)");
                        post(LegalActivityEvent.ERRRO, context.getString(R.string.error_data_base));
                    }
                } else {
                    Utils.writelogFile(context, "about == null || legal == null(legal, Repository)");
                    post(LegalActivityEvent.ERRRO, context.getString(R.string.error_data_base));
                }
            } else {
                Utils.writelogFile(context, "support != null(legal, Repository)");
                post(LegalActivityEvent.ERRRO, context.getString(R.string.error_data_base));
            }
        } catch (Exception e) {
            Utils.writelogFile(context, "catch: " + e.getMessage() + "(legal, Repository)");
            post(LegalActivityEvent.ERRRO, e.getMessage());
        }
    }

    private void post(int type, String about, String legal) {
        post(type, about, legal, null);
    }

    private void post(int type, String error) {
        post(type, null, null, error);
    }

    private void post(int type, String about, String legal, String error) {
        LegalActivityEvent event = new LegalActivityEvent();
        event.setType(type);
        event.setAbout(about);
        event.setLegal(legal);
        event.setError(error);
        eventBus.post(event);
    }

}
