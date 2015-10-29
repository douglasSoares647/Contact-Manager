package contatos.treinamento.com.br.contatos.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncSave;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.CameraHelper;

/**
 * Created by c1284521 on 29/10/2015.
 */
public class ContactPhotoActivity extends AppCompatActivity {

    public static final String PARAM_CONTACT = "Contact";
    private Toolbar actionBar;
    private ImageView photo;


    private Contact contact;
    private String pathPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_photo);
        initContact();
        bindActionBar();
        bindPhoto();
    }

    private void initContact() {
        Bundle values = getIntent().getExtras();

        if(values!=null){
            contact = values.getParcelable(PARAM_CONTACT);
        }
        else
            contact = new Contact();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_information, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_info_take_photo:
                onMenuTakePhotoClick();
                break;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onMenuTakePhotoClick() {
        pathPhoto = CameraHelper.takePhotoWithCamera(ContactPhotoActivity.this);
    }


    private void bindPhoto() {
        photo = (ImageView) findViewById(R.id.imageViewContactPhoto);
        if(contact.getPhoto()!=null)
            loadImage(contact.getPhoto() );

    }

    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar_contact_photo);
        actionBar.setTitle(getString(R.string.lbl_contact_photo));
        actionBar.setBackgroundColor(getResources().getColor(R.color.toolbar_contact_photo_color));

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CameraHelper.CAMERA_RESULT_OK){
            if (resultCode == RESULT_OK){
                contact.setPhoto(pathPhoto);
                loadImage(contact.getPhoto());
                AsyncSave save = new AsyncSave(this);
                save.execute(contact);

                Intent sendContactInfoToPreviousActivity = new Intent(this,ContactInformationActivity.class);
                sendContactInfoToPreviousActivity.putExtra(ContactInformationActivity.PARAM_CONTACTINFO,contact);
                setResult(RESULT_OK, sendContactInfoToPreviousActivity);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    private void loadImage(String image) {
        Glide.with(this).load(image).fitCenter().centerCrop().into(photo);
            }
}
