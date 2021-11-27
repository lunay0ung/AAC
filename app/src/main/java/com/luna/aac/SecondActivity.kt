package com.luna.aac

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.luna.aac.DetailFragment.Companion.ITEM_TITLE
import com.luna.aac.data.Item
import com.luna.aac.data.categoryItems
import com.luna.aac.databinding.ActivitySecondBinding

@SuppressLint("NotifyDataSetChanged")
class SecondActivity : FragmentActivity(), ParentCallback {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivitySecondBinding
    private lateinit var itemAdapter: ItemAdapter

    private val editText by lazy {
        binding.headerLayout.editText
    }

    private var selectedItem = categoryItems[0] as Item


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java).apply {
            this.parentCallback = this@SecondActivity
        }

        initUi()
    }

    private fun initUi() {
        with(binding) {
            recyclerView.apply {
                itemAdapter = ItemAdapter(categoryItems.filter { it.title != "dummy" },
                    object : ItemAdapter.ItemClickListener {
                        override fun onClickItem(item: Item) {
                            selectedItem = item
                            editText.text?.clear()
                            editText.setText(selectedItem.getItemTitle())
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
                removeExpressions()
            }

            headerLayout.cancelButton.setOnClickListener {
                editText.text?.clear()
            }
        }
    }

    fun showExpressions(item: Item) {
        var fragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = bundleOf(
            ITEM_TITLE to item.getItemTitle()
        )
        if (fragment == null) {
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

    private fun removeExpressions() {
        binding.recyclerView.visibility = View.VISIBLE
        val fragment = supportFragmentManager.findFragmentById(binding.fragmentContainer.id)
            ?: return
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commitNowAllowingStateLoss()
    }

    override fun onBackMainScreen() {
        removeExpressions()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}