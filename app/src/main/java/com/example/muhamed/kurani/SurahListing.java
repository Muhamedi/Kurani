package com.example.muhamed.kurani;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SurahListing extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecycleViewSurahListingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_listing);

        buildRecyclerView();
    }

    public void buildRecyclerView()
    {
        ArrayList<SurasItem> suras = Helper.ReadSurasListXmlData(this);

        mRecyclerView = findViewById(R.id.rv_suras);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecycleViewSurahListingAdapter(suras);

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecycleViewSurahListingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String surahNumber = Integer.toString(position);
                Intent intent = new Intent(SurahListing.this,AyahListing.class);
                intent.putExtra("surahNumber",surahNumber);
                startActivity(intent);
            }
        });
    }
}
