package contatos.treinamento.com.br.contatos.controller.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.text.Normalizer;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncSave;
import contatos.treinamento.com.br.contatos.controller.interfaces.UpdatableViewPager;
import contatos.treinamento.com.br.contatos.model.ContactBusinessService;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
import contatos.treinamento.com.br.contatos.model.util.CameraHelper;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

/**
 * Created by c1284521 on 19/10/2015.
 */
public class ContactInformationActivity extends AppCompatActivity {

    public static final int CODE_FROM_PHOTO_ACTIVITY = 3;
    private TextView textViewEmail;
    private TextView textViewTelephone;
    private TextView textViewWebSite;
    private Toolbar actionBar;
    private RatingBar ratingBar;
    private TextView textViewBirth;
    private ImageView photo;

    private Menu menu;
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
        refreshContact();

        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }


    private void bindRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(contact.getRating() == null ? 0 : contact.getRating());
    }

    private void bindTextViewBirth() {
        textViewBirth = (TextView) findViewById(R.id.textViewContactInfoBirth);
        if (contact.getBirth() != null)
            textViewBirth.setText(FormHelper.convertDateToString(contact.getBirth()));
        else
            textViewBirth.setText("");

    }

    private void bindImageIcon() {
        iconInformation = (ImageView) findViewById(R.id.iconInformation);
        iconInformation.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }

    private void initContact() {
        Bundle values = getIntent().getExtras();

        if (values != null) {
            contact = values.getParcelable(PARAM_CONTACTINFO);
        } else contact = new Contact();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_information, menu);
        this.menu = menu;
        MenuItem menuItemAddFavorite = this.menu.findItem(R.id.menu_info_add_favorite);
        if(contact.isFavorite()){
            menuItemAddFavorite.setIcon(R.mipmap.ic_filled_star);
            menuItemAddFavorite.setChecked(true);
        }

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
            AsyncSave save = new AsyncSave();
            save.execute(contact);
            setResult(RESULT_OK);
            finish();
            return true;
        } else if (id == R.id.menu_info_add_favorite) {
            item.setChecked(!item.isChecked());
            contact.setIsFavorite(item.isChecked());
            item.setIcon(item.isChecked()? R.mipmap.ic_filled_star : R.mipmap.ic_empty_star);
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
        photo = (ImageView) view.findViewById(R.id.imageViewContactToolbar);

        actionBar = (Toolbar) view.findViewById(R.id.actionBarWithImage);
        actionBar.setTitle("");
        TextView title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText(contact.getName());
        BitmapHelper.loadFullImage(this, photo, contact.getPhoto());

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToContactPhotoActivity = new Intent(ContactInformationActivity.this, ContactPhotoActivity.class);
                goToContactPhotoActivity.putExtra(ContactPhotoActivity.PARAM_CONTACT, contact);
                startActivityForResult(goToContactPhotoActivity, CODE_FROM_PHOTO_ACTIVITY);
            }
        });

    }


    private void bindTextViewWebSite() {
        textViewWebSite = (TextView) findViewById(R.id.textViewContactInfoWebSite);
        textViewWebSite.setText(contact.getWebSite() == null ? "" : contact.getWebSite());
    }

    private void bindTextViewTelephone() {
        textViewTelephone = (TextView) findViewById(R.id.textViewContactInfoTelephone);
        textViewTelephone.setText(contact.getTelephone() == null ? "" : contact.getTelephone());
    }


    private void bindTextViewEmail() {
        textViewEmail = (TextView) findViewById(R.id.textViewContactInfoEmail);
        textViewEmail.setText(contact.getEmail() == null ? "" : contact.getEmail());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                contact = data.getParcelableExtra(ContactInformationActivity.PARAM_CONTACTINFO);
            }
        }

        if (requestCode == CODE_FROM_PHOTO_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                contact = data.getParcelableExtra(ContactPhotoActivity.PARAM_CONTACT);
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
        bindRatingBar();
        bindTextViewBirth();
    }

    @Override
    public void onBackPressed() {
        AsyncSave save = new AsyncSave();
        save.execute(contact);
        super.onBackPressed();
    }
}
