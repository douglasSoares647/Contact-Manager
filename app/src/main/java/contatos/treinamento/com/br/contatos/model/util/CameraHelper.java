package contatos.treinamento.com.br.contatos.model.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

import contatos.treinamento.com.br.contatos.controller.activity.ContactPhotoActivity;

/**
 * Created by c1284521 on 29/10/2015.
 */
public final class CameraHelper  {

    public static final int CAMERA_RESULT_OK = 2;
    public static final int GALLERY_RESULT_OK = 3;

    public static String takePhotoWithCamera(Activity context) {
        Intent goToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/contactsApp/");
        folder.mkdir();

        String fileName;
        fileName = System.currentTimeMillis() + ".png";
        File photo = new File(folder,fileName);
        Uri uri = Uri.fromFile(photo);

        goToCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        context.startActivityForResult(goToCamera, CAMERA_RESULT_OK);
        String filePath = folder.toString()+"/"+fileName;
        return filePath;
    }

    public static void choosePhotoFromGallery(Activity context) {
        Intent goToGallery = new Intent(Intent.ACTION_GET_CONTENT);
        goToGallery.setType("image/*");
        context.startActivityForResult(goToGallery, GALLERY_RESULT_OK);
    }
}
