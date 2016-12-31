package net.couchdev.android.layoutsandbox.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RelativeLayout contentMain;

    private enum Tab{
        SHOP,
        PROFILE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateItemActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Database.getInstance().updateLastLoggedInUser();

        // navigation view
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.usernameText);
        username.setText("Username");
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profileImage);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserDataActivity.class);
                startActivity(intent);
            }
        });

        contentMain = (RelativeLayout) findViewById(R.id.content_main);
        selectTab(Tab.SHOP);

        navigationView.setCheckedItem(R.id.nav_tab_shop);
    }

    private void selectTab(Tab tab){
        switch (tab){
            case SHOP: contentMain.removeAllViews();
                contentMain.addView(LayoutInflater.from(this).inflate(R.layout.fragment_shop, null));
                createShopFragment();
                break;
            case PROFILE: contentMain.removeAllViews();
                contentMain.addView(LayoutInflater.from(this).inflate(R.layout.fragment_profile, null));
                createProfileFragment();
                break;
        }
    }

    private void createShopFragment(){
        ListView listView = (ListView) findViewById(R.id.salesList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_sale, R.id.itemTitle,
                new String[]{"Shop Item 1", "Shop Item 2", "Shop Item 3", "Shop Item 4", "Shop Item 5"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SaleItemActivity.class);
                startActivity(intent);
            }
        });

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.shopRecycler);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager3);
        RecyclerView.Adapter adapter3 = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_slider, parent, false);
                TextView owner = (TextView) view.findViewById(R.id.itemOwner);
                owner.setVisibility(View.VISIBLE);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler.setAdapter(adapter3);
        final TextView shopHeader = (TextView) findViewById(R.id.shopHeader);
        shopHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recycler.getVisibility() == View.INVISIBLE){
                    recycler.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shopHeader.getLayoutParams();
                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    shopHeader.setLayoutParams(params);
                    ImageView arrow = (ImageView) findViewById(R.id.arrowImage);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_small));
                } else{
                    recycler.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shopHeader.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    shopHeader.setLayoutParams(params);
                    ImageView arrow = (ImageView) findViewById(R.id.arrowImage);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up_small));
                }
            }
        });
        shopHeader.callOnClick();

        final SearchView shopSearch = (SearchView) findViewById(R.id.shopSearch);
        shopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopSearch.setIconified(false);
            }
        });
    }

    public void createProfileFragment() {
        RecyclerView recycler = (RecyclerView) findViewById(R.id.myItemsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_slider, parent, false);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
                // viewHolder.field.setText(mItems.get(i));
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler.setAdapter(adapter);

        RecyclerView recycler2 = (RecyclerView) findViewById(R.id.favoRecycler);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler2.setLayoutManager(layoutManager2);
        RecyclerView.Adapter adapter2 = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_slider, parent, false);
                TextView owner = (TextView) view.findViewById(R.id.itemOwner);
                owner.setVisibility(View.VISIBLE);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler2.setAdapter(adapter2);

        ProgressBar buyerProgress = (ProgressBar) findViewById(R.id.buyerProgress);
        buyerProgress.setMax(100);
        buyerProgress.setProgress(60);
        ProgressBar sellerProgress = (ProgressBar) findViewById(R.id.sellerProgress);
        sellerProgress.setMax(100);
        sellerProgress.setProgress(40);
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch(item.getItemId()){
            case R.id.nav_tab_shop:
                selectTab(Tab.SHOP);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_tab_profile:
                selectTab(Tab.PROFILE);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_new_item:
                Intent intent = new Intent(MainActivity.this, CreateItemActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_recently_viewed:
                intent = new Intent(MainActivity.this, RecentlyViewedActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_favos:
                intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_my_items:
                intent = new Intent(MainActivity.this, MyItemsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_messages:
                intent = new Intent(MainActivity.this, MessagesActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}
