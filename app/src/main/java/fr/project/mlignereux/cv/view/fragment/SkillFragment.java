package fr.project.mlignereux.cv.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.project.mlignereux.cv.CustomExpandableListAdapter;
import fr.project.mlignereux.cv.model.Skill;


public class SkillFragment extends Fragment {

    private final static String TAG = "SkillFragment";

    ExpandableListView mExpandableListView;
    ExpandableListAdapter mExpandableListAdapter;
    List<String> mExpandableListTitle;
    HashMap<String, List<Skill>> mExpandableListDetail;

    public SkillFragment(){}

    public static Fragment newInstance(String reference, String title) {

        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putString("REFERENCE", reference);
        fragment.setArguments(args);

        return fragment;
    }

    public String getReference(){
        return getArguments().getString("REFERENCE");
    }

    public String getTitle(){
        return getArguments().getString("TITLE");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(fr.project.mlignereux.cv.R.layout.fragment_skill_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mExpandableListView = (ExpandableListView) v.findViewById(fr.project.mlignereux.cv.R.id.expanded_list);
        mExpandableListTitle = new ArrayList<>();
        mExpandableListDetail = new HashMap<>();


        FirebaseDatabase.getInstance().getReference().child(getReference()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                List<Skill> skills = new ArrayList<>();
                mExpandableListTitle.add(dataSnapshot.getKey());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);
                    skills.add(skill);
                }

                mExpandableListDetail.put(dataSnapshot.getKey(), skills);

                mExpandableListAdapter = new CustomExpandableListAdapter(getContext().getApplicationContext(), mExpandableListTitle, mExpandableListDetail);
                mExpandableListView.setAdapter(mExpandableListAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                List<Skill> skills = new ArrayList<>();

                mExpandableListTitle.add(dataSnapshot.getKey());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Skill skill = d.getValue(Skill.class);
                    skills.add(skill);
                }

                mExpandableListDetail.put(dataSnapshot.getKey(), skills);

                mExpandableListAdapter = new CustomExpandableListAdapter(getContext().getApplicationContext(), mExpandableListTitle, mExpandableListDetail);
                mExpandableListView.setAdapter(mExpandableListAdapter);
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
