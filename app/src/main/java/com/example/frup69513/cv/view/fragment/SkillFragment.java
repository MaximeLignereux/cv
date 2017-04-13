package com.example.frup69513.cv.view.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Skill;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SkillFragment extends Fragment {

    private final static String TAG = "SkillFragment";

    private DatabaseReference mDatabase;

    private PieChart mChart;
    private Typeface tf;

    private List<PieEntry> mPieEntries;

    public static Fragment newInstance() {
        return new SkillFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mPieEntries = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_skill, container, false);

        mChart = (PieChart) v.findViewById(R.id.pieChart);
        mChart.getDescription().setEnabled(false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);
        mChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        generatePieData();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase.child("competence").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("skill", dataSnapshot.toString());
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);
                    Log.e("skill", skill.toString());
                    mPieEntries.add(new PieEntry(skill.getValue(), skill.getTitle()));
                }

                generatePieData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Compétence\nDéveloppement");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    /**
     * generates less data (1 DataSet, 4 values)
     * @return
     */
    protected void generatePieData() {
        Log.e(TAG, "generatePieData()");

        PieDataSet ds1 = new PieDataSet(mPieEntries, "Compétence Développement");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(ds1);
        d.setValueTypeface(tf);

        mChart.setData(d);
    }
}
