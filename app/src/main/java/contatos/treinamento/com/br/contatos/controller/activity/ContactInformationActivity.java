package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 19/10/2015.
 */
public class ContactInformationActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private TextView textViewTelephone;
    private TextView textViewWebSite;
    private Toolbar actionBar;

    private ImageView iconInformation;

    private Contact contact;

    public static String PARAM_CONTACTINFO = "ContactInfo";

    public ContactInformationActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_iformation);
        initContact();
        bindImageIcon();
        bindTextViewTelephone();
        bindTextViewEmail();
        bindTextViewWebSite();
        bindActionBar();
    }

    private void bindImageIcon() {
        iconInformation = (ImageView) findViewById(R.id.iconInformation);
        iconInformation.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }

    private void initContact() {
        Bundle values = getIntent().getExtras();

        if (contact == null) {
            contact = new Contact();
            contact = values.getParcelable(PARAM_CONTACTINFO);
        } else contact = new Contact();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_information, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_info_edit) {
            onMenuEditClick();
        } else if (id == R.id.menu_info_delete) {
            onMenuDeleteClick();
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onMenuDeleteClick() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_delete))
                .setNeutralButton(getString(R.string.dialog_no), null)
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContactBusinessService.delete(contact);
                        contact = null;
                        finish();
                    }
                }).setTitle(getString(R.string.dialog_confirm)).create().show();

    }

    private void onMenuEditClick() {
        Intent goToContactForm = new Intent(this, ContactFormActivity.class);
        goToContactForm.putExtra(ContactFormActivity.PARAM_CONTACT, contact);
        startActivityForResult(goToContactForm, 1);
    }

    private void bindActionBar() {
        View view = findViewById(R.id.actionBarContactInfo);
        ImageView photo = (ImageView) view.findViewById(R.id.imageViewContactToolbar);

        actionBar = (Toolbar) view.findViewById(R.id.actionBarWithImage);
        actionBar.setTitle("");
        TextView title = (TextView) actionBar.findViewById(R.id.toolbar_title);
        title.setText(contact.getName());
        if (contact.getPhoto() != null) {
            Glide.with(this).load(contact.getPhoto()).fitCenter().centerCrop().into(photo);
        } else {
            photo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void bindTextViewWebSite() {
        textViewWebSite = (TextView) findViewById(R.id.textViewContactInfoWebSite);
        textViewWebSite.setText(contact.getWebSite() == null ? "" : contact.getWebSite());
    }

    private void bindTextViewTelephone() {
        textViewTelephone = (TextView) findViewById(R.id.textViewContactInfoTelephone);

        textViewTelephone.setText(contact.getTelephone() == null ? "" : contact.getTelephone().replace("|", " "));
    }


    private void bindTextViewEmail() {
        textViewEmail = (TextView) findViewById(R.id.textViewContactInfoEmail);
        textViewEmail.setText(contact.getEmail() == null ? "" : contact.getEmail());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                contact = data.getParcelableExtra(ContactInformationActivity.PARAM_CONTACTINFO);
            }
        }
    }

    @Override
    protected void onResume() {
        refreshContact();
        super.onResume();
    }

    private void refreshContact() {
        bindImageIcon();
        bindTextViewTelephone();
        bindTextViewEmail();
        bindTextViewWebSite();
        bindActionBar();
    }
}
