package com.tong.wanandroid.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.UserBaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManager private constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wan_android_datastore")

    companion object {
        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager =
            instance ?: synchronized(this) {
                instance ?: DataStoreManager(context.applicationContext).also { instance = it }
            }
    }

    private object PreferencesKeys {
        val IS_LOGIN = booleanPreferencesKey("key_is_login")
        val USER_INFO = stringPreferencesKey("key_account_user_info")
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGIN] = isLoggedIn
        }
    }

    suspend fun cacheUserBaseInfo(user: UserBaseModel) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_INFO] = Gson().toJson(user)
        }
    }

    suspend fun clearUserBaseInfo() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_INFO] = ""
        }
    }

    fun getLoginCache() : Boolean{
        return RetrofitManager.cookieJar.isLoginCookieValid()
    }

    val isLogIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_LOGIN] ?: false
        }

    val userInfo: Flow<UserBaseModel> = context.dataStore.data
        .map {
            Gson().fromJson(it[PreferencesKeys.USER_INFO], UserBaseModel::class.java)
        }
}

