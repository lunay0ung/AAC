package com.luna.aac

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.luna.aac.DetailFragment.Companion.ITEM_TITLE
import com.luna.aac.data.Expression
import com.luna.aac.data.Item
import com.luna.aac.data.categoryItems
import com.luna.aac.databinding.ActivitySecondBinding
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class SecondActivity : FragmentActivity(), TextToSpeech.OnInitListener, ParentCallback {

    private val TAG = "luna: " + SecondActivity::class.java.simpleName
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivitySecondBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var tts: TextToSpeech

    private val editText by lazy {
        binding.headerLayout.editText
    }

    private val speakButton by lazy {
        binding.headerLayout.speakButton
    }

    private var selectedItem = categoryItems[0] as Item

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            speakButton.alpha = if(editText.text.isBlank()) 0.1f else 1f
            speakButton.isEnabled = editText.text.isNotBlank()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java].apply {
            this.parentCallback = this@SecondActivity
        }

        tts = TextToSpeech(this, this)

        initUi()

        editText.addTextChangedListener(textWatcher)
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

            headerLayout.apply {
                this.motherButton.setOnClickListener {
                    editText.setText("엄마")
                }

                this.teacherButton.setOnClickListener {
                    editText.setText("선생님")
                }

                this.helpButton.setOnClickListener {
                    editText.setText("저기요")
                }

                speakButton.setOnClickListener {
                    playSentence()
                }

                this.cancelButton.setOnClickListener {
                    editText.text?.clear()
                }
            }
        }
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val isTtsActivated = !(tts.setLanguage(Locale.KOREA) == TextToSpeech.LANG_MISSING_DATA
                    || tts.setLanguage(Locale.KOREA) == TextToSpeech.LANG_NOT_SUPPORTED)

            speakButton.apply {
                this.isEnabled = isTtsActivated
                this.visibility = if (isTtsActivated) View.VISIBLE  else View.INVISIBLE
            }

            if (!isTtsActivated) Log.e(TAG, "!isTtsActivated")

        } else {
            Log.e(TAG, "init failed")
        }
    }

    private fun playSentence() {
        val expression = editText.text.toString()
        tts.speak(expression, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun showExpressions(item: Item) {
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

    override fun onSelectExpression(expression: Expression) {
        editText.setSelection(editText.text.length)
        editText.append(" ")
        editText.append(expression.title)
        editText.setSelection(editText.text.length)
    }

    override fun onBackMainScreen() {
        removeExpressions()
    }

    override fun onDestroy() {
        tts.let {
            it.stop()
            it.shutdown()
        }
        editText.removeTextChangedListener(textWatcher)
        super.onDestroy()
    }
}