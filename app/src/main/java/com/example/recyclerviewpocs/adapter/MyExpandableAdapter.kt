package com.example.recyclerviewpocs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewpocs.R
import com.example.recyclerviewpocs.models.ExpandableModel
import com.example.recyclerviewpocs.utils.CallBackInterface
import kotlinx.android.synthetic.main.child_row.view.*
import kotlinx.android.synthetic.main.header_row.view.*
import kotlinx.android.synthetic.main.header_row.view.header_row
import kotlinx.android.synthetic.main.header_row.view.iv_collapse
import kotlinx.android.synthetic.main.header_row.view.iv_expand
import kotlinx.android.synthetic.main.header_row.view.tv_header
import kotlinx.android.synthetic.main.header_row_new.view.*
import kotlinx.android.synthetic.main.selector_row.view.*

class MyExpandableAdapter(var myList: MutableList<ExpandableModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var callBackInterface: CallBackInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ExpandableModel.HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.header_row_new, parent, false
                    )
                )
            }
            ExpandableModel.CHILD -> {
                ChildViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.child_row_new, parent, false
                    )
                )
            }
            ExpandableModel.SELECTOR -> {
                SelectorViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.selector_row, parent, false
                    )
                )
            }
            else -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.header_row, parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = myList[position]
        when (row.type) {
            ExpandableModel.HEADER -> {
                (holder as HeaderViewHolder).headerTitle.text = row.header?.headerTitle
                holder.icon.setBackgroundResource(row.drawable)
                holder.childCountTv.text = row.header?.childrenList?.size.toString()

                when(row.isExpanded){
                    true -> {
                        holder.expandArrow.visibility = View.GONE
                        holder.collapseArrow.visibility = View.VISIBLE
                    }
                    false -> {
                        holder.collapseArrow.visibility = View.GONE
                        holder.expandArrow.visibility = View.VISIBLE
                    }
                }

                holder.expandArrow.setOnClickListener {
                    expandRow(position)
                }
                holder.collapseArrow.setOnClickListener {
                    collapseRow(position)
                }
            }
            ExpandableModel.CHILD -> {
                (holder as ChildViewHolder).childTitle.text = row.child?.childTitle
                holder.rimNumber.text = row.child?.rimNum
                holder.dateTime.text = row.child?.dateTime
                holder.amount.text = row.child?.amount
                holder.cbChild.isChecked = row.child?.childCheckStatus ?: false
                holder.cbChild.setOnClickListener {
                    if(row.child?.childCheckStatus == true){
                        row.child?.childCheckStatus = false
                        holder.cbChild.isChecked = row.child?.childCheckStatus ?: false

                        for(i in myList){
                            if(i.type == ExpandableModel.HEADER && i.header?.hId == row.header?.hId){
                                i.header?.headerCheckStatus = false
                            }
                        }
                        notifyDataSetChanged()
                    }else{
                        row.child?.childCheckStatus = true
                        holder.cbChild.isChecked = row.child?.childCheckStatus ?: false
                    }
                }
            }
            ExpandableModel.SELECTOR -> {
                (holder as SelectorViewHolder).cbSelector.isChecked = row.header?.headerCheckStatus ?: false
                holder.cbSelector.setOnClickListener {
                    if(row.header?.headerCheckStatus == true){
                        row.header?.headerCheckStatus = false
                        holder.cbSelector.isChecked = row.header?.headerCheckStatus ?: false
                        for(i in row.header?.childrenList ?: mutableListOf()){
                            i.childCheckStatus = false
                        }
                    }else{
                        row.header?.headerCheckStatus = true
                        holder.cbSelector.isChecked = row.header?.headerCheckStatus ?: false
                        for(i in row.header?.childrenList ?: mutableListOf()){
                            i.childCheckStatus = true
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun expandRow(position: Int) {
        val row = myList[position]
        var nextPosition = position
        when (row.type) {
            ExpandableModel.HEADER -> {
                myList[position].isExpanded = true

                var expandableModel = ExpandableModel()
                expandableModel.type = ExpandableModel.SELECTOR
                expandableModel.header = row.header
                myList.add(++nextPosition, expandableModel)

                for (child in row.header!!.childrenList) {
                    var expandableModel = ExpandableModel()
                    expandableModel.type = ExpandableModel.CHILD
                    expandableModel.child = child
                    expandableModel.header = row.header
                    myList.add(++nextPosition, expandableModel)
                }
                notifyDataSetChanged()
            }
            ExpandableModel.CHILD -> {
                notifyDataSetChanged()
            }
        }
    }

    private fun collapseRow(position: Int) {
        val row = myList[position]
        var nextPosition = position + 1
        when (row.type) {
            ExpandableModel.HEADER -> {
                myList[position].isExpanded = false
                outerloop@ while (true) {
                    if (nextPosition == myList.size || myList[nextPosition].type == ExpandableModel.HEADER) {
                        break@outerloop
                    }
                    myList.removeAt(nextPosition)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun getCheckedItems(): String?{
        var result: String? = ""
        for(i in myList){
            if(i.type == ExpandableModel.HEADER) {
                for (j in i.header?.childrenList ?: mutableListOf()) {
                    if (j.childCheckStatus) {
                        result += j.cId.toString() + "\n"
                    }
                }
            }
        }
        return result
    }

    override fun getItemCount(): Int = myList.size

    override fun getItemViewType(position: Int): Int = myList[position].type

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.header_row
        internal var headerTitle = itemView.tv_header
        internal var expandArrow = itemView.iv_expand
        internal var collapseArrow = itemView.iv_collapse
        internal var icon = itemView.icon_title
        internal var childCountTv = itemView.child_count_tv
    }

    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.child_row
        internal var childTitle = itemView.tv_child
        internal var rimNumber = itemView.tv_rim
        internal var dateTime = itemView.tv_datetime
        internal var amount = itemView.tv_amt
        internal var rightArrow = itemView.iv_right
        internal var cbChild = itemView.child_cb
    }

    class SelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var cbSelector = itemView.selector_cb
    }

    fun initCallBackInterface(callBackInterface: CallBackInterface){
        this.callBackInterface = callBackInterface
    }

}