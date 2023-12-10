package com.example.course2.protobuf

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.course2.protobuf.model.AuthState
import com.example.course2.protobuf.serializers.AuthStateSerializer

private const val AUTH_PREFERENCES_NAME = "auth_prefs"
private const val DATA_STORE_FILE_NAME = "auth_prefs.pb"
private const val SORT_ORDER_KEY = "sort_order"

val Context.authPreferencesStore: DataStore<AuthState> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = AuthStateSerializer
)