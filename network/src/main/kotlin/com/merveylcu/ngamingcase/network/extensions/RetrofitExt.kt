package com.merveylcu.ngamingcase.network.extensions

import retrofit2.Retrofit

public inline fun <reified T> Retrofit.create(): T = create(T::class.java)
