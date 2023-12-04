package com.tong.wanandroid.common.services.cookie

import com.tong.wanandroid.common.services.http.RetrofitManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

const val COOKIE_LOGIN_USER_NAME = "loginUserName_wanandroid_com"
const val COOKIE_LOGIN_USER_TOKEN = "token_pass_wanandroid_com"
class CookieJarImpl : CookieJar {

    val cookieStore = mutableListOf<Cookie>()


    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val (expiredCookies, validCookies) = cookieStore
            .partition { it.expiresAt < System.currentTimeMillis() }

        cookieStore.removeAll(expiredCookies)
        return validCookies.filter { it.matches(url) }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val (expiredCookies, validCookies) = cookies.partition { it.expiresAt < System.currentTimeMillis() }
        cookieStore.removeAll(expiredCookies)
        cookieStore.addAll(validCookies.filter { it.persistent })
    }

    fun isLoginCookieValid(): Boolean {
        var isUserNameValid = false
        var isUserTokenValid = false
        RetrofitManager.cookieJar.cookieStore.forEach {
            if (it.name == COOKIE_LOGIN_USER_NAME) {
                isUserNameValid = it.value.isNotBlank()
            }
            if (it.name == COOKIE_LOGIN_USER_TOKEN) {
                isUserTokenValid = it.value.isNotBlank()
            }
        }
        return isUserNameValid && isUserTokenValid
    }

    fun clear(){
        cookieStore.clear()
    }
}