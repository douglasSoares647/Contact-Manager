package contatos.treinamento.com.br.contatos.model.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public final class ContactContract {

    public static final String TABLE = "Contact";
    public static final String ID = "id";
    public static final String NAME = "Name";
    public static final String BIRTH = "BIRTH";
    public static final String WEBSITE = "WebSite";
    public static final String RATING = "Rating";
    public static final String PHOTO = "photo";
    public static final String EMAIL = "email";
    public static final String TELEPHONE = "telephone";


    public static final String[] columns = {ID,NAME,BIRTH,WEBSITE,RATING,PHOTO,TELEPHONE,EMAIL};

    public static String createTableContact(){
        StringBuilder table = new StringBuilder();
        table.append(" create table "+ TABLE);
        table.append(" ( ");
        table.append(ID + " integer primary key, ");
        table.append(NAME + " text unique not null, ");
        table.append(BIRTH + " text, ");
        table.append(WEBSITE + " text, ");
        table.append(RATING + " real, ");
        table.append(PHOTO + " text, ");
        table.append(EMAIL + " text, ");
        table.append(TELEPHONE + " text ); ");

        return table.toString();
    }
    public static ContentValues getContentValues(Contact contact){
        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(BIRTH, FormHelper.convertDateToString(contact.getBirth()));
        values.put(WEBSITE, contact.getWebSite());
        values.put(RATING, contact.getRating());
        values.put(PHOTO, contact.getPhoto());
        values.put(EMAIL, contact.getEmail());
        values.put(TELEPHONE, contact.getTelephone());

        return values;
    }



    public static List<Contact> getContacts(Cursor cursor){
        List<Contact> contacts = new ArrayList<>();
        while(cursor.moveToNext()){
            contacts.add(getContact(cursor));
        }
        return contacts;
    }


    public static Contact getContact(Cursor cursor) {
        Contact contact = new Contact();

        while(!cursor.isBeforeFirst() || cursor.moveToNext()){
            contact.setId(cursor.getLong(cursor.getColumnIndex(ContactContract.ID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactContract.NAME)));
            contact.setWebSite(cursor.getString(cursor.getColumnIndex(ContactContract.WEBSITE)));
            contact.setBirth(FormHelper.convertStringToDate(cursor.getString(cursor.getColumnIndex(ContactContract.BIRTH))));
            contact.setRating(cursor.getFloat(cursor.getColumnIndex(ContactContract.RATING)));
            contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactContract.PHOTO)));
            contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactContract.EMAIL)));
            contact.setTelephone(cursor.getString(cursor.getColumnIndex(ContactContract.TELEPHONE)));
            return contact;
        }
        return null;
    }
}
