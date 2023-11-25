package com.tong.wanandroid.common.services.cookie

import android.util.Log
import com.tong.wanandroid.common.services.http.RetrofitManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

class CookieJarImpl : CookieJar {

    private val cookieStore = mutableListOf<Cookie>()


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
}