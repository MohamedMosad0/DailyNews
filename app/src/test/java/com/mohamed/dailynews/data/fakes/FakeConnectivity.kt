package com.mohamed.dailynews.data.fakes

import com.mohamed.dailynews.utils.Connectivity

class FakeConnectivity(var isOnlineValue: Boolean = true) : Connectivity {
    override fun isOnline(): Boolean = isOnlineValue
}
