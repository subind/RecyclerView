package com.example.recyclerviewpocs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewpocs.R
import com.example.recyclerviewpocs.models.ExpandableModel
import kotlinx.android.synthetic.main.child_row.view.*
import kotlinx.android.synthetic.main.header_row.view.*

class MyExpandableAdapter(var myList: MutableList<ExpandableModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ExpandableModel.HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.header_row, parent, false
                    )
                )
            }
            ExpandableModel.CHILD -> {
                ChildViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.child_row, parent, false
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
                (holder as HeaderViewHolder).headerTitle.text = row.header.headerTitle

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
                (holder as ChildViewHolder).childTitle.text = row.child.childTitle
                holder.rimNumber.text = row.child.rimNum
                holder.dateTime.text = row.child.dateTime
                holder.amount.text = row.child.amount
            }
        }
    }

    private fun expandRow(position: Int) {
        val row = myList[position]
        var nextPosition = position
        when (row.type) {
            ExpandableModel.HEADER -> {
                myList.removeAt(position)
                myList.add(
                    position,
                    ExpandableModel(ExpandableModel.HEADER, row.header, true)
                )
                for (child in row.header.childrenList) {
                    myList.add(
                        ++nextPosition,
                        ExpandableModel(ExpandableModel.CHILD, child)
                    )
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
                myList.removeAt(position)
                myList.add(
                    position,
                    ExpandableModel(ExpandableModel.HEADER, row.header, false)
                )
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

    override fun getItemCount(): Int = myList.size

    override fun getItemViewType(position: Int): Int = myList[position].type

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.header_row
        internal var headerTitle = itemView.tv_header
        internal var expandArrow = itemView.iv_expand
        internal var collapseArrow = itemView.iv_collapse
    }

    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var layout = itemView.child_row
        internal var childTitle = itemView.tv_child
        internal var rimNumber = itemView.tv_rim
        internal var dateTime = itemView.tv_datetime
        internal var amount = itemView.tv_amt
        internal var rightArrow = itemView.iv_right
    }

}