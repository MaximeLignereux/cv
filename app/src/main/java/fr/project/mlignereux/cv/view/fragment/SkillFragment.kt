package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import fr.project.mlignereux.cv.CustomExpandableListAdapter
import fr.project.mlignereux.cv.model.Skill
import java.util.*

class SkillFragment : Fragment() {

    lateinit var mExpandableListView: ExpandableListView
    lateinit var mExpandableListAdapter: ExpandableListAdapter
    lateinit var mExpandableListTitle: MutableList<String>
    lateinit var mExpandableListDetail: HashMap<String?, List<Skill>>

    val reference: String?
        get() = arguments?.getString("REFERENCE")

    val title: String?
        get() = arguments?.getString("TITLE")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val v = inflater.inflate(fr.project.mlignereux.cv.R.layout.fragment_skill_list, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = title

        mExpandableListView = v.findViewById<View>(fr.project.mlignereux.cv.R.id.expanded_list) as ExpandableListView
        mExpandableListTitle = ArrayList()
        mExpandableListDetail = HashMap()

        reference?.let {
            FirebaseDatabase.getInstance().reference.child(it).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    val skills = ArrayList<Skill>()
                    dataSnapshot.key?.let { it1 -> (mExpandableListTitle as ArrayList<String>).add(it1) }

                    for (d in dataSnapshot.children) {
                        val skill = d.getValue(Skill::class.java)
                        if (skill != null) {
                            skills.add(skill)
                        }
                    }
                    mExpandableListDetail[dataSnapshot.key] = skills
                    mExpandableListAdapter = CustomExpandableListAdapter(requireContext(), mExpandableListTitle, mExpandableListDetail)
                    mExpandableListView.setAdapter(mExpandableListAdapter)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val skills = ArrayList<Skill>()
                    dataSnapshot.key?.let { it1 -> mExpandableListTitle.add(it1) }
                    for (d in dataSnapshot.children) {
                        val skill = d.getValue(Skill::class.java)
                        skill?.let { it1 -> skills.add(it1) }
                    }
                    mExpandableListDetail[dataSnapshot.key] = skills
                    mExpandableListAdapter = CustomExpandableListAdapter(requireContext(), mExpandableListTitle, mExpandableListDetail)
                    mExpandableListView.setAdapter(mExpandableListAdapter)
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        return v
    }

    companion object {

        private val TAG = "SkillFragment"

        fun newInstance(reference: String, title: String): Fragment {

            val fragment = SkillFragment()
            val args = Bundle()
            args.putString("TITLE", title)
            args.putString("REFERENCE", reference)
            fragment.arguments = args

            return fragment
        }
    }


}
