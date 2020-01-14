package com.example.muhamed.kurani;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CardView readQuranCard,
                     ayahBookmark,
                     ayahSavings;

    private AutoCompleteTextView searchQuran;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // My code
        /*autoCompleteSearchQuran = findViewById(R.id.autoCompleteSearchQuran);
        autoCompleteSearchQuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            }
        });
        autoCompleteSearchQuran.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
                overridePendingTransition(R.anim.lefttoright, R.anim.righttoleft);
            }
        });*/

        readQuranCard =  findViewById(R.id.readQuran);
        readQuranCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQuranCard.setCardBackgroundColor(getResources().getColor(R.color.cardviewClickedColor));

                Intent readQuranIntent = new Intent(MainActivity.this,SurahListing.class);
                startActivity(readQuranIntent);
            }
        });

        ayahBookmark =  findViewById(R.id.ayahBookmark);
        ayahBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ayahBookmark.setCardBackgroundColor(getResources().getColor(R.color.cardviewClickedColor));
                //File dir = getFilesDir();
                //File file = new File(dir, Constants.filenameForBookMark);
                //file.delete();
                Intent ayahBookmarkIntent = new Intent(MainActivity.this,AyahListing.class);
                if (Helper.fileExists(Constants.filenameForBookMark,MainActivity.this))
                {
                    String surahAyah = Helper.ReadXmlFromInternalStorage(Constants.filenameForBookMark,MainActivity.this);
                    String[] surahAyahArray = surahAyah.split(",");
                    ayahBookmarkIntent.putExtra("surahAyahArray", surahAyahArray);
                    startActivity(ayahBookmarkIntent);
                }
                else
                {
                    Helper.showInformationDialog("Ju nuk e keni ruajtur ajetin e mbetur! \nJu lutem ruani ajetin e mbetur dhe këtu do të mund të vazhdoni leximin aty ku keni mbetur.",MainActivity.this);
                }
            }
        });

         ayahSavings = findViewById(R.id.ayasSavings);
         ayahSavings.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //ayahSavings.setCardBackgroundColor(getResources().getColor(R.color.cardviewClickedColor));
                 Intent ayahSavingsIntent = new Intent(MainActivity.this,AyahListing.class);
                 if (Helper.fileExists(Constants.filenameForAyahSavings,MainActivity.this)) {
                     ayahSavingsIntent.putExtra("savedAyas", 1);
                     startActivity(ayahSavingsIntent);
                 }
                 else{
                     Helper.showInformationDialog("Ju nuk keni ajete të ruajtura !\nJu lutem ruani ajetet e preferuara të cilat mund t'i rishikoni këtu.",MainActivity.this);
                 }
             }
         });

        // --



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =   findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
