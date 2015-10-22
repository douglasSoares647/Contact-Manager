package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;


public class ContactListActivity extends AppCompatActivity {

    private RecyclerView contactList;
    private Contact selectedContact;
    private FloatingActionButton fab;
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        bindActionBar();
        bindContactList();
        bindFloatingActionButton();


    }



    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        actionBar.setTitle("Contacts");
        setSupportActionBar(actionBar);
    }

    private void bindFloatingActionButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToContactForm = new Intent(ContactListActivity.this, ContactFormActivity.class);
                startActivity(goToContactForm);
            }
        });
    }

    private void bindContactList() {
        contactList = (RecyclerView) findViewById(R.id.listViewContacts);
        registerForContextMenu(contactList);
        contactList.setLayoutManager(new LinearLayoutManager(this));
        contactList.setItemAnimator(new DefaultItemAnimator());

        contactList.addOnItemTouchListener(
                new RecyclerItemClickListener(ContactListActivity.this, contactList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
                        selectedContact = adapter.getItem(position);

                        Intent goToContactInfo = new Intent(ContactListActivity.this, ContactInformationActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_search) {
            onMenuSearchClick();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuSearchClick() {
        Intent goToSearchForm = new Intent(ContactListActivity.this, ContactSearchActivity.class);
        startActivity(goToSearchForm);
    }

    @Override
    protected void onResume() {
        updateList();
        super.onResume();
    }


    private void updateList() {
        List<Contact> contacts = ContactBusinessService.findContacts();

        for(Contact c : contacts){
            if(c.getBirth().getTime()== new Date().getTime());
        }
        contactList.setAdapter(new ContactListAdapter(this, contacts));
        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contact_list, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.context_menu_edit)
                onMenuEditClick();

        else if (id==R.id.context_menu_delete)
                onMenuDeleteClick();

        else if(id == R.id.context_menu_website)
                onMenuWebSiteClick();

        else if(id == R.id.context_menu_call)
                onMenuCallClick();
        return super.onContextItemSelected(item);
    }

    private void onMenuCallClick() {
        Intent goToCallActivity = new Intent(Intent.ACTION_CALL);
        goToCallActivity.setData(Uri.parse("tel:"+selectedContact.getTelephone()));

        startActivity(goToCallActivity);
    }

    private void onMenuWebSiteClick() {
        try {
            String url = selectedContact.getWebSite();
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "http://" + url;
            Intent goToWebSite = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(goToWebSite);
        }catch(Exception e){
            Toast.makeText(this,getString(R.string.msg_connection_failed), Toast.LENGTH_SHORT).show();
        }

    }

    private void onMenuDeleteClick() {


        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_delete))
                .setNeutralButton(getString(R.string.dialog_no),null)
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactBusinessService.delete(selectedContact);
                        selectedContact = null;
                        updateList();
                    }
                }).setTitle(getString(R.string.dialog_confirm)).create().show();

    }

    private void onMenuEditClick() {
        Intent goToContactForm = new Intent(this, ContactFormActivity.class);
        goToContactForm.putExtra(ContactFormActivity.PARAM_CONTACT, selectedContact);
        startActivity(goToContactForm);
    }

}
