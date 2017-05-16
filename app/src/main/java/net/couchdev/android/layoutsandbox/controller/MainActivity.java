package net.couchdev.android.layoutsandbox.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import net.couchdev.android.layoutsandbox.R;
import net.couchdev.android.layoutsandbox.model.Database;
import net.couchdev.android.layoutsandbox.model.ServerMock;
import net.couchdev.android.layoutsandbox.model.ShopItemAdapter;
import net.couchdev.android.layoutsandbox.model.ShopItemSerializable;
import net.couchdev.android.layoutsandbox.model.Userdata;
import net.couchdev.android.layoutsandbox.tools.FileTools;
import net.couchdev.android.layoutsandbox.tools.Tools;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String STATE_USER_DATA = "userDataBoolean";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQ_SIGN_OUT = 1;

    public static final String EXTRA_ITEM = "shop item extra";
    public static final String EXTRA_IMAGE = "image extra";

    private RelativeLayout contentMain;
    private boolean headerViewClicked;
    private boolean isMainLayout;
    private boolean signingOut;

    private enum Tab{
        SHOP,
        PROFILE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        boolean shouldLogin = true;
        String[] lastLogin = Database.getInstance().getLastLogin();
        if(lastLogin != null){
            String[] user = ServerMock.getInstance().login(lastLogin[0], lastLogin[1]);
            if(user.length == 2){
                Database.setLoggedInUser(lastLogin[0], lastLogin[1], lastLogin[2]);
                if(Database.getInstance().isComplete()){
                    setMainLayout(savedInstanceState);
                    shouldLogin = false;
                } else {
                    Log.e(LOG_TAG, "Last logged in user had incomplete Data!");
                    Database.getInstance().clearLogin();
                }
            } else{
                Log.e(LOG_TAG, "Login failed: Could not login to server!");
            }
        }
        if(shouldLogin){
            setLoginLayout(savedInstanceState);
        }
    }

    private void setMainLayout(Bundle savedInstanceState){
        isMainLayout = true;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        contentMain = (RelativeLayout) findViewById(R.id.content_main);
        selectTab(Tab.SHOP);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_tab_shop);
        View headerView = navigationView.getHeaderView(0);
        if(savedInstanceState != null && savedInstanceState.getBoolean(STATE_USER_DATA)){
            headerView.callOnClick();
        }
    }

    private void setLoginLayout(final Bundle savedInstanceState){
        isMainLayout = false;
        setContentView(R.layout.activity_login);
        final EditText userEmail = (EditText) findViewById(R.id.emailEdit);
        final EditText password = (EditText) findViewById(R.id.passwordEdit);
        ImageButton viewPass = (ImageButton) findViewById(R.id.viewPassButton);
        viewPass.setOnTouchListener(new View.OnTouchListener() {
            private int inputType;
            private int selection;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!password.getText().toString().isEmpty()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            inputType = password.getInputType();
                            selection = password.getSelectionStart();
                            password.setInputType(inputType | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            password.setSelection(selection);
                            break;
                        case MotionEvent.ACTION_UP:
                            password.setInputType(inputType);
                            password.setSelection(selection);
                            break;
                    }
                }
                return false;
            }
        });
        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] user = ServerMock.getInstance().login(userEmail.getText().toString(),
                        "" + password.getText().toString().hashCode());
                if(user.length == 2 ){
                    Database.setLoggedInUser(user[0], user[1], "" + password.getText().toString().hashCode());
                    if(Database.getInstance().isComplete()){
                        Database.getInstance().updateLastLoggedInUser();
                        setMainLayout(savedInstanceState);
                        onResume();
                    } else {
                        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if(ServerMock.INVALID_LOGIN.equals(user[0])){
                    Toast.makeText(MainActivity.this, "Username or Email could not be found", Toast.LENGTH_SHORT).show();
                } else if(ServerMock.INVALID_PASS.equals(user[0])){
                    Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        Button infoButton = (Button) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Password Restrictions");
                builder.setMessage("Your password must contain:\n\tA lower case letter\n\tAn upper" +
                        " case letter\n\tA digit\n\tA special character\n\tAt least 8 characters");
                builder.create().show();
            }
        });
    }

    private void setUserData(){
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Database database = Database.getInstance();
        navigationView.setNavigationItemSelectedListener(this);
        final View headerView = navigationView.getHeaderView(0);
        Userdata userdata = database.getUserdata();
        TextView username = (TextView) headerView.findViewById(R.id.usernameText);
        username.setText(userdata.getUsername());
        TextView fullName = (TextView) headerView.findViewById(R.id.fullNameText);
        fullName.setText(userdata.getFirstName() + " " + userdata.getLastName());
        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profileImage);
        Bitmap profilePic = FileTools.getProfilePic();
        if(profilePic != null){
            profileImage.setImageBitmap(profilePic);
        } else{
            profileImage.setImageResource(R.drawable.ic_profile_white);
            profileImage.getDrawable().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
        }
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerViewClicked = true;
                Intent intent = new Intent(MainActivity.this, UserDataActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        headerViewClicked = false;
        HashMap<String, Integer> msgs = FileTools.getUnreadMessages();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem item = navigationView.getMenu().findItem(R.id.nav_messages);
        String title = (String) item.getTitle();
        title = title.substring(0, (title.indexOf('(') == -1 ? title.length() : title.indexOf('('))).trim();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        if(!msgs.isEmpty()) {
            int count = 0;
            for(String key: msgs.keySet()){
                count += msgs.get(key);
            }
            if(count > 0){
                title += "    (" + count + ")";
                BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_menu_white_24dp);
                Bitmap src = d.getBitmap();
                Bitmap b = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
                Canvas c = new Canvas(b);
                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setStrokeWidth(1);
                p.setStyle(Paint.Style.FILL_AND_STROKE);
                p.setColor(getResources().getColor(R.color.colorPrimary));
                c.drawBitmap(src, 0, 0, p);
                c.drawCircle(b.getWidth()-15, b.getHeight()-15, 25, p);
                p.setColor(getResources().getColor(R.color.white));
                c.drawCircle(b.getWidth()-15, b.getHeight()-15, 15, p);
                BitmapDrawable dn = new BitmapDrawable(b);
                dn.setBounds(d.getBounds());
                getSupportActionBar().setHomeAsUpIndicator(dn);
            }
        }
        item.setTitle(title);

        if(isMainLayout){
            setUserData();
            ListView listView = (ListView) findViewById(R.id.salesList);
            if(listView != null) {
                ShopItemAdapter adapter = new ShopItemAdapter(MainActivity.this, R.layout.item_sale,
                        ServerMock.getInstance().getShopItems());
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_USER_DATA, headerViewClicked);
    }

    private void init(){
        signingOut = false;
        Tools.init(getApplicationContext());
        FileTools.init(getApplicationContext());
        Database.init(getApplicationContext());
        ServerMock.init(getApplicationContext());
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
        final ShopItemAdapter adapter = new ShopItemAdapter(MainActivity.this, R.layout.item_sale,
                ServerMock.getInstance().getShopItems());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SaleItemActivity.class);
                ShopItemSerializable sis = ShopItemSerializable.fromShopItem(adapter.getItem(position));
                FileTools.createTmpFile(adapter.getItem(position).getImage());
                intent.putExtra(EXTRA_ITEM, sis);
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

        Userdata userdata = Database.getInstance().getUserdata();
        TextView username = (TextView) findViewById(R.id.usernameText);
        username.setText(userdata.getUsername());
        TextView fullName = (TextView) findViewById(R.id.fullNameText);
        fullName.setText(userdata.getFirstName() + " " + userdata.getLastName());
        ImageView profileImage = (ImageView) findViewById(R.id.profileImage);
        Bitmap profilePic = FileTools.getProfilePic();
        if(profilePic != null){
            profileImage.setImageBitmap(profilePic);
        } else{
            profileImage.setImageResource(R.drawable.ic_profile_white);
            profileImage.getDrawable().setColorFilter(getResources().getColor(R.color.light_gray), PorterDuff.Mode.MULTIPLY);
        }

        ProgressBar buyerProgress = (ProgressBar) findViewById(R.id.buyerProgress);
        buyerProgress.setMax(100);
        buyerProgress.setProgress(60);
        ProgressBar sellerProgress = (ProgressBar) findViewById(R.id.sellerProgress);
        sellerProgress.setMax(100);
        sellerProgress.setProgress(40);

        View userDataView = findViewById(R.id.userDataView);
        userDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserDataActivity.class);
                startActivity(intent);
            }
        });
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
            //super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isMainLayout && !signingOut) {
            ServerMock.destroy();
            Database.destroy();
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
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQ_SIGN_OUT);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_SIGN_OUT){
            if(resultCode == RESULT_OK){
                if(data.getBooleanExtra(SettingsActivity.SIGN_OUT, false)){
                    Database.getInstance().clearLogin();
                    signingOut = true;
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
