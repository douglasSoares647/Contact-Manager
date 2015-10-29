package contatos.treinamento.com.br.contatos.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncInterface;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncLoadList;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 20/10/2015.
 */
public class ContactSearchActivity extends AppCompatActivity implements AsyncInterface {

    private RecyclerView contactList;
    private Toolbar actionBar;

    private Contact selectedContact;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_contact);
        bindContactList();
        bindActionBar();

        searchDatabase();

        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void searchDatabase() {
        AsyncLoadList asyncLoadList = new AsyncLoadList(this,this);
        asyncLoadList.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextSearch = (EditText) actionBar.findViewById(R.id.editTextToolBarFind);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                updateListByName(editTextSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextSearch.getText().toString().equals("")) {
                    searchDatabase();
                }
            }
        });

    }

    private void bindContactList() {

        contactList = (RecyclerView) findViewById(R.id.listViewContactsFound);
        registerForContextMenu(contactList);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        contactList.setItemAnimator(new DefaultItemAnimator());

        contactList.addOnItemTouchListener(
                new RecyclerItemClickListener(ContactSearchActivity.this, contactList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
                        selectedContact = adapter.getItem(position);

                        Intent goToContactInfo = new Intent(ContactSearchActivity.this, ContactInformationActivity.class);
                        goToContactInfo.putExtra(ContactInformationActivity.PARAM_CONTACTINFO, selectedContact);
                        startActivity(goToContactInfo);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
                        selectedContact = adapter.getItem(position);
                    }
                }
                )
        );

    }


    private void updateListByName(String name){
        List<Contact> contactsByName = ContactBusinessService.findContactsByName(name);
        setAdapter(contactsByName);
    }

    public void setAdapter(List<Contact>contacts){
        contactList.setAdapter(new ContactListAdapter(this, contacts));
        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshList(List<Contact> contacts) {
        setAdapter(contacts);
    }

    @Override
    protected void onResume() {
        String name = editTextSearch.getText().toString();

        if (!name.isEmpty())
        updateListByName(name);
        else {
            searchDatabase();
        }

        super.onResume();

    }
}
