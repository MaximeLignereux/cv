package fr.project.mlignereux.cv.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.project.mlignereux.cv.R
import fr.project.mlignereux.cv.model.Data

class DataFragment : Fragment() {

    private var mDatabase: DatabaseReference? = null

    lateinit var mAdapter: FirebaseRecyclerAdapter<Data, DataViewHolder>
    private lateinit var mRecycler: RecyclerView
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    val reference: String?
        get() = arguments?.getString("REFERENCE")

    val title: String?
        get() = arguments?.getString("TITLE")

    val subtitle: String?
        get() = arguments?.getString("SUBTITLE")

    val description: String?
        get() = arguments?.getString("DESCRIPTION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_data_list, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = title

        mRecycler = view.findViewById<View>(R.id.list) as RecyclerView

        return view
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mLinearLayoutManager = LinearLayoutManager(requireContext())

        val dataQuery = reference?.let { mDatabase?.child(it)?.orderByKey() }
        dataQuery?.keepSynced(true)

        mAdapter = object : FirebaseRecyclerAdapter<Data, DataViewHolder>(
            Data::class.java,
            R.layout.fragment_data,
            DataViewHolder::class.java,
            dataQuery) {

            override fun populateViewHolder(viewHolder: DataViewHolder, model: Data, position: Int) {

                viewHolder.title.text = model.title
                viewHolder.date.text = model.date
                viewHolder.subtitle.text = subtitle
                viewHolder.subtitleContent.text = model.subtitle
                viewHolder.description.text = description
                viewHolder.descriptionContent.text = model.description

                Glide.with(requireContext()).load(model.imageUrl).skipMemoryCache(true).into(viewHolder.imageView)
            }
        }

        mAdapter.notifyDataSetChanged()

        mRecycler.layoutManager = mLinearLayoutManager
        mRecycler.adapter = mAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.cleanup()
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<View>(R.id.tv_title) as TextView
        val date: TextView = itemView.findViewById<View>(R.id.tv_date) as TextView
        val subtitle: TextView = itemView.findViewById<View>(R.id.tv_subtitle) as TextView
        val subtitleContent: TextView = itemView.findViewById<View>(R.id.tv_subtitle_content) as TextView
        val description: TextView = itemView.findViewById<View>(R.id.tv_description) as TextView
        val descriptionContent: TextView = itemView.findViewById<View>(R.id.tv_description_content) as TextView
        val imageView: ImageView = itemView.findViewById<View>(R.id.iv_image) as ImageView
    }

    companion object {

        private val TAG = "DataFragment"

        fun newInstance(reference: String, title: String, subtitle: String, description: String): DataFragment {

            val fragment = DataFragment()
            val args = Bundle()
            args.putString("REFERENCE", reference)
            args.putString("TITLE", title)
            args.putString("SUBTITLE", subtitle)
            args.putString("DESCRIPTION", description)
            fragment.arguments = args

            return fragment
        }
    }
}
