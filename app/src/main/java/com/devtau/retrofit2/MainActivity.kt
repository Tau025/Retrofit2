package com.devtau.retrofit2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.devtau.retrofit2.util.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), MainActivityView {

    private var restClient = RESTClient(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        request_data_button.setOnClickListener {
            clearUserInfo()
            clearError()
            restClient.sendRequest(user_name_edit_text.text.toString())
        }
    }

    override fun showProgress(show: Boolean) {
        progress.isVisible = show
    }

    override fun clearUserInfo() = showUserInfo(null)

    override fun showUserInfo(user: GitUserModel?) {
        if (user == null) {
            user_avatar.isVisible = false
            user_info.isVisible = false
            user_info_text_view.text = ""
            return
        }

        if (user.avatarUrl.isNullOrEmpty()) {
            user_avatar.isVisible = false
        } else {
            user_avatar.isVisible = true
            Glide.with(this).load(user.avatarUrl)
                .transform(CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(user_avatar)
        }

        user_info.isVisible = true
        user_info_text_view.text = user.describeSelf(resources)
    }

    override fun clearError() = showError("")

    override fun showError(msg: String) {
        error_title.isVisible = msg.isNotEmpty()
        error_msg.text = msg
    }
}