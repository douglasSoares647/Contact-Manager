package contatos.treinamento.com.br.contatos.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import contatos.treinamento.com.br.contatos.model.util.ApplicationUtil;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contactManagerdb";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(){
        return new DatabaseHelper(ApplicationUtil.getContext());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactContract.createTableContact());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
