package contatos.treinamento.com.br.contatos.controller.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.entity.Contact;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class ContactListAdapter extends
        RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> contacts;
    private Context context;

    public ContactListAdapter(Activity context, List<Contact> contacts) {
        this.contacts = contacts;
        this.context = context;
    }


    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.contact_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.ViewHolder viewHolder, int i) {
        Contact contact = contacts.get(i);

        viewHolder.itemView.setLongClickable(true);
        TextView textViewName = viewHolder.textViewName;
        RatingBar ratingBarItemList = viewHolder.ratingBar;
        final ImageView imageViewItemList = viewHolder.photo;


        if (contact.getPhoto() != null) {
            Glide.with(context).load(contact.getPhoto()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageViewItemList) {
                protected void setResource(Bitmap resource) {
                    //USANDO ROUNDEDBITMAPDRAWABLE A IMAGEM SERÁ MOSTRADA APENAS EM UM FORMATO CIRCULAR, PORÉM O IMAGEVIEW CONTINUA QUADRADO
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageViewItemList.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            imageViewItemList.setImageBitmap(null);
        }

//        imageViewItemList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog dialog = new Dialog(context, R.style.CustomDialog);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.contact_image);
//                ImageView imageBigger = (ImageView) dialog.findViewById(R.id.contact_image_bigger);
//                TextView textViewContactNameOnDialog = (TextView) dialog.findViewById(R.id.textViewNameOnDialog);
//                textViewContactNameOnDialog.setText(contact.getName());
//                if (contact.getPhoto() != null)
//                    BitmapHelper.loadImageBigger(imageBigger, contact.getPhoto());
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//
//            }
//        });


        textViewName.setText(contact.getName());
        ratingBarItemList.setRating(contact.getRating());
        ratingBarItemList.setIsIndicator(true);

    }

    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView photo;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarItemList);
            photo = (ImageView) itemView.findViewById(R.id.imageViewContactItemList);

        }
    }


}
