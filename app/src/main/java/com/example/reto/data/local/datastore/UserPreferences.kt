package com.example.reto.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.reto.data.local.model.UserModel

class UserPreferences(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    suspend fun saveUser(user: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKey.ID] = user.id
            preferences[UserPreferencesKey.NAME] = user.name
            preferences[UserPreferencesKey.EMAIL] = user.email
            preferences[UserPreferencesKey.TOKEN] = user.token
            preferences[UserPreferencesKey.EMAIL_VERIFIED] = user.emailVerified
        }
    }

//    suspend fun logout() {
//        context.dataStore.edit { preferences ->
//            preferences[UserPreferencesKey.ID] = ""
//            preferences[UserPreferencesKey.NAME] = ""
//            preferences[UserPreferencesKey.EMAIL] = ""
//            preferences[UserPreferencesKey.TOKEN] = ""
//            preferences[UserPreferencesKey.EMAIL_VERIFIED] = false
//        }
//    }
}