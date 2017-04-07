package com.bogueratcreations.eaftoolkit.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogueratcreations.eaftoolkit.R;

import java.util.ArrayList;

public class GalleryDetail extends AppCompatActivity {

    private ArrayList<ImageModel> data = new ArrayList<>();

    // An array of full-sized images to display
    private static Integer fullIMGS[] = {
            R.drawable.eaf1, R.drawable.eaf2,
            R.drawable.eaf3, R.drawable.eaf4,
            R.drawable.eaf5, R.drawable.eaf6,
            R.drawable.eaf7, R.drawable.eaf8,
            R.drawable.eaf9, R.drawable.eaf10,
            R.drawable.eaf11, R.drawable.eaf12,
            R.drawable.eaf13, R.drawable.eaf14,
            R.drawable.eaf15, R.drawable.eaf16,
            R.drawable.eaf17, R.drawable.eaf18,
            R.drawable.eaf19, R.drawable.eaf20,
            R.drawable.eaf21, R.drawable.eaf22,
            R.drawable.eaf23, R.drawable.eaf24,
            R.drawable.eaf25, R.drawable.eaf26,
            R.drawable.eaf27, R.drawable.eaf28,
            R.drawable.eaf29, R.drawable.eaf30,
            R.drawable.eaf31, R.drawable.eaf32,
            R.drawable.eaf33
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
        setContentView(R.layout.activity_gallery_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load up thumbnail links and image captions
        for (int i = 0; i < fullIMGS.length; i++) {

            ImageModel imageModel = new ImageModel();
            imageModel.setName(CAPTIONS[i]);
            imageModel.setUrl(fullIMGS[i]);
            data.add(imageModel);
        }
        //data = getIntent().getParcelableArrayListExtra("data");
        int pos = getIntent().getIntExtra("pos", 0);

        setTitle(data.get(pos).getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), data);
        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(pos);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //noinspection ConstantConditions
                setTitle(data.get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public ArrayList<ImageModel> data = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm, ArrayList<ImageModel> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("EAF Toolkit", "Fragment getItem called");
            return PlaceholderFragment.newInstance(position, data.get(position).getName(), data.get(position).getUrl());
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //Log.d("EAF Toolkit", "data.size: " + data.size());
            return data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return data.get(position).getName();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        String name; //, url;
        int pos, url;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMG_TITLE = "image_title";
        private static final String ARG_IMG_URL = "image_url";

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getInt(ARG_IMG_URL);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String name, int url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            // Using IMGS array to insert the full size image for use in the fragment
            args.putInt(ARG_IMG_URL, url); // IMGS[sectionNumber]);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
            //removed final from the following
            final TouchImageView touchImageView = (TouchImageView) rootView.findViewById(R.id.detail_image);
            // Using this to load the image because glide was not showing the first image to the right.
            touchImageView.setImageResource(url);
            // Original:  Glide.with(getActivity()).load(url).thumbnail(0.1f).into(touchImageView);
//            Glide.with(getActivity()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    touchImageView.setImageBitmap(resource);
//                }
//            });

            return rootView;
        }

    }

}
