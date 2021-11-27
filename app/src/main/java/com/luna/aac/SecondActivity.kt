package com.luna.aac

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.luna.aac.DetailFragment.Companion.ITEM_TITLE
import com.luna.aac.data.Item
import com.luna.aac.data.categoryItems
import com.luna.aac.databinding.ActivitySecondBinding

@SuppressLint("NotifyDataSetChanged")
class SecondActivity : FragmentActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var itemAdapter: ItemAdapter
    private var selectedItem = categoryItems[0] as Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)

        initUi()
    }

    private fun initUi() {
        with(binding) {
            recyclerView.apply {
                itemAdapter = ItemAdapter(categoryItems.filter { it.title != "dummy" },
                    object : ItemAdapter.ItemClickListener {
                        override fun onClickItem(item: Item) {
                            selectedItem = item
                            showExpressions(item)
                            itemAdapter.notifyDataSetChanged()
                        }

                        override fun isSelected(item: Item): Boolean {
                            return selectedItem == item
                        }
                    })
                layoutManager = GridLayoutManager(context, 5)
                adapter = itemAdapter
            }

            headerLayout.homeButton.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                val fragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
                    ?: return@setOnClickListener
                supportFragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitNowAllowingStateLoss()
            }
        }
    }

    fun showExpressions(item: Item) {
        var fragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = bundleOf(
            ITEM_TITLE to item.getItemTitle()
        )
        if(fragment == null) {
            fragment = DetailFragment.newInstance()
            fragment.arguments = bundle
            transaction.add(binding.fragmentContainer.id, fragment)
        } else {
            fragment.arguments = bundle
            transaction.show(fragment)
        }
        binding.recyclerView.visibility = View.INVISIBLE
        transaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}