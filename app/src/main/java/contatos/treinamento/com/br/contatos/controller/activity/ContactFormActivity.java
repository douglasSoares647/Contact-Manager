package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncSave;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
import contatos.treinamento.com.br.contatos.model.util.CameraHelper;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class ContactFormActivity extends AppCompatActivity {
    private ImageView photo;
    private EditText editTextName;
    private EditText editTextWebSite;
    private EditText editTextEmail;
    private EditText editTextBirth;
    private ImageButton btnBirth;
    private ImageButton btnImportContact;
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
        bindBirth();
        bindBtnImportContact();

        bindRatingBar();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

    }

    private void bindBtnImportContact() {
        btnImportContact = (ImageButton) findViewById(R.id.imageButtonImportContact);
        btnImportContact.setColorFilter(getResources().getColor(R.color.colorPrimary));
        btnImportContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToPhoneContacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                goToPhoneContacts.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(goToPhoneContacts, 1);
            }
        });
    }

    private void bindBirth() {
        editTextBirth = (EditText) findViewById(R.id.editTextBirth);
        editTextBirth.setText(contact.getBirth() == null ? "" : FormHelper.convertDateToString(contact.getBirth()));
        editTextBirth.setKeyListener(null);
        editTextBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                FormHelper.hideKeyboard(ContactFormActivity.this, editTextBirth);
                showDatePickerDialog();
            }
        });
        editTextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormHelper.hideKeyboard(ContactFormActivity.this, editTextBirth);
                showDatePickerDialog();
            }
        });
        editTextBirth.setLongClickable(false);
        btnBirth = (ImageButton) findViewById(R.id.btnBirth);
        btnBirth .setColorFilter(getResources().getColor(R.color.colorPrimary));
    }




    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar_form);
        actionBar.setTitle(getString(R.string.bar_new_contact));

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initContact() {
        Bundle values = getIntent().getExtras();

        if (values != null) {
            ContactListFragment fragment = new ContactListFragment();
            //ESCONDE O FRAGMENT DEPOIS DA TRANSIÇÃO
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).hide(fragment).commit();
            this.contact =  values.getParcelable(PARAM_CONTACT);
            getSupportActionBar().setTitle(getString(R.string.app_edit_contact));
        } else
            contact = new Contact();
    }

    private void bindContact() {
        contact.setName(editTextName.getText().toString());
        contact.setWebSite(editTextWebSite.getText().toString());
        contact.setBirth(FormHelper.convertStringToDate(editTextBirth.getText().toString()));
        contact.setRating(ratingBar.getRating());
        contact.setTelephone(editTextTelephone.getText().toString());
        contact.setEmail(editTextEmail.getText().toString());
        contact.setLastDateModified(new Date());
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
        editTextWebSite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    FormHelper.showKeyboard(ContactFormActivity.this,editTextWebSite);
                }
            }
        });

        editTextTelephone = (EditText) findViewById(R.id.editTextTelephone);
        editTextTelephone.setText(contact.getTelephone() == null ? "" : contact.getTelephone());

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setText(contact.getEmail() == null ? "" : contact.getEmail());
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                    FormHelper.hideKeyboard(ContactFormActivity.this, editTextEmail);
            }
        });

    }

    private void bindPhoto() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        photo = (ImageView) findViewById(R.id.imageViewContact);
        if(contact.getPhoto()==null){
            contact.setPhoto(String.valueOf(color));
            photo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_person));
            photo.setColorFilter(color);
        }
        if (contact.getPhoto() != null)
            BitmapHelper.loadImage(this, photo, contact.getPhoto());

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = CameraHelper.takePhotoWithCamera(ContactFormActivity.this);
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraHelper.CAMERA_RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                BitmapHelper.deleteRecursive(new File(contact.getPhoto()));
                contact.setPhoto(path);
                BitmapHelper.loadImage(this,photo,path);

            } else {
                path = null;
            }
        } else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri contactData = data.getData();

                    String[] columns = {ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Email.DATA,
                            ContactsContract.CommonDataKinds.Website.DATA,
                            ContactsContract.CommonDataKinds.Photo.PHOTO};


                    Cursor cursor = getContentResolver().query(contactData, columns, null, null, null);

                    if (cursor.moveToNext()) {
                        editTextName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME)));
                        editTextTelephone.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        // contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_THUMBNAIL_URI)));
                        // Glide.with(this).load(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))).centerCrop().fitCenter().into(photo);
                    }
                    cursor.close();

                } catch (Exception e) {
                    Toast.makeText(this, "Contact was not imported!", Toast.LENGTH_LONG).show();
                }
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
                break;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onAcceptMenuClick() {
        bindContact();
        if (!FormHelper.validateForm(editTextName, editTextTelephone, editTextEmail) &&
                !FormHelper.validateEmail(editTextEmail,contact.getId()) && !FormHelper.validateName(editTextName,contact.getId())) {
            AsyncSave asyncSave = new AsyncSave();
            asyncSave.execute(contact);

            Intent sendContactInfoToPreviousActivity = new Intent(this,ContactInformationActivity.class);
            sendContactInfoToPreviousActivity.putExtra(ContactInformationActivity.PARAM_CONTACTINFO,contact);
            setResult(RESULT_OK,sendContactInfoToPreviousActivity);
            finish();
        }

    }



    private void showDatePickerDialog() {
        if (editTextBirth.isFocused()) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);


            DatePickerDialog datePicker = new DatePickerDialog(ContactFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    editTextBirth.setText(String.valueOf(dayOfMonth) + "/" +
                            (monthOfYear + 1 < 10 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1)) +
                            "/" + String.valueOf(year));
                    editTextWebSite.requestFocus();
                }
            }, year, month + 1, day);
            datePicker.setCanceledOnTouchOutside(true);
            datePicker.getDatePicker().setMaxDate(new Date().getTime());
            datePicker.show();
        }
    }
}

