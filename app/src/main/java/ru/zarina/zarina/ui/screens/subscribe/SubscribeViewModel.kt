package ru.zarina.zarina.ui.screens.subscribe

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zarina.zarina.data.StaticPages
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val interactor: SubscribeInteractor,
) : ViewModel(),
    ISideEffectSource<SubscribeViewModel.SideEffect> by SideEffectQueue() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _isSwitchChecked = MutableStateFlow(false)
    val isSwitchChecked = _isSwitchChecked.asStateFlow()

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        _isSwitchChecked.value = isChecked
    }

    fun onBackClick() {
        // TODO
    }

    fun onLinkClick(link: Link) {
        val url = when (link) {
            Link.POLICY -> StaticPages.PRIVACY_POLICY_URL
            Link.RULES -> StaticPages.CONDITIONS_URL
            Link.DATA -> StaticPages.DATA_POLICY_URL
        }
        sideEffect(SideEffect.ShowBrowser(url))
    }

    enum class Link { POLICY, RULES, DATA }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowBrowser(val url: String) : SideEffect
    }

}
