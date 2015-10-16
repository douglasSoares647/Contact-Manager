package contatos.treinamento.com.br.contatos.model.util;

import android.content.Context;

/**
 * Created by c1284521 on 14/10/2015.
 */
public final class ApplicationUtil {


    private static Context applicationContext;
    private ApplicationUtil(){
        super();
    }


    public static void setContext(Context context){
        applicationContext = context;
    }
    public static Context getContext(){
        return applicationContext;
    }


}
