package contatos.treinamento.com.br.contatos.controller.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 23/10/2015.
 */
public class AsyncLoadList extends AsyncTask <Void,Integer,List<Contact>>{

    private AsyncInterface activity;
    private Activity context;
    private ProgressDialog pg;

    public AsyncLoadList(AsyncInterface activity,Activity context) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        pg = new ProgressDialog(context);
        pg.setTitle(R.string.dialog_wait);
        pg.setMessage(context.getString(R.string.dialog_load));
        pg.show();
        super.onPreExecute();
    }

    @Override
    protected List<Contact> doInBackground(Void... voids) {
        List<Contact> contacts = ContactBusinessService.findContacts();
        return contacts;
    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        activity.refreshList(contacts);
        pg.dismiss();
        super.onPostExecute(contacts);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
