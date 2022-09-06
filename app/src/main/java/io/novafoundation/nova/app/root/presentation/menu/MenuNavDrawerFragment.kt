package io.novafoundation.nova.app.root.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import coil.load
import io.novafoundation.nova.app.databinding.FragmentMenuNavDrawerBinding
import io.novafoundation.nova.app.root.di.RootApi
import io.novafoundation.nova.app.root.di.RootComponent
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_impl.R
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MenuNavDrawerFragment : BaseFragment<NavDrawerPresenter>(), NavDrawerView {
    companion object {
        fun getNewInstance(): MenuNavDrawerFragment = MenuNavDrawerFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: NavDrawerPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentMenuNavDrawerBinding

    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .menuComponentFactory()
            .create(fragment = this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuNavDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imVisible.setOnClickListener { presenter.toggleValueVisibility() }
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnLock.setOnClickListener { presenter.onLockClicked() }
        binding.imEditName.setOnClickListener { presenter.editName() }
        binding.imIconBig.setOnClickListener { presenter.onAvatarClick() }
        binding.holderChangePassword.setOnClickListener { presenter.toChangePassword() }
        binding.holderChangeAutoLock.setOnClickListener { presenter.onAutoLockClick() }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }

    override fun setName(data: String) {
        binding.tvName.text = data
        binding.tvLogOut.text = getString(R.string.log_out_skywalker, data)
    }

    override fun setAvatar(data: String) {
        if (data.isDigitsOnly()) {
            binding.imIconBig.load(data.toInt())
        } else {
            binding.imIconBig.load(data)
        }
    }

    override fun setBalance(data: String) {
        binding.tvTokenAmount.text = data
    }

    override fun setShowValues(data: Boolean) {
        if (data) {
            binding.imVisible.setImageResource(R.drawable.ic_size_24_eye)
        } else {
            binding.imVisible.setImageResource(R.drawable.ic_size_24_eye_off)
        }
    }

    override fun setContactNumbers(data: String) {
        TODO("Not yet implemented")
    }
}
