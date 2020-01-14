package com.example.muhamed.kurani;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AyahListing extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecycleViewAyahListingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Integer previousAvtivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayah_listing);
        ActionBar actionBar = getSupportActionBar();

        Intent intent = getIntent();

        if (intent.hasExtra("surahAyahArray")) {
            String[] surahAyahArray = intent.getStringArrayExtra("surahAyahArray");
            int surahNumber = Integer.parseInt(surahAyahArray[0]);
            int ayahNumber = Integer.parseInt(surahAyahArray[1]);
            actionBar.setTitle(surahNumber+"."+Helper.ReadSurahTitle(AyahListing.this,surahNumber));
            buildRecyclerView(surahNumber,ayahNumber);
            previousAvtivity = 1;
        } else if (intent.hasExtra("surahNumber")){
            int surahNumber = Integer.parseInt(intent.getStringExtra("surahNumber").toString());
            actionBar.setTitle(surahNumber+"."+Helper.ReadSurahTitle(AyahListing.this,surahNumber));
            buildRecyclerView(surahNumber,-1);
            previousAvtivity = 2;
        } else if (intent.hasExtra("savedAyas")) {
            actionBar.setTitle("Ruajtjet");
            buildRecyclerViewForSavedAyas();
            previousAvtivity = 3;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (previousAvtivity != 3) {
            menu.findItem(R.id.pdf).setVisible(false);
            menu.findItem(R.id.clear_Savings).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ayah_saving_actionbar_items,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.clear_Savings:
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Kjo do të shlyej të gjitha ajetet e ruajtura.\nDëshironi të vazhdoni?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Helper.deleteFile(AyahListing.this,Constants.filenameForAyahSavings);
                                AyahListing.this.recreate();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            }
            case R.id.pdf:
            {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File saveFile = new File(dir,Constants.filenameForPdfSavedAyas);
                if (saveFile.exists())
                {
                    saveFile.delete();
                }
                //File dir = getFilesDir();
                PdfHelper ph = new PdfHelper(dir ,AyahListing.this);
                mRecyclerView = findViewById(R.id.rv_ayas);

                final LayoutInflater factory = getLayoutInflater();

                final View ayahView = factory.inflate(R.layout.activity_ayah_listing, null);
                ph.saveImageToPDF(ayahView,Helper.getRecyclerViewScreenshot(mRecyclerView),Constants.filenameForPdfSavedAyas.replace(".pdf",""));
                Helper.openFile(this,saveFile);
                //Toast.makeText(AyahListing.this,"Pdf file është ruajtur me sukses dhe mund ta gjeni te DOCUMENTS.",Toast.LENGTH_LONG).show();
                break;
            }
        }
        return false;
    }

    public void buildRecyclerView(int surahNumber, int ayahNumber)
    {

        final ArrayList<AyahItem> ayas = Helper.ReadAyasListXmlData(surahNumber,this);

        mRecyclerView = findViewById(R.id.rv_ayas);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecycleViewAyahListingAdapter(this, ayas, getSupportFragmentManager(), surahNumber);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (ayahNumber != -1) {
            mLayoutManager.scrollToPosition(ayahNumber-1);
        }

        mRecyclerView.setAdapter(mAdapter);

        registerForContextMenu(mRecyclerView);

        mAdapter.notifyDataSetChanged();
    }
    public void buildRecyclerViewForSavedAyas()
    {
        final ArrayList<AyahItem> savedAyas = new ArrayList<AyahItem>();
        if (Helper.fileExists(Constants.filenameForAyahSavings,AyahListing.this)) {
            ArrayList<SurahAyahItem> saiItems = Helper.readSavingsAyas(Constants.filenameForAyahSavings, AyahListing.this);
            for (SurahAyahItem sai : saiItems) {
                 AyahItem ai = Helper.ReadSpecificAyah(AyahListing.this,Integer.parseInt(sai.getSurahNumber()),Integer.parseInt(sai.getAyahNumber()));
                 savedAyas.add(ai);
            }

            mRecyclerView = findViewById(R.id.rv_ayas);
            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new RecycleViewAyahListingAdapter(this, savedAyas, getSupportFragmentManager(),-1);
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.setAdapter(mAdapter);

            registerForContextMenu(mRecyclerView);
        }
    }
}
