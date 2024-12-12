package com.example.reto.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKey {
    val ID = stringPreferencesKey("id")
    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
    val TOKEN = stringPreferencesKey("token")
    val EMAIL_VERIFIED = booleanPreferencesKey("email_verified")
}