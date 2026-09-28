package com.merveylcu.ngamingcase.feature.posts.domain.usecase

import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.common.result.resultFlow
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class RefreshPostsUseCase @Inject constructor(private val repository: PostRepository) {
    public operator fun invoke(): Flow<RestResult<Unit>> = resultFlow { repository.refresh() }
}
