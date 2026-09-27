package com.merveylcu.ngamingcase.feature.posts.data.repository

import com.merveylcu.ngamingcase.core.common.qualifiers.IoDispatcher
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.common.result.mapOnSuccess
import com.merveylcu.ngamingcase.core.database.dao.PostDao
import com.merveylcu.ngamingcase.feature.posts.data.mapper.toDto
import com.merveylcu.ngamingcase.feature.posts.data.mapper.toEntity
import com.merveylcu.ngamingcase.feature.posts.data.mapper.toPost
import com.merveylcu.ngamingcase.feature.posts.data.remote.PostApi
import com.merveylcu.ngamingcase.feature.posts.domain.model.Post
import com.merveylcu.ngamingcase.feature.posts.domain.repository.PostRepository
import com.merveylcu.ngamingcase.network.extensions.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val api: PostApi,
    private val dao: PostDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PostRepository {

    override fun observePosts(): Flow<List<Post>> = dao.observeVisible()
        .map { entities -> entities.map { it.toPost() } }
        .flowOn(ioDispatcher)

    override fun observePost(id: Int): Flow<Post?> = dao.observeById(id)
        .map { it?.toPost() }
        .distinctUntilChanged()
        .flowOn(ioDispatcher)

    override suspend fun isEmpty(): Boolean = withContext(ioDispatcher) { dao.count() == 0 }

    override suspend fun refresh(): RestResult<Unit> = withContext(ioDispatcher) {
        safeApiCall { api.getPosts() }
            .mapOnSuccess { posts -> dao.mergeRemote(posts.map { it.toEntity() }) }
    }

    override suspend fun softDelete(id: Int): Unit = withContext(ioDispatcher) { dao.setDeleted(id, deleted = true) }

    override suspend fun restore(id: Int): Unit = withContext(ioDispatcher) { dao.setDeleted(id, deleted = false) }

    override suspend fun confirmDelete(id: Int): RestResult<Unit> = withContext(ioDispatcher) {
        val result = safeApiCall { api.deletePost(id) }
        if (result is RestResult.Error) dao.setDeleted(id, deleted = false)
        result
    }

    override suspend fun updatePost(
        id: Int,
        title: String,
        body: String,
    ): RestResult<Unit> = withContext(ioDispatcher) {
        val current = dao.getById(id) ?: return@withContext RestResult.Error(ErrorEntity.Unknown)
        val updated = current.copy(title = title, body = body, isLocallyModified = true)
        safeApiCall { api.updatePost(id, updated.toDto()) }
            .mapOnSuccess { dao.upsert(updated) }
    }
}
