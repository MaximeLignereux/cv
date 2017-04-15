package com.example.frup69513.cv.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.frup69513.cv.CustomExpandableListAdapter;
import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Skill;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SkillFragment extends Fragment {

    private final static String TAG = "SkillFragment";

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Skill>> expandableListDetail;

    public SkillFragment(){}

    public static Fragment newInstance(String title) {
        Log.d(TAG, "newInstance()");

        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        fragment.setArguments(args);

        return fragment;
    }

    public String getTitle(){
        Log.d(TAG, "getTitle()");
        return getArguments().getString("TITLE");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        View v = inflater.inflate(R.layout.fragment_skill_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        expandableListView = (ExpandableListView) v.findViewById(R.id.expanded_list);
        expandableListTitle = new ArrayList<>();
        expandableListDetail = new HashMap<>();


        FirebaseDatabase.getInstance().getReference().child("competence").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onCreateView():onChildAdded()");

                List<Skill> skills = new ArrayList<Skill>();
                Log.d(TAG, "GROUP: " + dataSnapshot.getKey());
                expandableListTitle.add(dataSnapshot.getKey());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);

                    Log.d(TAG, "Skill: " + skill.toString());
                    skills.add(skill);
                }

                expandableListDetail.put(dataSnapshot.getKey(), skills);

                expandableListAdapter = new CustomExpandableListAdapter(getContext().getApplicationContext(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onCreateView():onChildChanged()");

                List<Skill> skills = new ArrayList<Skill>();

                Log.d(TAG, "GROUP: " + dataSnapshot.getKey());
                expandableListTitle.add(dataSnapshot.getKey());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);

                    Log.d(TAG, "Skill: " + skill.toString());
                    skills.add(skill);
                }

                expandableListDetail.put(dataSnapshot.getKey(), skills);

                expandableListAdapter = new CustomExpandableListAdapter(getContext().getApplicationContext(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }



}
