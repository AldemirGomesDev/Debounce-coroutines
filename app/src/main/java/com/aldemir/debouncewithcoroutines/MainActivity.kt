package com.aldemir.debouncewithcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext
    get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autoCompleteTextView.addTextChangedListener(
            DebouncingAddChangeListener.debounce(textViewResult)
        )

        searchView.setOnQueryTextListener(
            DebouncingQueryTextListener { newText ->
                newText?.let {
                    if (it.isEmpty()) {
                        Log.w("Debounce", "search clear: ")
                    } else {
                        Log.w("Debounce", "search 1: ${it}")
                        textViewResult.text = it
                    }
                }
            }
        )

        editText.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText == searchFor)
                    return

                searchFor = searchText

                launch {
                    delay(1000)  //debounce timeOut
                    if (searchText != searchFor)
                        return@launch

                    // do our magic here
                    Log.w("Debounce", "Debounce: ${searchFor}")
                    textViewResult.text = searchFor
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        })
    }

}
