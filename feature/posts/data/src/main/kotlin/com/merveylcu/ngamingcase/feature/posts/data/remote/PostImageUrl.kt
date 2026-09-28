package com.merveylcu.ngamingcase.feature.posts.data.remote

internal fun postImageUrl(postId: Int): String =
    "https://picsum.photos/300/300?random=$postId&grayscale"
