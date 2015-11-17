package contatos.treinamento.com.br.contatos.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.activity.fragments.ContactFavoriteListFragment;
import contatos.treinamento.com.br.contatos.controller.activity.fragments.ContactListFragment;
import contatos.treinamento.com.br.contatos.controller.activity.fragments.RecentlyContactActivity;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[];
    private int NumbOfTabs;
    private FragmentManager fm;
    private static int[] ICONS = new int[] {
            R.drawable.ic_action_list,
            R.drawable.ic_toggle_star,
    };

    public ViewPageAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.fm = fm;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0)
        {
            ContactListFragment tab1 = new ContactListFragment();
            return tab1;
        } else if(position ==1)
        {
            ContactFavoriteListFragment tab2 = new ContactFavoriteListFragment();
            return tab2;
        }
        else {
            RecentlyContactActivity tab3 = new RecentlyContactActivity();
            return tab3;
        }


    }


    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }


    public int getDrawableId(int position){
        return ICONS[position];
    }
}