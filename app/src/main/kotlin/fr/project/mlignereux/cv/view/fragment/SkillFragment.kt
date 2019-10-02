package fr.project.mlignereux.cv.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dagger.android.support.DaggerFragment
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Skill
import fr.project.mlignereux.cv.view.CustomExpandableListAdapter
import java.util.*

class SkillFragment : DaggerFragment() {

    lateinit var expandableListView: ExpandableListView
    lateinit var expandableListAdapter: ExpandableListAdapter
    lateinit var expandableListTitle: MutableList<String>
    lateinit var expandableListDetail: HashMap<String?, List<Skill>>

    private lateinit var reference: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.skill_title)

        val view = inflater.inflate(R.layout.fragment_skill_list, container, false)
        expandableListView = view.findViewById(R.id.expanded_list) as ExpandableListView
        expandableListTitle = ArrayList()
        expandableListDetail = HashMap()

        reference = getString(R.string.skill_bundle)

        FirebaseDatabase.getInstance().reference.child(reference).addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                updateExpandableList(dataSnapshot)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                updateExpandableList(dataSnapshot)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return view
    }

    private fun updateExpandableList(dataSnapshot: DataSnapshot) {
        val skills = arrayListOf<Skill>()
        expandableListTitle.add(dataSnapshot.key ?: "")
        dataSnapshot.children.forEach { data ->
            val skill = data.getValue(Skill::class.java) ?: Skill()
            skills.add(skill)
        }
        expandableListDetail[dataSnapshot.key] = skills
        expandableListAdapter = CustomExpandableListAdapter(requireContext(), expandableListTitle, expandableListDetail)
        expandableListView.setAdapter(expandableListAdapter)
    }
}
