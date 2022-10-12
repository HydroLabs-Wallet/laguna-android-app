package io.novafoundation.nova.feature_assets.presentation.send.create_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.feature_assets.databinding.FragmentCreateContactBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveAdapter
import io.novafoundation.nova.common.data.model.ContactPayload
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CreateContactFragment : BaseFragment<CreateContactPresenter>(), CreateContactView {
    companion object {
        private const val EXTRA_PAYLOAD = "AssetReceiveFragment.extra_asset"
        fun getNewInstance(data: ContactPayload) = CreateContactFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    @InjectPresenter
    lateinit var presenter: CreateContactPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var adapter: AssetReceiveAdapter
    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .createContactComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    lateinit var binding: FragmentCreateContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnClose.setOnClickListener { presenter.onBackCommandClick() }
        binding.tvName.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onTextChanged(it) }
            .launchIn(lifecycleScope)
        binding.btnNext.setOnClickListener { presenter.onNextClicked() }
    }

    override fun onAssetChanged(data: String) {
        binding.tvContact.text = data.ellipsis()
    }

    override fun enableButton(enabled: Boolean) {
        binding.btnNext.isEnabled = enabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
