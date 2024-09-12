package com.example.personal_training.vista.ui.dietas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dietas Fragment"
    }
    val text: LiveData<String> = _text
}