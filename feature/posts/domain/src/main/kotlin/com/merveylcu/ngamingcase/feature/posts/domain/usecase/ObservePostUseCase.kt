package com.merveylcu.ngamingcase.feature.posts.domain.usecase

import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class ObservePostUseCase
@Inject
constructor(private val repository: PostRepository) {
    public operator fun invoke(id: Int): Flow<Post?> = repository.observePost(id)
}
