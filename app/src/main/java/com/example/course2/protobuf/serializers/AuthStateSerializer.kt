package com.example.course2.protobuf.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.course2.protobuf.model.AuthState
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AuthStateSerializer : Serializer<AuthState> {
    override val defaultValue: AuthState
        get() = AuthState.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthState {
        try {
            return AuthState.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AuthState, output: OutputStream) {
        t.writeTo(output)
    }

}