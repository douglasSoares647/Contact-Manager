package contatos.treinamento.com.br.contatos.model;

import android.app.Application;

import contatos.treinamento.com.br.contatos.model.util.ApplicationUtil;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class ContactManagerApplication extends Application {


        public void onCreate() {
            super.onCreate();
            ApplicationUtil.setContext(getApplicationContext());

        }
}
