package fr.project.mlignereux.cv

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ProgressBar
import android.widget.TextView
import fr.project.mlignereux.R
import fr.project.mlignereux.cv.model.Skill
import java.util.*

class CustomExpandableListAdapter(private val context: Context, private val expandableListTitle: List<String>,
                                  private val expandableListDetail: HashMap<String?, List<Skill>>) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Skill? {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]?.get(expandedListPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {
        var view = convertView

        val skill = getChild(listPosition, expandedListPosition) as Skill

        if (view == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.fragment_skill, null)
        }

        val expandedListTextView = view?.findViewById<View>(R.id.name_skill_textview) as TextView
        expandedListTextView.text = skill.title

        val expandedProgressBar = view.findViewById<View>(R.id.skill_progress_bar) as ProgressBar
        expandedProgressBar.progress = skill.value.toInt()

        return view
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.expandableListDetail[this.expandableListTitle[listPosition]]?.size ?: 0
    }

    override fun getGroup(listPosition: Int): Any {
        return this.expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        val listTitle = getGroup(listPosition) as String
        if (view == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.fragment_skill_group, null)
        }
        val listTitleTextView = view?.findViewById<View>(R.id.listTitle) as TextView
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return view
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
