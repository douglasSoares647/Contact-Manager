package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class ContactFormActivity extends AppCompatActivity {
    private ImageView photo;
    private EditText editTextName;
    private EditText editTextBirth;
    private EditText editTextWebSite;
    private EditText editTextEmail;
    private EditText editTextDDD;

    private EditText editTextTelephone;
    private RatingBar ratingBar;
    private Toolbar actionBar;


    private Contact contact;
    private String path;

    public static final String PARAM_CONTACT = "Contact";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_contact);
        bindActionBar();
        initContact();
        bindPhoto();
        bindEditTexts();
        bindEditTextBirth();
        bindRatingBar();

    }

    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar_form);
        actionBar.setTitle(getString(R.string.bar_new_contact));

        setSupportActionBar(actionBar);
    }

    private void bindEditTextBirth() {
        editTextBirth = (EditText) findViewById(R.id.editTextBirth);
        String birth = FormHelper.convertDateToString(contact.getBirth());
        editTextBirth.setText(birth == null ? "" : birth);


        editTextBirth.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if ((editTextBirth.getText().length() == 2 || editTextBirth.getText().length() == 5)) {
                    editTextBirth.append("/");
                    editTextBirth.setSelection(editTextBirth.getText().length());
                }

            }
        });

    }

    private void initContact() {
        Bundle values = getIntent().getExtras();

        if (values != null) {
            contact = new Contact();
            this.contact = values.getParcelable(PARAM_CONTACT);
        } else
            contact = new Contact();
    }

    private void bindContact() {
        contact.setName(editTextName.getText().toString());
        contact.setWebSite(editTextWebSite.getText().toString());
        contact.setBirth(FormHelper.convertStringToDate(editTextBirth.getText().toString()));
        contact.setRating(ratingBar.getRating());
        contact.setTelephone(editTextDDD.getText().toString() + "-" + editTextTelephone.getText().toString());
        contact.setEmail(editTextEmail.getText().toString());

    }


    private void bindRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(contact.getRating() == null ? 0 : contact.getRating());
    }

    private void bindEditTexts() {
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setText(contact.getName() == null ? "" : contact.getName());

        editTextWebSite = (EditText) findViewById(R.id.editTextWebSite);
        editTextWebSite.setText(contact.getWebSite() == null ? "" : contact.getWebSite());

        editTextDDD = (EditText) findViewById(R.id.editTextDDD);

        editTextTelephone = (EditText) findViewById(R.id.editTextTelephone);
        if (contact.getTelephone() != null) {
            String[] telephone = contact.getTelephone().split("-");

            editTextDDD.setText(telephone[0]);

            editTextTelephone.setText(telephone[1]);
        } else {
            editTextDDD.setText("");
            editTextTelephone.setText("");
        }

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setText(contact.getEmail() == null ? "" : contact.getEmail());

    }

    private void bindPhoto() {
        photo = (ImageView) findViewById(R.id.imageViewContact);
        if (contact.getPhoto() != null)
            BitmapHelper.loadImage(photo, contact.getPhoto());

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                path = Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".png";
                File photo = new File(path);
                Uri uri = Uri.fromFile(photo);

                goToCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(goToCamera, 10);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                contact.setPhoto(path);
                BitmapHelper.loadImage(photo, path);
            } else {
                path = null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();

        switch (idItem) {
            case R.id.item_accept:
                onAcceptMenuClick();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onAcceptMenuClick() {
        bindContact();
        if (!FormHelper.validateForm(editTextName, editTextDDD, editTextTelephone, editTextEmail)
                && !FormHelper.validateEmail(editTextEmail)) {
            ContactBusinessService.save(contact);
            finish();
        }
    }
}
