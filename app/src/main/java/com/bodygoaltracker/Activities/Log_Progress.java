package com.bodygoaltracker.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.bodygoaltracker.Adapters.SectionsPageAdapter;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.bodygoaltracker.fragments.Bodyweight_frag;
import com.bodygoaltracker.fragments.Pictures_frag;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;


public class Log_Progress extends AppCompatActivity {
    private static final String TAG = "Log_progress";
    private SectionsPageAdapter adapter;
    private ViewPager mViewPager;
    public static int page = 0;
    long id;
    TabLayout tabLayout;

    //constants:
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private void setupViewPage(ViewPager viewPager) {
        adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Bodyweight_frag(), "Bodyweight");
        adapter.addFragment(new Pictures_frag(), "Pictures");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(page);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_log__progress);

        DB.init(getApplicationContext());
        initialiseViews();

        Intent intent = getIntent();
        if (intent.getIntExtra("Frag_Tab_Number", -1) != -1) {
            int tabNumber = intent.getIntExtra("Frag_Tab_Number", -1);
            TabLayout.Tab tab = tabLayout.getTabAt(tabNumber);
            tab.select();
        }

    }

    public void initialiseViews() {

        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Records");
        ab.setDisplayHomeAsUpEnabled(true);

        //Fragment adapter:
        adapter = new SectionsPageAdapter(getSupportFragmentManager());


        //View pager:
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPage(mViewPager);

        //Tab layout:
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void verifyPermissions(String[] permissions) {

        ActivityCompat.requestPermissions(
                Log_Progress.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean checkPermissions(String permission) {

        int permissionRequest = ActivityCompat.checkSelfPermission(Log_Progress.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        id = info.id;

        if (v.getId() == R.id.listview || v.getId() == R.id.gridview) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.longclickmenu, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        long index = info.position;
        if (adapter.getItem(0).getUserVisibleHint()) {
            switch (item.getItemId()) {
                case R.id.delete:
                    deleteRowItemBodyweight(id);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        if (adapter.getItem(1).getUserVisibleHint()) {
            switch (item.getItemId()) {
                case R.id.delete:
                    deleteRowItemPictures((Long) info.targetView.getTag());
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;

    }

    public void deleteRowItemBodyweight(long id) {

        String table = DB.MySQLOpenLiteHelper.BODYWEIGHT_TABLE;
        String where = DB.DBContract.KEY_ID + "=?";
        String[] whereArgs = new String[]{id + ""};

        DB.db.delete(table, where, whereArgs);

        Bodyweight_frag frag = (Bodyweight_frag) adapter.getItem(0);
        frag.addItemsToList();
    }

    public void deleteRowItemPictures(long id) {

        String table = DB.MySQLOpenLiteHelper.IMAGES_TABLE;
        String where = DB.DBContract.KEY_ID + " =?";
        String[] whereArgs = new String[]{id + ""};

        DB.db.delete(table, where, whereArgs);

        Pictures_frag frag = (Pictures_frag) adapter.getItem(1);
        frag.displayImage();
    }


}
