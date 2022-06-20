package io.novafoundation.nova.feature_assets.presentation.send_receive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.databinding.FragmentSendReceivePopupBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SendReceiveFragment : BaseFragment(), SendReceiveView {
    companion object {
        fun getNewInstance(): SendReceiveFragment = SendReceiveFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SendReceivePresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSendReceivePopupBinding

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendReceiveComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendReceivePopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.holderSend.setOnClickListener { presenter.onAssetChooseClicked() }
        binding.holderReceive.setOnClickListener { presenter.onAssetChooseClicked() }
        binding.root.setOnClickListener { presenter.onBackCommandClick() }

    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
