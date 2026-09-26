package com.merveylcu.ngamingcase.feature.posts.data.mapper

import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import com.merveylcu.ngamingcase.feature.posts.data.remote.dto.PostDto
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post

internal fun PostDto.toEntity(): PostEntity = PostEntity(
    id = id,
    userId = userId,
    title = title,
    body = body,
)

internal fun PostEntity.toDto(): PostDto = PostDto(
    userId = userId,
    id = id,
    title = title,
    body = body,
)

internal fun PostEntity.toPost(): Post = Post(
    id = id,
    title = title,
    body = body,
)
