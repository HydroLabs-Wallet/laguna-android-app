package io.novafoundation.nova.feature_assets.presentation.send.address_choose

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentSendAddressChooseBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.send.ContactUiMarker
import io.novafoundation.nova.feature_assets.presentation.send.address_choose.adapter.SendAddressChooseAdapter
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class SendAddressChooseFragment : BaseFragment(), SendAddressChooseView {
    companion object {
        const val EXTRA_PAYLOAD = "AssetReceiveFragment.extra_asset"

        fun getNewInstance(data: AssetPayload) = SendAddressChooseFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: SendAddressChoosePresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSendAddressChooseBinding
    lateinit var adapter: SendAddressChooseAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendAddressChooseComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendAddressChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SendAddressChooseAdapter()
        adapter.onItemClick = { asset -> presenter.onItemClicked(asset) }
        binding.tvSearch.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onSearchChanged(it) }
            .launchIn(lifecycleScope)
        binding.rvList.adapter = adapter
        binding.tvSearch.setOnEndClickListener { presenter.searchButtonClicked() }
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnMyAccounts.setOnClickListener { presenter.onMyAccountClick() }
        binding.tvSearchResult.setOnClickListener { presenter.onSearchResultClicked() }

    }

    override fun submitList(data: List<ContactUiMarker>, query: String) {
        adapter.items = data
        if (data.isEmpty() && query.isNotBlank()) {
            binding.holderSearchResult.isVisible = true
            binding.tvSearch.setIconEnd(R.drawable.ic_size_24_user_add)
            binding.tvSearch.setTextEnd(R.string.save)
            binding.tvSearchResult.text = query.ellipsis()
        } else {
            binding.holderSearchResult.isVisible = false
            binding.tvSearch.setIconEnd(R.drawable.ic_size_24_clipboard)
            binding.tvSearch.setTextEnd(R.string.paste)
        }
    }

    override fun paste() {
        val clipboard: ClipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var pasteData = ""
        when {
            !clipboard.hasPrimaryClip() -> {
                // no clip
            }
            clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) != true -> {
                // since the clipboard has data but it is not plain text
            }
            else -> {

                //since the clipboard contains plain text.
                val item: ClipData.Item? = clipboard.getPrimaryClip()?.getItemAt(0)

                // Gets the clipboard as text.
                pasteData = item?.text.toString()
                binding.tvSearch.setText(pasteData)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
