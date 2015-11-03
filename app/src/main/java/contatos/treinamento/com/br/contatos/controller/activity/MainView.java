package contatos.treinamento.com.br.contatos.controller.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.NavigationAdapter;
import contatos.treinamento.com.br.contatos.controller.adapter.ViewPageAdapter;
import contatos.treinamento.com.br.contatos.controller.interfaces.UpdatableViewPager;
import contatos.treinamento.com.br.contatos.model.entity.NavigationItem;
import contatos.treinamento.com.br.contatos.view.slidingtab.SlidingTabLayout;


public class MainView extends AppCompatActivity implements UpdatableViewPager {

    private Toolbar actionBar;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private SlidingTabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        bindActionBar();
        bindDrawerList();
        bindDrawerLayout();
        bindViewPageAdapter();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);

    }

    @Override
    protected void onResume() {
        updateViewPager();
        super.onResume();
    }

    private void bindViewPageAdapter() {
        CharSequence[] charSequences = {"Contact List","Favorites"};
        adapter = new ViewPageAdapter(getSupportFragmentManager(),charSequences, 2);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.slidingTabBar);
            }
        });
        tabs.setViewPager(viewPager);
    }


    private void bindDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        setDrawerToggle();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private void setDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);


    }

    private void bindDrawerList() {
        drawerList = (ListView) findViewById(R.id.drawerList);
        List<NavigationItem> navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationItem(R.mipmap.ic_navigation_drawer, getString(R.string.navigation_birthdays)));
        navigationItems.add(new NavigationItem(R.mipmap.ic_navigation_drawer, "Teste"));

        drawerList.setAdapter(new NavigationAdapter(this, navigationItems));
    }


    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbarMainView);
        actionBar.setTitle("Contacts");
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setNavigationIcon(R.mipmap.ic_navigation_drawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_search) {
            onMenuSearchClick();
        }
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onMenuSearchClick() {
       Intent goToSearchForm = new Intent(MainView.this, ContactSearchActivity.class);
        startActivity(goToSearchForm);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void updateViewPager() {
        adapter.notifyDataSetChanged();
    }
}
