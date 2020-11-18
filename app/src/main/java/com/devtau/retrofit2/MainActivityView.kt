package com.devtau.retrofit2

interface MainActivityView {
    fun showProgress(show: Boolean)

    fun clearUserInfo()
    fun showUserInfo(user: GitUserModel?)

    fun clearError()
    fun showError(msg: String)
}