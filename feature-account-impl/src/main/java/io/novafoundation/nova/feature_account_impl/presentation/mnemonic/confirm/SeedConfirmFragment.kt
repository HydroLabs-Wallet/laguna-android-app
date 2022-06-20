package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSeedConfirmBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SeedConfirmFragment : BaseFragment(), SeedConfirmView {
    companion object {
        private const val EXTRA_LIST = "extra_list"
        const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(isAuth: Boolean, data: List<SeedWord>): SeedConfirmFragment =
            SeedConfirmFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_LIST, ArrayList(data))
                    putBoolean(EXTRA_IS_AUTH, isAuth)
                }
            }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SeedConfirmPresenter

    @ProvidePresenter
    fun createPresenter() = presenter.apply {
        requireNotNull(arguments)
        presenter.isAuth = requireArguments().getBoolean(EXTRA_IS_AUTH)
        requireArguments().getParcelableArrayList<SeedWord>(EXTRA_LIST)?.let {
            seedList.addAll(it)
        }
    }

    lateinit var binding: FragmentSeedConfirmBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(context!!, AccountFeatureApi::class.java)
            .seedConfirmComponentFactory()
            .create(
                fragment = this,
                isAuth = argument(EXTRA_IS_AUTH),
                list = argument(EXTRA_LIST)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeedConfirmBinding.inflate(inflater, container, false)
        binding.seedView.showNumber = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.seedView.onItemClick = { presenter.onSeedListItemClick(it) }
        binding.seedConfirm.setOnSelectionChangedListener { presenter.onSelectionChanged(it) }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
    }

    override fun setList(data: List<SeedWord>) {
        binding.seedView.submitList(data)
    }

    override fun setSelection(data: List<SeedWord>) {
        binding.seedConfirm.setData(data)
    }

    override fun enableButton(isEnabled: Boolean) {
        binding.btnNext.isEnabled = isEnabled
    }

    override fun showConfirmationError() {
        Snackbar.make(
            requireView(),
            "Seed words does not match. Please, try again",
            Snackbar.LENGTH_SHORT
        ).show()

    }

    override fun showError(text: String) {
        Snackbar.make(
            requireView(),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
