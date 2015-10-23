package contatos.treinamento.com.br.contatos.controller.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 23/10/2015.
 */
public class AsyncSave extends AsyncTask<Contact,Void,Void> {

    Activity context;
    ProgressDialog pg;

    public AsyncSave(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pg = new ProgressDialog(context);
        pg.setTitle(context.getString(R.string.dialog_wait));
        pg.setMessage(context.getString(R.string.dialog_saving_database));
        pg.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Contact... contacts) {
        ContactBusinessService.save(contacts[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        pg.dismiss();
        super.onPostExecute(aVoid);
    }
}
