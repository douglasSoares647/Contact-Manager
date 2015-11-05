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
    public static final String PHOTO = "photo";
    public static final String EMAIL = "email";
    public static final String TELEPHONE = "telephone";
    public static final String LAST_DATE_MODIFIED = "lastDateModified";
    public static final String ISFAVORITE = "isFavorite";
    public static final String CONTACTCOLOR = "contactColor";

    public static final String[] columns = {ID,NAME,BIRTH,WEBSITE,PHOTO,TELEPHONE,EMAIL,LAST_DATE_MODIFIED,ISFAVORITE,CONTACTCOLOR};


    public static String createTableContact(){
        StringBuilder table = new StringBuilder();
        table.append(" create table "+ TABLE);
        table.append(" ( ");
        table.append(ID + " integer primary key, ");
        table.append(NAME + " text unique not null, ");
        table.append(BIRTH + " text, ");
        table.append(WEBSITE + " text, ");
        table.append(PHOTO + " text, ");
        table.append(EMAIL + " text, ");
        table.append(TELEPHONE + " text, ");
        table.append(LAST_DATE_MODIFIED + " text, ");
        table.append(ISFAVORITE + " integer ,");
        table.append(CONTACTCOLOR + " text );");
        return table.toString();
    }
    public static ContentValues getContentValues(Contact contact){
        ContentValues values = new ContentValues();
        values.put(ID, contact.getId());
        values.put(NAME, contact.getName());
        values.put(BIRTH, FormHelper.convertDateToString(contact.getBirth()));
        values.put(WEBSITE, contact.getWebSite());
        values.put(PHOTO, contact.getPhoto());
        values.put(EMAIL, contact.getEmail());
        values.put(TELEPHONE, contact.getTelephone());
        values.put(LAST_DATE_MODIFIED, FormHelper.convertDateToString(contact.getLastDateModified()));
        values.put(ISFAVORITE, contact.isFavorite()? 1 : 0);
        values.put(CONTACTCOLOR, contact.getContactColor());

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
            contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactContract.PHOTO)));
            contact.setEmail(cursor.getString(cursor.getColumnIndex(ContactContract.EMAIL)));
            contact.setTelephone(cursor.getString(cursor.getColumnIndex(ContactContract.TELEPHONE)));
            contact.setLastDateModified(FormHelper.convertStringToDate(cursor.getString(cursor.getColumnIndex(ContactContract.LAST_DATE_MODIFIED))));
            contact.setIsFavorite((cursor.getLong(cursor.getColumnIndex(ContactContract.ISFAVORITE))) == 1 ? true : false);
            contact.setContactColor(cursor.getString(cursor.getColumnIndex(ContactContract.CONTACTCOLOR)));
            return contact;
        }
        return null;
    }

    public static boolean verifyEmailAlreadyRegistered(Cursor cursor, Long id) {
        if(cursor.moveToNext()){
            Long idContato = cursor.getLong(cursor.getColumnIndex(ContactContract.ID));
            if(!(idContato==id)){
                return true;
            }

        }

        return false;
    }

    public static boolean verifyNameAlreadyRegistered(Cursor cursor, Long id) {
        if(cursor.moveToNext()){
            Long idContacto = cursor.getLong(cursor.getColumnIndex(ContactContract.ID));

            if(!(idContacto==id)){
                return true;
            }
        }
        return false;
    }
}
