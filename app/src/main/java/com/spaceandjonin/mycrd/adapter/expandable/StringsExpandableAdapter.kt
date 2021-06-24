package com.spaceandjonin.mycrd.adapter.expandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.spaceandjonin.mycrd.databinding.LabelChildItemBinding
import com.spaceandjonin.mycrd.databinding.LabelHeaderItemBinding
import com.spaceandjonin.mycrd.listeners.StringInteraction
import com.spaceandjonin.mycrd.viewholders.BaseViewHolder

class StringsExpandableAdapter constructor(
    val groupList: List<String>,
    val childList: List<List<String>>,
    val itemInteraction: StringInteraction
) : BaseExpandableListAdapter() {

    //expandable adapter that contains a header item and a list of child items
    lateinit var groupItem: BaseViewHolder //header item
    lateinit var childItem: BaseViewHolder //children items

    override fun getGroupCount(): Int {
        return groupList.size //number of groups
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childList[groupPosition].size //number of items per group
    }

    override fun getGroup(groupPosition: Int): Any {
        return groupList[groupPosition] //get group header
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childList[groupPosition][childPosition] //child item per list
    }

    // the id of the group
    override fun getGroupId(groupPosition: Int): Long {
        return groupList[groupPosition].hashCode().toLong()
    }

    //the id of the item
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childList[groupPosition][childPosition].hashCode().toLong()

    }

    override fun hasStableIds(): Boolean {
        return false //because we aren't sure if the hashcode function will prevent collision for larger datasets
    }


    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        //using viewbinding to display group items
        val binding =
            LabelHeaderItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        groupItem = BaseViewHolder(binding)
        groupItem.bindTo(getGroup(groupPosition))

        binding.icon.isSelected = isExpanded

        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        //using viewbinding to display child items

        val binding =
            LabelChildItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        childItem = BaseViewHolder(binding)

        //filter makes sure you have only one contributor per cashout destination number
        childItem.bindTo(getChild(groupPosition, childPosition))

        binding.container.setOnClickListener {
            itemInteraction.onItemClicked(getChild(groupPosition, childPosition) as String)
        }

        return binding.root

    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        //disable selectable children
        return true
    }

}