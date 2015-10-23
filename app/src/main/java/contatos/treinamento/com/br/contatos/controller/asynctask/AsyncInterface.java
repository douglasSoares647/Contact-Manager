package contatos.treinamento.com.br.contatos.controller.asynctask;

import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 23/10/2015.
 */
public interface AsyncInterface {

    public void refreshList(List<Contact> contacts);
}
