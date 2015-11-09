package contatos.treinamento.com.br.contatos.controller.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;
import contatos.treinamento.com.br.contatos.model.util.FormHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public abstract class ContactListAdapter extends
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Contact contact = contacts.get(i);
        viewHolder.itemView.setSelected(contacts.contains(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(i);
            }
        });


        viewHolder.itemView.setLongClickable(true);
        TextView textViewName = viewHolder.textViewName;
        final ImageView imageViewItemList = viewHolder.photo;
        TextView textViewLastModified = viewHolder.textViewLastModified;


        if(contact.getPhoto()!=null)
            BitmapHelper.loadImage(context,imageViewItemList,contact.getPhoto());
        else
            BitmapHelper.loadImage(context,imageViewItemList,contact.getContactColor());

            textViewName.setText(contact.getName());


            Date currentDate = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                currentDate = simpleDateFormat.parse(FormHelper.convertDateToString(new Date()));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            long diffDate = ((contact.getLastDateModified().getTime() - currentDate.getTime()) / 1000 / 60 / 60 / 24) * -1;
            if (diffDate == 0) {
                textViewLastModified.setText(context.getString(R.string.lbl_today));
            } else if (diffDate > 0 && diffDate <= 1) {
                textViewLastModified.setText(context.getString(R.string.lbl_yesterday));
            } else {
                textViewLastModified.setText(FormHelper.convertDateToString(contact.getLastDateModified()));
            }


            viewHolder.relativeInfo.setClickable(true);
            viewHolder.relativeInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onInfoClick(contact);

                }
            });


        viewHolder.layoutImage.setClickable(true);
        viewHolder.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClick(contact);
            }
        });


        viewHolder.layoutImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onImageLongClick(contact);
                return true;
            }
        });


        viewHolder.relativeInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onInfoLongClick(contact);
                return true;
            }
        });
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
        public TextView textViewLastModified;
        public RelativeLayout relativeInfo;
        public LinearLayout layoutImage;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            photo = (ImageView) itemView.findViewById(R.id.imageViewContactItemList);
            textViewLastModified = (TextView) itemView.findViewById(R.id.textViewLastModified);

            relativeInfo = (RelativeLayout) itemView.findViewById(R.id.relativeInfo);
            layoutImage = (LinearLayout) itemView.findViewById(R.id.layoutImage);
        }
    }

    public abstract void onImageLongClick(Contact contact);
    public abstract void onInfoLongClick(Contact contact);
    public abstract void onImageClick(Contact contact);
    public abstract void onInfoClick(Contact contact);


}
