package contatos.treinamento.com.br.contatos.model.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.io.File;
import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;
import contatos.treinamento.com.br.contatos.model.util.ListHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public final class ContactRepository {

    public static void save(Contact contact){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = ContactContract.getContentValues(contact);

        if(contact.getId()== null)
        db.insert(ContactContract.TABLE, null, values);
        else {
            String where = ContactContract.ID +" = ?";
            String[] params = {contact.getId().toString()};
            db.update(ContactContract.TABLE,values,where,params);
        }

        db.close();
        databaseHelper.close();

    }


    public static List<Contact> getContacts(){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(ContactContract.TABLE, ContactContract.columns, null, null, null, null, ContactContract.NAME);
        List<Contact> contacts = ContactContract.getContacts(cursor);
        db.close();
        databaseHelper.close();
        return contacts;
    }

    public static void delete(Contact contact) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = ContactContract.ID + " = ?";
        String[] values = {String.valueOf(contact.getId())};

        db.delete(ContactContract.TABLE,where,values);

        if(contact.getPhoto()!=null)
        ListHelper.DeleteRecursive(new File(contact.getPhoto()));

        db.close();
        databaseHelper.close();
    }
}
