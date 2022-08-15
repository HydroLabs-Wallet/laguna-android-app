package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSeedCreateBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.SeedPromptFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SeedCreateFragment : BaseFragment<SeedCreatePresenter>(), SeedCreateView {
    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"
        fun getNewInstance(payload: AddAccountPayload): SeedCreateFragment = SeedCreateFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to payload)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SeedCreatePresenter

    @ProvidePresenter
    fun createPresenter() = presenter
    lateinit var binding: FragmentSeedCreateBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .seedCreateComponentFactory()
            .create(
                fragment = this,
                isAuth = argument(EXTRA_IS_AUTH)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeedCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.btnCopy.setOnClickListener { presenter.onCopyClick() }
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
        binding.tvSubTitle.setOnClickListener { presenter.onInfoClick() }

    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }

    override fun copyToClipboard(data: String) {
        val clip = ClipData.newPlainText(getString(R.string.app_name), data)
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(clip)
        showSuccess(getString(R.string.seed_copied_to_clip))
    }

    override fun setSeeds(data: List<SeedWord>) {
        binding.seedView.submitList(data)
    }

    override fun enableNext() {
        TODO("Not yet implemented")
    }

    override fun disableNexT() {
        TODO("Not yet implemented")
    }
}
