package contatos.treinamento.com.br.contatos.model.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;

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

        db.delete(ContactContract.TABLE, where, values);

        if(contact.getPhoto()!=null)
        BitmapHelper.deleteRecursive(new File(contact.getPhoto()));

        db.close();
        databaseHelper.close();
    }


    public static List<Contact> findContactsByName(String name) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = ContactContract.NAME + " like '" + name + "%'";
        Cursor cursor = db.query(ContactContract.TABLE,ContactContract.columns,where,null,null,null,ContactContract.NAME);
        List<Contact> contacts = ContactContract.getContacts(cursor);

        db.close();
        databaseHelper.close();

        return contacts;
    }


    public static boolean isEmailAlreadyRegistered(String email, Long id) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db  = databaseHelper.getReadableDatabase();

        String where =  ContactContract.EMAIL+ " like '" + email + "'";
        String[] columns =  {ContactContract.EMAIL,ContactContract.ID};
        Cursor cursor = db.query(ContactContract.TABLE, columns, where, null, null, null, null);

        boolean isEmailAlreadyRegistered = ContactContract.verifyEmailAlreadyRegistered(cursor, id);

        return isEmailAlreadyRegistered;
    }

    public static boolean isNameAlreadyRegistered(String name, Long id) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = ContactContract.NAME + " like '" + name + "'";
        String[] columns = {ContactContract.NAME, ContactContract.ID};
        Cursor cursor = db.query(ContactContract.TABLE,columns,where,null,null,null,null);


        boolean isNameAlreadyRegistered = ContactContract.verifyNameAlreadyRegistered(cursor, id);

        return isNameAlreadyRegistered;
    }

    public static List<Contact> loadFavorites() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = ContactContract.ISFAVORITE + " = 1";
        Cursor cursor = db.query(ContactContract.TABLE,ContactContract.columns,where,null,null,null,ContactContract.NAME);

        List<Contact> contacts = ContactContract.getContacts(cursor);

        return contacts;
    }
}
