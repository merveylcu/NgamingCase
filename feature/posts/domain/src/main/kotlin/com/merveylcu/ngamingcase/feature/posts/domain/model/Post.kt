package com.merveylcu.ngamingcase.feature.posts.domain.model

public data class Post(
    val id: Int,
    val title: String,
    val body: String,
) {
    val imageUrl: String get() = "https://picsum.photos/300/300?random=$id&grayscale"
}
