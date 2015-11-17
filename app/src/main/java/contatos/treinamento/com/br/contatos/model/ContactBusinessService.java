package contatos.treinamento.com.br.contatos.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.persistence.ContactContract;
import contatos.treinamento.com.br.contatos.model.persistence.ContactRepository;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

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

    public static List<Contact> findContactsByName(String name){
       return ContactRepository.findContactsByName(name);
    }

    public static boolean isEmailAlreadyRegistered(String email, Long id){
        return ContactRepository.isEmailAlreadyRegistered(email, id);
    }


    public static boolean isNameAlreadyRegistered(String name, Long id) {
        return ContactRepository.isNameAlreadyRegistered(name, id);
    }

    public static List<Contact> loadFavorites() {
        return ContactRepository.loadFavorites();
    }

    public static List<Contact> loadRecent(){
        List<Contact> contacts = ContactRepository.getContacts();
        List<Contact> recent = new ArrayList<>();

        Date date = new Date();
        for(Contact contact : contacts){
            long diffDate = ((contact.getLastDateModified().getTime() - date.getTime()) / 1000 / 60 / 60 / 24) * -1;
            if(diffDate<7){
                recent.add(contact);
            }
        }
        return recent;
    }
}
