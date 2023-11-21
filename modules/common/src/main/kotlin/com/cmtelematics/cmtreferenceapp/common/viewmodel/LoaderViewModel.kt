package com.cmtelematics.cmtreferenceapp.common.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoaderViewModel @Inject constructor() : ViewModel() {

    val fullScreenLoader: Loader = SimpleLoader()

    val inlineLoader: Loader = SimpleLoader()
}

interface Loader {
    val state: StateFlow<LoaderState>

    fun show()
    fun hide()
}

enum class LoaderState {
    Show, Hide
}

private class SimpleLoader : Loader {
    private val mutableState = MutableStateFlow(LoaderState.Hide)
    override val state = mutableState.asStateFlow()

    override fun show() {
        mutableState.value = LoaderState.Show
    }

    override fun hide() {
        mutableState.value = LoaderState.Hide
    }
}
