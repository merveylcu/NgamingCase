package com.merveylcu.ngamingcase.feature.posts.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
)
