package contatos.treinamento.com.br.contatos.model.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;

import contatos.treinamento.com.br.contatos.R;

/**
 * Created by c1284521 on 16/10/2015.
 */
public final class BitmapHelper {


    public static void loadImage(final Context context, final ImageView photo, final String path) {
        if (new File(path).exists()) {
            Glide.with(context).load(path).asBitmap().centerCrop().into(new BitmapImageViewTarget(photo) {
                protected void setResource(Bitmap resource) {
                    //USANDO ROUNDEDBITMAPDRAWABLE A IMAGEM SERÁ MOSTRADA APENAS EM UM FORMATO CIRCULAR, PORÉM O IMAGEVIEW CONTINUA QUADRADO
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    photo.setImageDrawable(circularBitmapDrawable);

                }
            });
        } else {
            Glide.with(context).load(R.drawable.ic_person).asBitmap().centerCrop().into(new BitmapImageViewTarget(photo) {
                protected void setResource(Bitmap resource) {
                    //USANDO ROUNDEDBITMAPDRAWABLE A IMAGEM SERÁ MOSTRADA APENAS EM UM FORMATO CIRCULAR, PORÉM O IMAGEVIEW CONTINUA QUADRADO
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setColorFilter(Integer.parseInt(path), PorterDuff.Mode.OVERLAY);
                    photo.setImageDrawable(circularBitmapDrawable);



                }
            });
        }
    }

    public static void loadFullImage(final Context context, final ImageView photo, final String path){
        if (new File(path).exists()) {
            Glide.with(context).load(path).fitCenter().centerCrop().into(photo);
            photo.setColorFilter(null);
        } else {
            photo.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_person));
            photo.setBackgroundColor(Integer.parseInt(path));

        }
    }



    public static void deleteRecursive(File path)
    {
        if (path.isDirectory())
        {
            for (File child : path.listFiles())
            {
                deleteRecursive(child);
            }
        }

        path.delete();
    }
}
