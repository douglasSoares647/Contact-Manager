package contatos.treinamento.com.br.contatos.controller.adapter;

import android.app.Activity;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.model.entity.NavigationItem;

/**
 * Created by c1284521 on 23/10/2015.
 */
public class NavigationAdapter extends BaseAdapter{

    private Activity context;
    private List<NavigationItem> navigationItems;
    public NavigationAdapter(Activity context,List<NavigationItem> navigationItems){
        this.context = context;
        this.navigationItems = navigationItems;
    }
    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public NavigationItem getItem(int i) {
        return navigationItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        NavigationItem navigationItem = getItem(i);
        View navigationView = context.getLayoutInflater().inflate(R.layout.navigation_list_item,null);

        TextView textViewNavigationItem = (TextView) navigationView.findViewById(R.id.textViewNavigationItem);
        ImageView imageViewNavigationItem = (ImageView) navigationView.findViewById(R.id.imageViewNavigationItem);

        textViewNavigationItem.setText(navigationItem.getName());
        imageViewNavigationItem.setImageResource(navigationItem.getIcon());
        imageViewNavigationItem.setColorFilter(context.getResources().getColor(R.color.colorPrimary));

        return navigationView;
    }
}
