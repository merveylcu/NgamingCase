package com.merveylcu.ngamingcase.feature.posts.data.repository

import com.merveylcu.ngamingcase.core.database.dao.PostDao
import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [PostDao]. `mergeRemote` is inherited from the interface, so the real merge logic is tested.
 */
internal class FakePostDao(initial: List<PostEntity> = emptyList()) : PostDao {

    private val table = MutableStateFlow(initial.associateBy { it.id })

    val posts: List<PostEntity> get() = table.value.values.sortedBy { it.id }

    override fun observeVisible(): Flow<List<PostEntity>> = table.map { rows -> rows.values.filterNot { it.isDeleted }.sortedBy { it.id } }

    override fun observeById(id: Int): Flow<PostEntity?> = table.map { rows -> rows[id]?.takeUnless { it.isDeleted } }

    override suspend fun getById(id: Int): PostEntity? = table.value[id]

    override suspend fun count(): Int = table.value.size

    override suspend fun getProtectedIds(): List<Int> = table.value.values.filter { it.isDeleted || it.isLocallyModified }.map { it.id }

    override suspend fun upsertAll(posts: List<PostEntity>) {
        table.value = table.value + posts.associateBy { it.id }
    }

    override suspend fun upsert(post: PostEntity) {
        table.value = table.value + (post.id to post)
    }

    override suspend fun setDeleted(id: Int, deleted: Boolean) {
        val post = table.value[id] ?: return
        table.value = table.value + (id to post.copy(isDeleted = deleted))
    }
}
