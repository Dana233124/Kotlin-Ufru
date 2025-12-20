package com.example.urfu.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterBadgeCache {
    private val _showBadge = MutableStateFlow(false)
    val showBadge: StateFlow<Boolean> = _showBadge

    fun setBadgeVisible(visible: Boolean) {
        _showBadge.value = visible
    }
}
