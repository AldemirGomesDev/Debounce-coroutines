package com.aldemir.debouncewithcoroutines

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.*

abstract class DebouncingAddChangeListener {

    companion object {

        fun debounce(textView: TextView): TextWatcher {
            var debouncePeriod: Long = 500

            return object : TextWatcher {
                private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

                private var searchJob: Job? = null

                override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        search?.let {
                            delay(debouncePeriod)
                            Log.w("Debounce", "search debounce: ${search}")
                            textView.setText(search)

                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            }

        }

    }

}