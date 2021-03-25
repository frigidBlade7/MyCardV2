package com.spaceandjonin.mycrd.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class DropDownAdapter(context: Context, layout: Int, var values: Array<String>) : ArrayAdapter<String>(context,layout,values) {

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.count = values.count()
                results.values = values

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }
}