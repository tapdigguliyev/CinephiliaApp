package az.abb.tap.cinephilia.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseAdapter<T : Any> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    var expressionOnBindViewHolder: ((T, ViewBinding) -> Unit)? = null
    var expressionOnCreateViewHolder: ((ViewGroup) -> ViewBinding)? = null

    private val differCallback = object : DiffUtil.ItemCallback<T>(){
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return  oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        return expressionOnCreateViewHolder?.let { it(parent) }?.let { BaseViewHolder(it, expressionOnBindViewHolder!!) }!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}