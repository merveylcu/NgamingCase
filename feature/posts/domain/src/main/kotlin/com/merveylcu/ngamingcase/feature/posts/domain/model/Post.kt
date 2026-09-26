package com.merveylcu.ngamingcase.feature.posts.domain.model

/**
 * The image is keyed by [id], not by list position, so it stays the same after other posts are removed.
 */
public data class Post(
    val id: Int,
    val title: String,
    val body: String,
) {
    val imageUrl: String get() = "https://picsum.photos/300/300?random=$id&grayscale"
}
