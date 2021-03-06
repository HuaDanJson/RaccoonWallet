package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_friend_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo

class CreateFriendInfoFragment : BaseFragment() {
    override fun layoutRes() = R.layout.fragment_friend_info

    fun getAndCheckFriendInfo(): FriendInfo? {
        return if (validateAllInfo()) {
            createPrams()
        } else {
            null
        }
    }

    private fun validateAllInfo(): Boolean {
        return when {
            nameEditText.text.isNullOrEmpty() -> {
                nameInputLayout.error = getString(R.string.create_friend_address_input_error)
                false
            }
            else -> true
        }
    }

    private fun createPrams(): FriendInfo {
        return FriendInfo(
                name = nameEditText.text.toString(),
                nameRuby = nameRubyEdiText.text.toString(),
                phoneNumber = phoneNumberEditText.text.toString(),
                mailAddress = mailAddressEditText.text.toString()
        )
    }

    companion object {
        const val PAGE_POSITION = 0

        fun newInstance(): CreateFriendInfoFragment {
            val fragment = CreateFriendInfoFragment()
            val args = Bundle()
            args.putInt(ARG_CONTENTS_NAME_ID, R.string.create_friend_address_title)
            fragment.arguments = args
            return fragment
        }
    }
}