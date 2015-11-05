package contatos.treinamento.com.br.contatos.model.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.activity.ContactFormActivity;
import contatos.treinamento.com.br.contatos.controller.interfaces.UpdatableViewPager;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 05/11/2015.
 */
public final class MenuHelper {

    public static void onMenuFavoriteClick(Contact selectedContact, Context context) {
        selectedContact.setIsFavorite(true);
        ContactBusinessService.save(selectedContact);

        UpdatableViewPager activityWithViewPager = (UpdatableViewPager) context;
        activityWithViewPager.updateViewPager();
    }

    public static void onMenuCallClick(Contact selectedContact ,Context context) {
        Intent goToCallActivity = new Intent(Intent.ACTION_CALL);
        goToCallActivity.setData(Uri.parse("tel:" + selectedContact.getTelephone()));
        context.startActivity(goToCallActivity);
    }

    public static void onMenuWebSiteClick(Contact selectedContact , Context context) {
        try {
            String url = selectedContact.getWebSite();
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "http://" + url;
            Intent goToWebSite = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(goToWebSite);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.msg_connection_failed), Toast.LENGTH_SHORT).show();
        }

    }

    public static void onMenuDeleteClick(final Contact selectedContact, final Context context) {


        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.dialog_delete))
                .setNeutralButton(context.getString(R.string.dialog_no), null)
                .setPositiveButton(context.getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactBusinessService.delete(selectedContact);
                        UpdatableViewPager updatableViewPager = (UpdatableViewPager) context;
                        updatableViewPager.updateViewPager();
                    }
                }).setTitle(context.getString(R.string.dialog_confirm)).create().show();

    }


    public static void onMenuEditClick(Contact selectedContact, Context context) {
        Intent goToContactForm = new Intent(context, ContactFormActivity.class);
        goToContactForm.putExtra(ContactFormActivity.PARAM_CONTACT, selectedContact);

        context.startActivity(goToContactForm);

    }
}
