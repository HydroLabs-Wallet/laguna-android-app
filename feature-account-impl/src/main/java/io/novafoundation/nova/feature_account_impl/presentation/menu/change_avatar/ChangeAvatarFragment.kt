package io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.hideKeyboard
import io.novafoundation.nova.common.view.GridSpacingItemDecoration
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.FragmentChangeAvatarBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ChangeAvatarFragment : BaseFragment<ChangeAvatarPresenter>(), ChangeAvatarView {
    companion object {
        fun getNewInstance(): ChangeAvatarFragment = ChangeAvatarFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ChangeAvatarPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentChangeAvatarBinding
    lateinit var adapter: AvatarAdapter
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .changeAvatarComponentFactory()
            .create(
                fragment = this,
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnCancel.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.rvList.layoutManager = GridLayoutManager(requireContext(), 5)
        val spacing = resources.getDimensionPixelSize(R.dimen.margin_16)
        binding.rvList.addItemDecoration(GridSpacingItemDecoration(5, spacing, false))
        adapter = AvatarAdapter()
        binding.rvList.adapter = adapter
        adapter.onItemClick = { presenter.onAvatarClick(it) }
    }

    override fun setAvatar(data: String) {
        if (data.isDigitsOnly()) {
            binding.imAvatar.load(data.toInt())
        } else {
            binding.imAvatar.load(data)
        }
    }

    override fun enableButton(enable: Boolean) {
        binding.btnNext.isEnabled = enable
    }

    override fun setImages(data: List<String>) {
        adapter.items = data
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
