package contatos.treinamento.com.br.contatos.model;

import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.persistence.ContactContract;
import contatos.treinamento.com.br.contatos.model.persistence.ContactRepository;

/**
 * Created by c1284521 on 14/10/2015.
 */
public final class ContactBusinessService {


    public static void save(Contact contact) {
        ContactRepository.save(contact);
    }


    public static List<Contact> findContacts() {
        return ContactRepository.getContacts();
    }

    public static void delete(Contact contact) {
        ContactRepository.delete(contact);
    }

}