package dev.arli.sunnyday.ui.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : ViewEvent, State : ViewState, Effect : ViewEffect> : ViewModel() {

    private val initialViewState: State by lazy { initialViewState() }

    private val _viewState: MutableStateFlow<State> = MutableStateFlow(initialViewState)
    val viewState: StateFlow<State> = _viewState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        observeEvents()
    }

    abstract fun initialViewState(): State

    abstract fun handleEvent(event: Event)

    private fun observeEvents() {
        _event.onEach(::handleEvent).launchIn(viewModelScope)
    }

    fun onEventSent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: (State) -> State) {
        _viewState.update(reducer)
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
