package com.devtau.retrofit2

import android.content.res.Resources
import com.google.gson.annotations.SerializedName
import java.util.*
/**
 * POJO class files can be generated from Json strings with help of http://www.jsonschema2pojo.org/
 */
class GitUserModel(
    var login: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null
) {
    fun describeSelf(resources: Resources?): String? {
        val formatter = resources?.getString(R.string.account_data_formatter) ?: return name
        return String.format(Locale.getDefault(), formatter, name, blog, company, avatarUrl)
    }
}