package contatos.treinamento.com.br.contatos.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import contatos.treinamento.com.br.contatos.R;
import contatos.treinamento.com.br.contatos.controller.adapter.ViewPageAdapter;
import contatos.treinamento.com.br.contatos.controller.interfaces.UpdatableViewPager;
import contatos.treinamento.com.br.contatos.view.slidingtab.SlidingTabLayout;


public class MainView extends AppCompatActivity implements UpdatableViewPager{

    private Toolbar actionBar;

    private ViewPageAdapter adapter;
    private ViewPager viewPager;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        bindViewPageAdapter();
        bindActionBar();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    protected void onResume() {
        updateViewPager();
        super.onResume();
    }

    private void bindViewPageAdapter() {
        CharSequence[] charSequences = {"Contact List","Favorites","Recent"};
        adapter = new ViewPageAdapter(getSupportFragmentManager(),charSequences, 3);

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


    private void bindActionBar() {
        actionBar = (Toolbar) findViewById(R.id.toolbarMainView);
        actionBar.setTitle("Contacts");
        setSupportActionBar(actionBar);
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

        return super.onOptionsItemSelected(item);
    }

    private void onMenuSearchClick() {
       Intent goToSearchForm = new Intent(MainView.this, ContactSearchActivity.class);
        startActivity(goToSearchForm);
    }

    @Override
    public void updateViewPager() {
        adapter.notifyDataSetChanged();
    }


}
