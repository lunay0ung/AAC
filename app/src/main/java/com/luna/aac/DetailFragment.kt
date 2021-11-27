package com.luna.aac

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.luna.aac.data.Category
import com.luna.aac.data.Expression
import com.luna.aac.data.Item
import com.luna.aac.data.categoryItems
import com.luna.aac.databinding.FragmentDetailBinding

@SuppressLint("NotifyDataSetChanged")
class DetailFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var parentCallback: ParentCallback
    private lateinit var itemAdapter: ItemAdapter
    private var categoryTitle: String = ""
    private lateinit var expressions: List<Expression>
    private var selectedItem: Expression = Expression(R.drawable.ic_launcher_background, "dummy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
            }
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
            this@DetailFragment.parentCallback = this.parentCallback
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryTitle = arguments?.getString(ITEM_TITLE).toString()
        expressions = getExpressionsByCategoryTitle(categoryTitle)

        initUi()
    }

    private fun initUi() {
        with(binding) {
            expressions = getExpressionsByCategoryTitle(categoryTitle)
            recyclerView.apply {
                itemAdapter = ItemAdapter(expressions, object : ItemAdapter.ItemClickListener {
                    override fun onClickItem(item: Item) {
                        selectedItem = item as Expression
                        if (selectedItem.title == "뒤로 가기") {
                            parentCallback.onBackMainScreen()
                            return
                        }
                        itemAdapter.notifyDataSetChanged()
                    }

                    override fun isSelected(item: Item): Boolean {
                        return selectedItem == item as Expression
                    }
                })

                layoutManager = GridLayoutManager(context, 5)
                adapter = itemAdapter
            }
        }
    }

    private fun getExpressionsByCategoryTitle(categoryTitle: String): List<Expression> {
        return (categoryItems.find { it.title == categoryTitle } as Category).expressions
    }

    companion object {
        const val ITEM_TITLE = "itemTitle"
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }
}