package wacode.yamada.yuki.nempaymentapp.view.activity.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_my_address_profile.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.buildSpannableText
import wacode.yamada.yuki.nempaymentapp.extentions.setSpan
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.view.activity.BaseActivity
import wacode.yamada.yuki.nempaymentapp.view.adapter.SimpleViewPagerAdapter
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.profile.MyProfileInfoFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.profile.MyWalletInfoFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.MyAddressProfileViewModel
import javax.inject.Inject

class MyAddressProfileActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MyAddressProfileViewModel
    override fun setLayout() = R.layout.activity_my_address_profile

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupViewModel()
        setupViews()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyAddressProfileViewModel::class.java)
        viewModel.createLiveData.observe(this, Observer {
            it ?: return@Observer
            // do nothing
        })
    }

    private fun setupViews() {
        setupToolbar()
        setupViewPager()
        changeAddBottomButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            toolbarTitle.apply {
                text = getString(R.string.my_address_profile_activity_title_initial)
            }.buildSpannableText {
                val targetTop = getString(R.string.my_address_profile_activity_title_initial_guest)
                val targetBottom = getString(R.string.my_address_profile_activity_title_initial_bottom_guest)
                it.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MyAddressProfileActivity, R.color.textBlack)), targetTop)
                        .setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MyAddressProfileActivity, R.color.textGrayDark)), targetBottom)
                        .setSpan(AbsoluteSizeSpan(20, true), targetTop)
                        .setSpan(AbsoluteSizeSpan(14, true), targetBottom)
            }
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewPager() {
        ArrayList<BaseFragment>().let {
            it.add(MyWalletInfoFragment.newInstance())
            it.add(MyProfileInfoFragment.newInstance())
            SimpleViewPagerAdapter(this, it, supportFragmentManager).let {
                viewpager.adapter = it
            }
        }
        tabs.setupWithViewPager(viewpager)
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // do nothing
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // do nothing
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> changeAddBottomButton()
                    1 -> changeEditBottomButton()
                }
            }
        })
    }

    private fun changeAddBottomButton() {
        bottomButton.setText(R.string.my_address_profile_activity_bottom_button_add)
        bottomButton.setImage(R.mipmap.icon_plus)
        bottomButton.setClickListener(View.OnClickListener { startActivityForResult(ProfileAddressAddActivity.createIntent(this@MyAddressProfileActivity), ProfileAddressAddActivity.REQUEST_CODE) })
    }

    private fun changeEditBottomButton() {
        bottomButton.setText(R.string.my_address_profile_activity_bottom_button_edit)
        bottomButton.setImage(R.mipmap.icon_pencil)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            when (requestCode) {
                ProfileAddressAddActivity.REQUEST_CODE -> handleProfileAddressAddActivity(resultCode, it)
            }
        }
    }

    private fun handleProfileAddressAddActivity(resultCode: Int, intent: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val item = intent.getSerializableExtra(ProfileAddressAddActivity.INTENT_WALLET_INFO) as WalletInfo
            MyAddress(walletInfoId = item.id).let {
                viewModel.create(it)
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, MyAddressProfileActivity::class.java)
            return intent
        }
    }
}