package com.merveylcu.ngamingcase.feature.posts.domain.usecase

import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import javax.inject.Inject

public class IsPostCacheEmptyUseCase
@Inject
constructor(private val repository: PostRepository) {
    public suspend operator fun invoke(): Boolean = repository.isEmpty()
}
