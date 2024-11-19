package com.demo.recipelist.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
){
    private companion object {
        const val TAG = "UserPreferencesRepo"
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
    }

    // 寫入 Preferences DataStore >>
    // 將 lambda 傳遞至 edit() 即可在 DataStore 中建立和更新值
    // lambda 會傳遞 MutablePreferences 的執行個體，用於更新 DataStore 中的值。
    // 注意：除非呼叫這個函式且已設定值，否則該值不會存在於 DataStore 中。
    // 在 edit() 中設定鍵/值組合，即會定義並初始化該值，直至 App 的快取或資料遭到清除為止。
    suspend fun saveLayoutPreference(isLinearLayout: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }

    // 從 Preferences DataStore 讀取 >>
    // dataStore.data 屬性是 Preferences 物件的 Flow。Preferences 物件包含 DataStore 中的所有鍵/值組合。
    // 可使用 map() 將 Flow<Preferences> 轉換為 Flow<Boolean>，指定定義的索引鍵，以取得版面配置偏好設定
    val isLinearLayout: Flow<Boolean> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            // 在定義並初始化偏好設定之前，DataStore 中不存在偏好設定。
            // 因此必須確認偏好設定確實存在，如果沒有預設值，則須提供預設值。
            // 尚未呼叫 saveLayoutPreference，preferences[IS_LINEAR_LAYOUT]不一定存在
            preferences[IS_LINEAR_LAYOUT] ?: true
        }
}