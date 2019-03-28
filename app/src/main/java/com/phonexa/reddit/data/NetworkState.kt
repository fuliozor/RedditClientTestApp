package com.phonexa.reddit.data

sealed class NetworkState
object StateLoading : NetworkState()
object StateLoaded : NetworkState()
data class StateError(val msg: String?) : NetworkState()
