package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncInterface;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncLoadList;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;


public class ContactListActivity extends Fragment implements AsyncInterface {

    private RecyclerView contactList;
    private Contact selectedContact;
    private FloatingActionButton fab;
    private List<Contact> contacts;
    private View contactListFragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactListFragmentView = inflater.inflate(R.layout.activity_contact_list, container, false);
        bindContactList();
        bindFloatingActionButton();

        return contactListFragmentView;

    }

    @Override
    public void onResume() {
        AsyncLoadList asyncLoadList = new AsyncLoadList(this, getActivity());
        asyncLoadList.execute();
        super.onResume();
    }

    private void bindFloatingActionButton() {
        fab = (FloatingActionButton) contactListFragmentView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToContactForm = new Intent(getActivity(), ContactFormActivity.class);
                startActivity(goToContactForm);
            }
        });
    }

    private void bindContactList() {
        contactList = (RecyclerView) contactListFragmentView.findViewById(R.id.listViewContacts);
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(contactList);

        contactList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), contactList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
                        selectedContact = adapter.getItem(position);

                        Intent goToContactInfo = new Intent(getActivity(), ContactInformationActivity.class);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu_contact_list, menu);
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
            Toast.makeText(getActivity(), getString(R.string.msg_connection_failed), Toast.LENGTH_SHORT).show();
        }

    }

    private void onMenuDeleteClick() {


        new AlertDialog.Builder(getActivity())
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
        Intent goToContactForm = new Intent(getActivity().getBaseContext(), ContactFormActivity.class);
        goToContactForm.putExtra(ContactFormActivity.PARAM_CONTACT, selectedContact);

        startActivity(goToContactForm);

    }

    @Override
    public void refreshList(List<Contact> contacts) {
        this.contacts = contacts;
        setAdapter();
    }


    private void setAdapter() {
        contactList.setAdapter(new ContactListAdapter(getActivity(), this.contacts));
        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
        adapter.notifyDataSetChanged();
    }

}
