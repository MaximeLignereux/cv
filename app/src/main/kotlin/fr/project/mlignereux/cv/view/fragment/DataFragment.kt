package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import dagger.android.support.DaggerFragment
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Data
import fr.project.mlignereux.cv.view.ImageLoader
import javax.inject.Inject

class DataFragment : DaggerFragment() {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var database: DatabaseReference

    private lateinit var adapter: FirebaseRecyclerAdapter<Data, DataViewHolder>
    private lateinit var recycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    private val reference: String
        get() = arguments?.getString("REFERENCE") ?: ""

    private val title: String
        get() = arguments?.getString("TITLE") ?: ""

    private val subtitle: String
        get() = arguments?.getString("SUBTITLE") ?: ""

    private val description: String
        get() = arguments?.getString("DESCRIPTION") ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.fragment_data_list, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = title
        recycler = view.findViewById(R.id.list) as RecyclerView
        return view
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext())

        val dataQuery = database.child(reference).orderByKey().apply { keepSynced(true) }
        adapter = object : FirebaseRecyclerAdapter<Data, DataViewHolder>(Data::class.java, R.layout.fragment_data, DataViewHolder::class.java, dataQuery) {

            override fun populateViewHolder(viewHolder: DataViewHolder, model: Data, position: Int) {

                viewHolder.title.text = model.title
                viewHolder.date.text = model.date
                viewHolder.subtitle.text = subtitle
                viewHolder.subtitleContent.text = model.subtitle
                viewHolder.description.text = description
                viewHolder.descriptionContent.text = model.description

                imageLoader.loadImage(model.imageUrl, viewHolder.imageView)
            }
        }

        adapter.notifyDataSetChanged()
        recycler.layoutManager = linearLayoutManager
        recycler.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.cleanup()
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title) as TextView
        val date: TextView = itemView.findViewById(R.id.tv_date) as TextView
        val subtitle: TextView = itemView.findViewById(R.id.tv_subtitle) as TextView
        val subtitleContent: TextView = itemView.findViewById(R.id.tv_subtitle_content) as TextView
        val description: TextView = itemView.findViewById(R.id.tv_description) as TextView
        val descriptionContent: TextView = itemView.findViewById(R.id.tv_description_content) as TextView
        val imageView: ImageView = itemView.findViewById(R.id.iv_image) as ImageView
    }

    companion object {

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
