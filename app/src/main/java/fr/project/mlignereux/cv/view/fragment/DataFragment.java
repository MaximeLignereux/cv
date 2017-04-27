package fr.project.mlignereux.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import fr.project.mlignereux.cv.R;
import fr.project.mlignereux.cv.model.Data;


public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";

    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Data, DataViewHolder> mAdapter;
    private RecyclerView mRecycler;

    private Context mContext;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String reference, String title, String subtitle, String description){
        Log.d(TAG,"newInstance():reference: " + reference);

        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(String.valueOf(R.string.reference_bundle), reference);
        args.putString(String.valueOf(R.string.title_bundle), title);
        args.putString(String.valueOf(R.string.subtitle_bundle), subtitle);
        args.putString(String.valueOf(R.string.description_bundle), description);
        fragment.setArguments(args);

        return fragment;
    }

    public String getReference(){
        return getArguments().getString(getString(R.string.reference_bundle));
    }

    public String getTitle(){
        return getArguments().getString(getString(R.string.title_bundle));
    }

    public String getSubtitle() { return getArguments().getString(getString(R.string.subtitle_bundle));}

    public String getDescription() { return getArguments().getString(getString(R.string.description_bundle)); }


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
        View view = inflater.inflate(fr.project.mlignereux.cv.R.layout.fragment_data_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mContext = view.getContext();

        mRecycler = (RecyclerView) view.findViewById(fr.project.mlignereux.cv.R.id.list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);

        Query dataQuery = mDatabase.child(getReference()).orderByKey();
        dataQuery.keepSynced(true);

        mAdapter = new FirebaseRecyclerAdapter<Data, DataViewHolder>(
                Data.class,
                fr.project.mlignereux.cv.R.layout.fragment_data,
                DataViewHolder.class,
                dataQuery ) {

            @Override
            protected void populateViewHolder(DataViewHolder viewHolder, Data model, int position) {
                Log.d(TAG, "onActivityCreated():populateViewHolder()");

                viewHolder.title.setText(model.getTitle());
                viewHolder.date.setText(model.getDate());
                viewHolder.subtitle.setText(getSubtitle());
                viewHolder.subtitle_content.setText(model.getSubtitle());
                viewHolder.description.setText(getDescription());
                viewHolder.description_content.setText(model.getDescription());

                Glide.with(mContext).load(model.getImageUrl()).skipMemoryCache( true ).into(viewHolder.imageView);
            }
        };

        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView subtitle;
        private TextView subtitle_content;
        private TextView description;
        private TextView description_content;
        private ImageView imageView;

        public DataViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_title);
            date = (TextView) itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_date);
            subtitle = (TextView) itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_subtitle);
            subtitle_content = (TextView) itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_subtitle_content);
            description = (TextView) itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_description);
            description_content = (TextView)itemView.findViewById(fr.project.mlignereux.cv.R.id.tv_description_content);
            imageView = (ImageView) itemView.findViewById(fr.project.mlignereux.cv.R.id.iv_image);
        }
    }
}
