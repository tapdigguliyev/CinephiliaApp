package az.abb.tap.cinephilia.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import az.abb.tap.cinephilia.feature.feature1.model.media.Media

class BaseAdapter<T : Any> : PagingDataAdapter<T, BaseViewHolder<T>>(
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }
) {

    var expressionOnBindViewHolder: ((T, ViewBinding) -> Unit)? = null
    var expressionOnCreateViewHolder: ((ViewGroup) -> ViewBinding)? = null

//     val differCallback = object : DiffUtil.ItemCallback<T>(){
//        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
//            return  oldItem == newItem
//        }
//
//        @SuppressLint("DiffUtilEquals")
//        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
//            return oldItem == newItem
//        }
//
//    }
//
//    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return expressionOnCreateViewHolder?.let { it(parent) }?.let { BaseViewHolder(it, expressionOnBindViewHolder!!) }!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

//    override fun getItemCount() = differ.currentList.size

//    val diffUtil = object : DiffUtil.ItemCallback<Int>() {
//        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
//            return oldItem == newItem
//        }
//
//        @SuppressLint("DiffUtilEquals")
//        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
//            return oldItem.hashCode() == newItem.hashCode()
//        }
//
//    }
}