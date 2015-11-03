package contatos.treinamento.com.br.contatos.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.util.Date;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.asynctask.AsyncSave;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
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
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_photo);
        if(savedInstanceState==null)
        initContact();
        else
        contact = savedInstanceState.getParcelable(ContactPhotoActivity.PARAM_CONTACT);

        bindActionBar();
        bindPhoto();
    }

    private void initContact() {
        Bundle values = getIntent().getExtras();

        if (values != null || contact == null) {
            contact = values.getParcelable(PARAM_CONTACT);
        } else
            contact = new Contact();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_photo, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_photo_take_photo:
                onMenuChooseOptionClick();
                break;

            case android.R.id.home:
                AsyncSave save = new AsyncSave();
                save.execute(contact);
                Intent sendInfo = new Intent(this, ContactInformationActivity.class );
                sendInfo.putExtra(ContactPhotoActivity.PARAM_CONTACT, contact);
                setResult(RESULT_OK, sendInfo);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onMenuChooseOptionClick() {
        PopupMenu popupMenu = new PopupMenu(this,findViewById(R.id.menu_photo_take_photo));
        popupMenu.inflate(R.menu.menu_choose_or_take_photo);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.menu_choose_from_gallery:
                        onMenuChooseFromGalleryClick();
                        break;
                    case R.id.menu_take_photo:
                        onMenuTakePhotoClick();
                }
                return true;
            }
        });


    }

    private void onMenuTakePhotoClick() {
        pathPhoto = CameraHelper.takePhotoWithCamera(ContactPhotoActivity.this);
    }



    private void onMenuChooseFromGalleryClick() {
        CameraHelper.choosePhotoFromGallery(ContactPhotoActivity.this);
    }


    private void bindPhoto() {
        photo = (ImageView) findViewById(R.id.imageViewContactPhoto);
        BitmapHelper.loadFullImage(this, photo, contact.getPhoto());

    }

    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbar_contact_photo);
        actionBar.setTitle(getString(R.string.lbl_contact_photo));
        actionBar.setBackgroundColor(getResources().getColor(R.color.toolbar_contact_photo_color));
        setStatusBarColor();
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setStatusBarColor() {
        //CHANGE STATUS BAR COLOR
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.toolbar_contact_photo_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraHelper.CAMERA_RESULT_OK) {
            if (resultCode == RESULT_OK) {
                BitmapHelper.deleteRecursive(new File(contact.getPhoto()));
                contact.setPhoto(pathPhoto);
                contact.setLastDateModified(new Date());
                BitmapHelper.loadFullImage(this,photo,contact.getPhoto());
            }
            else
                pathPhoto = null;
        }

        if(requestCode == CameraHelper.GALLERY_RESULT_OK){
            if(resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                contact.setPhoto(getpathring(selectedImageUri));
                BitmapHelper.loadFullImage(this,photo,contact.getPhoto());

            }
        }

    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(ContactPhotoActivity.PARAM_CONTACT, contact);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            contact = savedInstanceState.getParcelable(ContactPhotoActivity.PARAM_CONTACT);
        }
    }

    @Override
    public void onBackPressed() {
        AsyncSave save = new AsyncSave();
        save.execute(contact);
        Intent sendInfo = new Intent(this, ContactInformationActivity.class );
        sendInfo.putExtra(ContactPhotoActivity.PARAM_CONTACT, contact);
        setResult(RESULT_OK, sendInfo);

        super.onBackPressed();
    }




    private String getpathring(Uri selectedImageUri) {
        Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String pathFromUri = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return pathFromUri;
    }
}
