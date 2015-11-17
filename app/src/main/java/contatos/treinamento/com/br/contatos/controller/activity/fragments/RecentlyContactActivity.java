package contatos.treinamento.com.br.contatos.controller.activity.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncInterface;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncLoadList;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 17/11/2015.
 */
public class RecentlyContactActivity extends Fragment implements AsyncInterface {

    private FloatingActionButton fab;
    private RecyclerView contactList;
    private List<Contact> contacts;
    private View contactListFragmentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactListFragmentView = inflater.inflate(R.layout.fragment_contact_list,container,false);

        bindContactList();
        bindFab();
        return contactListFragmentView;
    }

    private void bindContactList() {
        contactList = (RecyclerView) contactListFragmentView.findViewById(R.id.listViewContacts);
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList.setItemAnimator(new DefaultItemAnimator());
    }

    private void bindFab() {
        fab = (FloatingActionButton) contactListFragmentView.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshList(List<Contact> contacts) {

    }
    private void refreshList() {
        this.contacts = ContactBusinessService.loadRecent();
        contactList.setAdapter(new ContactListAdapter(getActivity(), this.contacts));
    }

    @Override
    public void onResume() {
        refreshList();
        super.onResume();
    }
}


