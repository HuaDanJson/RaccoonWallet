package wacode.yamada.yuki.nempaymentapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.MenuItem
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.extentions.showToast
import wacode.yamada.yuki.nempaymentapp.model.TransactionAppEntity
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.view.activity.callback.TransactionCallback
import wacode.yamada.yuki.nempaymentapp.view.fragment.TransactionDetailFragment
import wacode.yamada.yuki.nempaymentapp.view.fragment.TransactionListFragment
import javax.inject.Inject


class TransactionActivity : BaseFragmentActivity(), TransactionCallback, HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun initialFragment() = TransactionListFragment.newInstance(wallet)
    override fun setLayout() = R.layout.activity_container
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setToolBarBackButton()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_search -> {
                    this.showToast("Coming Soon(すぐとは言ってない)")
                    false
                }
                else -> {
                    super.onOptionsItemSelected(item)
                }
            }

    override fun onReplaceTransactionDetail(entity: TransactionAppEntity) {
        replaceFragment(TransactionDetailFragment.newInstance(entity), true)
    }

    private val wallet by lazy {
        intent.getSerializableExtra(INTENT_WALLET_MODEL) as Wallet
    }

    companion object {
        private const val INTENT_WALLET_MODEL = "intent_wallet_model"

        fun getCallingIntent(context: Context, wallet: Wallet): Intent {
            val intent = Intent(context, TransactionActivity::class.java)
            intent.putExtra(INTENT_WALLET_MODEL, wallet)
            return intent
        }
    }
}