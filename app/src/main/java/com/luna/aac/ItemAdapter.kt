package com.luna.aac

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luna.aac.data.Item
import com.luna.aac.databinding.ItemBinding

class ItemAdapter(
    private val dataSet: List<Item>,
    private val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].getType().ordinal
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(
        private val binding: ItemBinding,
        private val itemClickListener: ItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.root.setOnClickListener {
                itemClickListener.onClickItem(item)
            }
            binding.imageView.setImageResource(item.getDrawableResId())
            binding.textView.text = item.getItemTitle()
            binding.textView.typeface = if (itemClickListener.isSelected(item)) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
    }

    interface ItemClickListener {
        fun onClickItem(item: Item)
        fun isSelected(item: Item): Boolean
    }
}