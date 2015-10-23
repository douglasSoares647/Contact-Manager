package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.adapter.NavigationAdapter;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncInterface;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncLoadList;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.entity.NavigationItem;


public class ContactListActivity extends AppCompatActivity implements AsyncInterface{

    private RecyclerView contactList;
    private Contact selectedContact;
    private FloatingActionButton fab;
    private Toolbar actionBar;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        bindActionBar();
        bindContactList();
        bindFloatingActionButton();
        bindDrawerList();
        bindDrawerLayout();

    }


    private void bindDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);


    }

    private void bindDrawerList() {
        drawerList = (ListView) findViewById(R.id.drawerList);
        List<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(R.mipmap.ic_navigation_drawer, getString(R.string.navigation_birthdays)));
        navigationItems.add(new NavigationItem(R.mipmap.ic_navigation_drawer, "Teste"));

        drawerList.setAdapter(new NavigationAdapter(this, navigationItems));
    }


    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        actionBar.setTitle("Contacts");
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setNavigationIcon(R.mipmap.ic_navigation_drawer);
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
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuSearchClick() {
        Intent goToSearchForm = new Intent(ContactListActivity.this, ContactSearchActivity.class);
        startActivity(goToSearchForm);
    }

    @Override
    protected void onResume() {
            AsyncLoadList asyncLoadList = new AsyncLoadList(this, this);
            asyncLoadList.execute();

        super.onResume();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contact_list, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.context_menu_edit)
            onMenuEditClick();

        else if (id == R.id.context_menu_delete)
            onMenuDeleteClick();

        else if (id == R.id.context_menu_website)
            onMenuWebSiteClick();

        else if (id == R.id.context_menu_call)
            onMenuCallClick();
        return super.onContextItemSelected(item);
    }

    private void onMenuCallClick() {
        Intent goToCallActivity = new Intent(Intent.ACTION_CALL);
        goToCallActivity.setData(Uri.parse("tel:" + selectedContact.getTelephone()));

        startActivity(goToCallActivity);
    }

    private void onMenuWebSiteClick() {
        try {
            String url = selectedContact.getWebSite();
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                url = "http://" + url;
            Intent goToWebSite = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(goToWebSite);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.msg_connection_failed), Toast.LENGTH_SHORT).show();
        }

    }

    private void onMenuDeleteClick() {


        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_delete))
                .setNeutralButton(getString(R.string.dialog_no), null)
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactBusinessService.delete(selectedContact);
                        contacts.remove(selectedContact);
                        selectedContact = null;
                        setAdapter();
                    }
                }).setTitle(getString(R.string.dialog_confirm)).create().show();

    }



    private void onMenuEditClick() {
        Intent goToContactForm = new Intent(this, ContactFormActivity.class);
        goToContactForm.putExtra(ContactFormActivity.PARAM_CONTACT, selectedContact);
        startActivity(goToContactForm);
    }

    @Override
    public void refreshList(List<Contact> contacts) {
        this.contacts = contacts;
        setAdapter();
    }


    private void setAdapter() {
        contactList.setAdapter(new ContactListAdapter(this, this.contacts));
        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
        adapter.notifyDataSetChanged();
    }
}
