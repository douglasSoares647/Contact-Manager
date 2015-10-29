package contatos.treinamento.com.br.contatos.model.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by c1284521 on 29/10/2015.
 */
public final class CameraHelper  {

    public static final int CAMERA_RESULT_OK = 2;
    public static String takePhotoWithCamera(Activity context) {
        Intent goToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String path;
        path = Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".png";
        File photo = new File(path);
        Uri uri = Uri.fromFile(photo);

        goToCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        context.startActivityForResult(goToCamera, CAMERA_RESULT_OK);
        return path;
    }
}
