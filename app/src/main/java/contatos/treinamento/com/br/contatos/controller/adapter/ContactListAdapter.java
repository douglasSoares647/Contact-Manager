package contatos.treinamento.com.br.contatos.controller.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.activity.ContactInformationActivity;
import contatos.treinamento.com.br.contatos.controller.activity.ContactListActivity;
import contatos.treinamento.com.br.contatos.controller.listener.RecyclerItemClickListener;
import contatos.treinamento.com.br.contatos.model.entity.Contact;
import contatos.treinamento.com.br.contatos.model.util.BitmapHelper;

/**
 * Created by c1284521 on 14/10/2015.
 */
public class ContactListAdapter extends
        RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> contacts;
    private Context context;

    public ContactListAdapter( Activity context, List<Contact> contacts) {
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
    public void onBindViewHolder(ContactListAdapter.ViewHolder viewHolder, int i) {
        final Contact contact = contacts.get(i);


        viewHolder.itemView.setLongClickable(true);
        TextView textViewName = viewHolder.textViewName;
        TextView textViewTelephone = viewHolder.textViewTelephone;
        TextView textViewEmail = viewHolder.textViewEmail;
        RatingBar ratingBarItemList = viewHolder.ratingBar;
        ImageView imageViewItemList = viewHolder.photo;

        imageViewItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context, R.style.CustomDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.contact_image);
                ImageView imageBigger = (ImageView) dialog.findViewById(R.id.contact_image_bigger);
                TextView textViewContactNameOnDialog = (TextView) dialog.findViewById(R.id.textViewNameOnDialog);
                textViewContactNameOnDialog.setText(contact.getName());
                if (contact.getPhoto() != null)
                    BitmapHelper.loadImageBigger(imageBigger, contact.getPhoto());
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

            }
        });

        if (contact.getPhoto() != null) {
            BitmapHelper.loadImage(imageViewItemList,contact.getPhoto());
            imageViewItemList.setBackground(null);
        }

        textViewName.append(contact.getName());

        textViewTelephone.setText(contact.getTelephone());
        textViewEmail.setText(contact.getEmail());
        ratingBarItemList.setRating(contact.getRating());
        ratingBarItemList.setIsIndicator(true);

    }

    public Contact getItem(int position){
        return contacts.get(position);
    }
    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewTelephone;
        public ImageView photo;
        public TextView textViewEmail;
        public RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewTelephone = (TextView) itemView.findViewById(R.id.textViewTelephone);
            textViewEmail = (TextView) itemView.findViewById(R.id.textViewEmail);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarItemList);
            photo = (ImageView) itemView.findViewById(R.id.imageViewContactItemList);

        }
    }


}
