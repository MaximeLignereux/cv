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
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.view.CustomExpandableListAdapter
import fr.project.mlignereux.cv.model.Skill
import java.util.*

class SkillFragment : Fragment() {

    lateinit var expandableListView: ExpandableListView
    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListTitle: MutableList<String>
    lateinit var expandableListDetail: HashMap<String?, List<Skill>>

    private val reference: String?
        get() = arguments?.getString("REFERENCE")

    private val title: String?
        get() = arguments?.getString("TITLE")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val v = inflater.inflate(R.layout.fragment_skill_list, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = title

        expandableListView = v.findViewById<View>(R.id.expanded_list) as ExpandableListView
        expandableListTitle = ArrayList()
        expandableListDetail = HashMap()

        reference?.let {
            FirebaseDatabase.getInstance().reference.child(it).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                    val skills = ArrayList<Skill>()
                    dataSnapshot.key?.let { it1 -> (expandableListTitle as ArrayList<String>).add(it1) }

                    for (d in dataSnapshot.children) {
                        val skill = d.getValue(Skill::class.java)
                        if (skill != null) {
                            skills.add(skill)
                        }
                    }
                    expandableListDetail[dataSnapshot.key] = skills
                    expandableListAdapter =
                        CustomExpandableListAdapter(
                            requireContext(),
                            expandableListTitle,
                            expandableListDetail
                        )
                    expandableListView.setAdapter(expandableListAdapter)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val skills = ArrayList<Skill>()
                    dataSnapshot.key?.let { it1 -> expandableListTitle.add(it1) }
                    for (d in dataSnapshot.children) {
                        val skill = d.getValue(Skill::class.java)
                        skill?.let { it1 -> skills.add(it1) }
                    }
                    expandableListDetail[dataSnapshot.key] = skills
                    expandableListAdapter =
                        CustomExpandableListAdapter(
                            requireContext(),
                            expandableListTitle,
                            expandableListDetail
                        )
                    expandableListView.setAdapter(expandableListAdapter)
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
