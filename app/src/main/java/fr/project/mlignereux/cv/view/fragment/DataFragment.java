package fr.project.mlignereux.cv.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    public FirebaseRecyclerAdapter<Data, DataViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLinearLayoutManager;

    private Context mContext;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String reference, String title, String subtitle, String description){

        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString("REFERENCE", reference);
        args.putString("TITLE", title);
        args.putString("SUBTITLE", subtitle);
        args.putString("DESCRIPTION", description);
        fragment.setArguments(args);

        return fragment;
    }

    public String getReference(){
        return getArguments().getString("REFERENCE");
    }

    public String getTitle(){
        return getArguments().getString("TITLE");
    }

    public String getSubtitle() { return getArguments().getString("SUBTITLE");}

    public String getDescription() { return getArguments().getString("DESCRIPTION"); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getTitle());

        mContext = view.getContext();

        mRecycler = (RecyclerView) view.findViewById(R.id.list);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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

                viewHolder.title.setText(model.getTitle());
                viewHolder.date.setText(model.getDate());
                viewHolder.subtitle.setText(getSubtitle());
                viewHolder.subtitle_content.setText(model.getSubtitle());
                viewHolder.description.setText(getDescription());
                viewHolder.description_content.setText(model.getDescription());

                Glide.with(mContext).load(model.getImageUrl()).skipMemoryCache( true ).into(viewHolder.imageView);
            }
        };

        mAdapter.notifyDataSetChanged();

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

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView subtitle;
        private TextView subtitle_content;
        private TextView description;
        private TextView description_content;
        private ImageView imageView;

        public DataViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            subtitle_content = (TextView) itemView.findViewById(R.id.tv_subtitle_content);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            description_content = (TextView)itemView.findViewById(R.id.tv_description_content);
            imageView = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
