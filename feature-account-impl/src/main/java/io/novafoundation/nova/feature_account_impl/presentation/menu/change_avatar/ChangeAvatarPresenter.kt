package io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.AvatarUtils
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ChangeAvatarPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,

    ) :
    BasePresenter<ChangeAvatarView>() {
    var currentAvatar: String? = ""
    var newAvatar = ""
    var metaId = 0L
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setImages(AvatarUtils().avatars)
        interactor.selectedMetaAccountFlow().onEach {
            currentAvatar = it.avatar
            currentAvatar?.let { viewState.setAvatar(it) }
            metaId = it.id

        }.launchIn(presenterScope)
    }

    fun onAvatarClick(data: String) {
        newAvatar = data
        viewState.setAvatar(data)
        validate()
    }

    fun onNextClick() {
        presenterScope.launch {
            interactor.updateMetaAccountAvatar(metaId, newAvatar)
            showSuccess(resourceManager.getString(R.string.avatar_changed))
            router.back()
        }
    }

    fun validate() {
        viewState.enableButton(newAvatar != currentAvatar && newAvatar.isNotEmpty())
    }

    fun onBackCommandClick() {
        router.back()
    }
}
