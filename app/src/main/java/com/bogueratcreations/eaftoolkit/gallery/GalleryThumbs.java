package com.bogueratcreations.eaftoolkit.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bogueratcreations.eaftoolkit.R;
import com.bogueratcreations.eaftoolkit.RecyclerItemClickListener;

import java.util.ArrayList;

public class GalleryThumbs extends AppCompatActivity {

    private ArrayList<ImageModel> data = new ArrayList<>();

    private static Integer IMGS[] = {
            R.drawable.eaf_thumb1, R.drawable.eaf_thumb2,
            R.drawable.eaf_thumb3, R.drawable.eaf_thumb4,
            R.drawable.eaf_thumb5, R.drawable.eaf_thumb6,
            R.drawable.eaf_thumb7, R.drawable.eaf_thumb8,
            R.drawable.eaf_thumb9, R.drawable.eaf_thumb10,
            R.drawable.eaf_thumb11, R.drawable.eaf_thumb12,
            R.drawable.eaf_thumb13, R.drawable.eaf_thumb14,
            R.drawable.eaf_thumb15, R.drawable.eaf_thumb16,
            R.drawable.eaf_thumb17, R.drawable.eaf_thumb18,
            R.drawable.eaf_thumb19, R.drawable.eaf_thumb20,
            R.drawable.eaf_thumb21, R.drawable.eaf_thumb22,
            R.drawable.eaf_thumb23, R.drawable.eaf_thumb24,
            R.drawable.eaf_thumb25, R.drawable.eaf_thumb26,
            R.drawable.eaf_thumb27, R.drawable.eaf_thumb28,
            R.drawable.eaf_thumb29, R.drawable.eaf_thumb30,
            R.drawable.eaf_thumb31, R.drawable.eaf_thumb32,
            R.drawable.eaf_thumb33
    };
    private static String CAPTIONS[] = {
            "Matting Project @ Bogue",
            "AM-2 Matting Storage",
            "VTOL 96x96 - Iraq",
            "MobiMat Installation",
            "MobiMat 3x7",
            "VTOL 96x96 - Iraq",
            "Matting Project",
            "Matting Embark",
            "Dust Abatement - COP",
            "Matting Evolution",
            "HPRU - Al Asad",
            "Another HPRU",
            "Revetments - Kandahar",
            "Tie-Downs",
            "MOSLS - oooooh",
            "M-31 MCEAGS Installation",
            "Arrested Landing with LSO",
            "Cable Boots after runover",
            "M-21 LEA Installation",
            "M-21 Retrieve Engine",
            "M-21 Absorber Assy",
            "M-21 Arrestment @ An Numinyah",
            "FLOLS for delivery",
            "EAF Complex - Al Asad",
            "California Shelter",
            "Dust Abatement Trailer",
            "OLS Tower - MCALF Bogue",
            "AV-8B Landing at OLS",
            "Alaska Shelter",
            "M-31 Stake installation",
            "MOSLS CabKit testing",
            "DCP - CBR evaluation",
            "Windsock"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_thumbs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load up thumbnail links and image captions
        for (int i = 0; i < IMGS.length; i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName(CAPTIONS[i]);
            imageModel.setUrl(IMGS[i]);
            data.add(imageModel);

        }

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);


        GalleryAdapter mAdapter = new GalleryAdapter(GalleryThumbs.this, data);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(GalleryThumbs.this, GalleryDetail.class);
                        intent.putParcelableArrayListExtra("data", data);
                        intent.putExtra("pos", position);
                        startActivity(intent);

                    }
                }));

    }

}
