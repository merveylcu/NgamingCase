package com.merveylcu.ngamingcase.feature.posts.domain.usecase

import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import javax.inject.Inject

public class ConfirmDeletePostUseCase @Inject constructor(private val repository: PostRepository) {
    public suspend operator fun invoke(id: Int): RestResult<Unit> = repository.confirmDelete(id)
}
