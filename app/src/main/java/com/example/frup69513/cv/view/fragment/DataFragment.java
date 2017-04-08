package com.example.frup69513.cv.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.frup69513.cv.R;
import com.example.frup69513.cv.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Data, DataViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLinearLayoutManager;

    private Context mContext;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String reference){
        Log.d(TAG,"newInstance():reference: " + reference);


        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString("REFERENCE", reference);
        fragment.setArguments(args);

        return fragment;
    }

    public String getReference(){
        return getArguments().getString("REFERENCE");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);

        mContext = view.getContext();

        mRecycler = (RecyclerView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        mLinearLayoutManager = new LinearLayoutManager(mContext);

        Query dataQuery = mDatabase.child(getReference()).orderByKey();
        dataQuery.keepSynced(true);

        mAdapter = new FirebaseRecyclerAdapter<Data, DataViewHolder>(
                Data.class,
                R.layout.fragment_data,
                DataViewHolder.class,
                dataQuery ) {

            @Override
            protected void populateViewHolder(DataViewHolder viewHolder, Data model, int position) {
                Log.d(TAG, "onActivityCreated():populateViewHolder()");

                viewHolder.title.setText(model.getTitle());
                viewHolder.date.setText(model.getDate());
                viewHolder.subtitle.setText(model.getSubtitle());
                viewHolder.description.setText(model.getDescription());

                Picasso.with(mContext).load(model.getImageUrl()).into(viewHolder.circleImageView);
            }
        };

        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView subtitle;
        private TextView description;
        private CircleImageView circleImageView;

        public DataViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.civ_image);
        }
    }
}
