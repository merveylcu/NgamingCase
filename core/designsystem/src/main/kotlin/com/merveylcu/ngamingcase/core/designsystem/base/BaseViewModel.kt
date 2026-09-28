package com.merveylcu.ngamingcase.core.designsystem.base

import androidx.lifecycle.ViewModel
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.common.base.DialogState
import com.merveylcu.ngamingcase.core.common.base.UiEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S, E : UiEffect> : ViewModel() {

    private val _uiState = MutableStateFlow<BaseUiState<S>>(BaseUiState.Loading)
    val uiState: StateFlow<BaseUiState<S>> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<E>(extraBufferCapacity = EFFECT_BUFFER_CAPACITY)
    val uiEffect: SharedFlow<E> = _uiEffect.asSharedFlow()

    protected val currentContent: S?
        get() = (_uiState.value as? BaseUiState.Content)?.data

    protected fun setState(state: BaseUiState<S>) {
        _uiState.value = state
    }

    protected fun setContent(data: S) {
        _uiState.update { current ->
            if (current is BaseUiState.Content) {
                current.copy(
                    data = data,
                )
            } else {
                BaseUiState.Content(data)
            }
        }
    }

    protected fun updateContent(transform: S.() -> S) {
        _uiState.update { current ->
            if (current is BaseUiState.Content) {
                current.copy(
                    data = current.data.transform(),
                )
            } else {
                current
            }
        }
    }

    protected fun showDialog(dialog: DialogState) {
        updateDialogState(dialog)
    }

    protected fun dismissDialog() {
        updateDialogState(null)
    }

    protected fun emitEffect(effect: E) {
        _uiEffect.tryEmit(effect)
    }

    private fun updateDialogState(dialog: DialogState?) {
        _uiState.update { current ->
            if (current is BaseUiState.Content) current.copy(dialogState = dialog) else current
        }
    }

    private companion object {
        const val EFFECT_BUFFER_CAPACITY = 16
    }
}
