package contatos.treinamento.com.br.contatos.controller.activity.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.activity.ContactFormActivity;
import contatos.treinamento.com.br.contatos.controller.activity.ContactInformationActivity;
import contatos.treinamento.com.br.contatos.controller.activity.ContactPhotoActivity;
import contatos.treinamento.com.br.contatos.controller.adapter.ContactListAdapter;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncInterface;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncLoadList;
import contatos.treinamento.com.br.contatos.controller.interfaces.UpdatableViewPager;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
import contatos.treinamento.com.br.contatos.model.util.MenuHelper;


public class ContactListFragment extends Fragment implements AsyncInterface {

    private static final int RESULTCONTACTINFO = 20;
    private static final int CODE_FROM_PHOTO_ACTIVITY = 2;
    private RecyclerView contactList;
    private Contact selectedContact;
    private FloatingActionButton fab;
    private List<Contact> contacts;
    private View contactListFragmentView;


    public ContactListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contactListFragmentView = inflater.inflate(R.layout.fragment_contact_list, container, false);
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

        contactList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), contactList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
                        selectedContact = adapter.getItem(position);
                        Intent goToContactInfo = new Intent(getActivity(), ContactInformationActivity.class);
                        goToContactInfo.putExtra(ContactInformationActivity.PARAM_CONTACTINFO, selectedContact);
                        startActivityForResult(goToContactInfo, ContactListFragment.RESULTCONTACTINFO);
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
        if (getUserVisibleHint()) {
            int id = item.getItemId();

            if (id == R.id.context_menu_edit)
                MenuHelper.onMenuEditClick(selectedContact, this.getActivity());

            else if (id == R.id.context_menu_delete)
                MenuHelper.onMenuDeleteClick(selectedContact, this.getActivity());

            else if (id == R.id.context_menu_website)
                MenuHelper.onMenuWebSiteClick(selectedContact, this.getActivity());

            else if (id == R.id.context_menu_call)
                MenuHelper.onMenuCallClick(selectedContact, this.getActivity());
            else if (id == R.id.context_menu_favorite) {
                MenuHelper.onMenuFavoriteClick(selectedContact, this.getActivity());
            }
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void refreshList(List<Contact> contacts) {
        this.contacts = contacts;
        setAdapter();
    }


    private void setAdapter() {
        contactList.setAdapter(new ContactListAdapter(getActivity(), this.contacts));
//
//            @Override
//            public void onImageLongClick(Contact contact) {
//                selectedContact = contact;
//            }
//
//            @Override
//            public void onInfoLongClick(Contact contact) {
//                selectedContact = contact;
//            }
//
//            @Override
//            public void onImageClick(Contact contact) {
//                selectedContact = contact;
//                Intent goToContactPhotoActivity = new Intent(getActivity(), ContactPhotoActivity.class);
//                goToContactPhotoActivity.putExtra(ContactPhotoActivity.PARAM_CONTACT, selectedContact);
//                startActivityForResult(goToContactPhotoActivity, CODE_FROM_PHOTO_ACTIVITY);
//            }
//
//            @Override
//            public void onInfoClick(Contact contact) {
//                selectedContact = contact;
//                Intent goToContactInfo = new Intent(getActivity(), ContactInformationActivity.class);
//                goToContactInfo.putExtra(ContactInformationActivity.PARAM_CONTACTINFO, selectedContact);
//                startActivityForResult(goToContactInfo, ContactListFragment.RESULTCONTACTINFO);
//            }
//        });
//        ContactListAdapter adapter = (ContactListAdapter) contactList.getAdapter();
//        adapter.notifyDataSetChanged();
//    }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ContactListFragment.RESULTCONTACTINFO) {
            if (resultCode == getActivity().RESULT_OK) {
                UpdatableViewPager activityWithViewPager = (UpdatableViewPager) getActivity();
                activityWithViewPager.updateViewPager();
            }
        }

        if (requestCode == CODE_FROM_PHOTO_ACTIVITY) {
            if (resultCode == getActivity().RESULT_OK) {
                UpdatableViewPager activityWithViewPager = (UpdatableViewPager) getActivity();
                activityWithViewPager.updateViewPager();
            }
        }
    }
}

