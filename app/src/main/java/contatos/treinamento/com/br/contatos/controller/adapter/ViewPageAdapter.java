package contatos.treinamento.com.br.contatos.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.activity.ContactFavoriteListFragment;
import contatos.treinamento.com.br.contatos.controller.activity.ContactListFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;
    FragmentManager fm;

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
        } else
        {
            ContactFavoriteListFragment tab2 = new ContactFavoriteListFragment();
            return tab2;
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
}