package com.valdroide.mycitysshopsuser.main.support;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.valdroide.mycitysshopsuser.R;
import com.valdroide.mycitysshopsuser.entities.shop.Support;
import com.valdroide.mycitysshopsuser.lib.base.EventBus;
import com.valdroide.mycitysshopsuser.main.support.events.SupportActivityEvent;
import com.valdroide.mycitysshopsuser.utils.Utils;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public class SupportActivityRepositoryImpl implements SupportActivityRepository {
    private EventBus eventBus;
    private Support support;
    private boolean isSent = false;

    public SupportActivityRepositoryImpl(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void sendEmail(final Context context, final String comment) {
        Utils.writelogFile(context, "Metodo sendEmail y getSupport(support, Repository)");
        support = getSupport(context);
        if (support != null) {
            Utils.writelogFile(context, "support != null(support, Repository)");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Utils.writelogFile(context, "sendEmail(support, Repository)");
                    isSent = sendEmail(context, support, "Usuario", comment);
                }
            };
            new Thread(runnable).start();
        } else {
            Utils.writelogFile(context, " Base de datos error " + context.getString(R.string.error_data_base) + "(support, Repository)");
            post(SupportActivityEvent.ERROR, context.getString(R.string.error_data_base));
        }
        Utils.writelogFile(context, "post SENDEMAILSUCCESS(support, Repository)");
    }

    private Support getSupport(Context context) {
        Utils.writelogFile(context, "getSupport(support, Repository)");
        return SQLite.select().from(Support.class).querySingle();
    }

    public boolean sendEmail(Context context, final Support support, String Shop_name, String comment) {
        Utils.writelogFile(context, "Metodo sendEmail y Se valida conexion a internet(support, Repository)");
        if (Utils.isNetworkAvailable(context)) {
            Utils.writelogFile(context, "setSupport variables(support, Repository)");
            String to = support.getTO();
            final String from = support.getFROM();
            final String password = support.getPASS();

            try {
                Utils.writelogFile(context, "createMailcapCommandMap(support, Repository)");
                Utils.createMailcapCommandMap();
                Utils.writelogFile(context, "createPropertiesAndSession(support, Repository)");
                Session session = Utils.createPropertiesAndSession(from, password);
                Utils.writelogFile(context, "createMimeMessage(support, Repository)");
                MimeMessage message = Utils.createMimeMessage(session, from, to, Shop_name, context);
                Utils.writelogFile(context, "createMimeBodyPart(support, Repository)");
                BodyPart messageBodyPart1 = Utils.createMimeBodyPart(comment);
                Utils.writelogFile(context, "createMimeBodyPart2(support, Repository)");
                MimeBodyPart messageBodyPart2 = Utils.createMimeBodyPart2(context.getFilesDir() + "/" + context.getResources().getString(R.string.log_file_name));
                Utils.writelogFile(context, "createMultipart(support, Repository)");
                Multipart multipart = Utils.createMultipart(messageBodyPart1, messageBodyPart2);
                Utils.writelogFile(context, "setContent(support, Repository)");
                message.setContent(multipart);
                Utils.writelogFile(context, "send(support, Repository)");
                Transport.send(message);
                post(SupportActivityEvent.SENDEMAILSUCCESS);
                return true;
            } catch (MessagingException ex) {
                Utils.writelogFile(context, " send error " + ex.getMessage() + "(support, Repository)");
                post(SupportActivityEvent.ERROR, ex.getMessage());
                return false;
            }
        } else {
            Utils.writelogFile(context, " Internet error " + context.getString(R.string.error_internet) + "(support, Repository)");
            post(SupportActivityEvent.ERROR, context.getString(R.string.error_internet));
            return false;
        }
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String error) {
        SupportActivityEvent event = new SupportActivityEvent();
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }
}
