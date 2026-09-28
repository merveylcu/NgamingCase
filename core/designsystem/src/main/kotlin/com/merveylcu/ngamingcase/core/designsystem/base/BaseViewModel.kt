package com.merveylcu.ngamingcase.core.designsystem.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.merveylcu.ngamingcase.core.common.UiText
import com.merveylcu.ngamingcase.core.common.base.BaseUiState
import com.merveylcu.ngamingcase.core.common.base.DialogState
import com.merveylcu.ngamingcase.core.common.base.UiEffect
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.designsystem.R
import com.merveylcu.ngamingcase.core.ui.extension.toMessageRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        _uiState.update { current ->
            if (current is BaseUiState.Content) current.copy(dialogState = dialog) else current
        }
    }

    protected fun dismissDialog() {
        _uiState.update { current ->
            if (current is BaseUiState.Content) current.copy(dialogState = null) else current
        }
    }

    protected fun emitEffect(effect: E) {
        _uiEffect.tryEmit(effect)
    }

    protected fun <T> Flow<RestResult<T>>.request(
        showLoading: Boolean = true,
        onError: ((ErrorEntity) -> Unit)? = null,
        onSuccess: (T) -> Unit = {},
    ) {
        viewModelScope.launch {
            collect { result ->
                when (result) {
                    is RestResult.Loading -> if (showLoading) setLoadingOverlay(result.isLoading)

                    is RestResult.Success -> {
                        if (showLoading) setLoadingOverlay(false)
                        onSuccess(result.data)
                    }

                    is RestResult.Error -> {
                        if (showLoading) setLoadingOverlay(false)
                        (onError ?: ::handleError)(result.error)
                    }
                }
            }
        }
    }

    protected open fun handleError(error: ErrorEntity) {
        if (_uiState.value is BaseUiState.Content) {
            showDialog(
                DialogState(
                    message = UiText.StringResource(error.toMessageRes()),
                    confirmText = UiText.StringResource(R.string.base_ok),
                    onConfirm = ::dismissDialog,
                    onDismiss = ::dismissDialog,
                ),
            )
        } else {
            setState(BaseUiState.Error(error))
        }
    }

    private fun setLoadingOverlay(isLoading: Boolean) {
        _uiState.update { current ->
            if (current is BaseUiState.Content) current.copy(isLoading = isLoading) else current
        }
    }

    private companion object {
        const val EFFECT_BUFFER_CAPACITY = 16
    }
}
