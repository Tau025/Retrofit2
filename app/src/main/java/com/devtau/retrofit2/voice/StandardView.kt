package com.devtau.retrofit2.voice

import io.reactivex.functions.Action

interface StandardView {
    fun showMsg(msgId: Int, confirmedListener: Action? = null, cancelledListener: Action? = null)
    fun showMsg(msg: String, confirmedListener: Action? = null, cancelledListener: Action? = null)
}