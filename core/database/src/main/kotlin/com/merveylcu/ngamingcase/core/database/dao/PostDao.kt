package com.merveylcu.ngamingcase.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts WHERE isDeleted = 0 ORDER BY id")
    public fun observeVisible(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id AND isDeleted = 0")
    public fun observeById(id: Int): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE id = :id")
    public suspend fun getById(id: Int): PostEntity?

    @Query("SELECT COUNT(*) FROM posts")
    public suspend fun count(): Int

    @Query("SELECT id FROM posts WHERE isDeleted = 1 OR isLocallyModified = 1")
    public suspend fun getProtectedIds(): List<Int>

    @Upsert
    public suspend fun upsertAll(posts: List<PostEntity>)

    @Upsert
    public suspend fun upsert(post: PostEntity)

    @Query("UPDATE posts SET isDeleted = :deleted WHERE id = :id")
    public suspend fun setDeleted(id: Int, deleted: Boolean)

    /**
     * Writes server data without touching deleted or locally modified posts.
     */
    @Transaction
    public suspend fun mergeRemote(remote: List<PostEntity>) {
        val protectedIds = getProtectedIds().toSet()
        upsertAll(remote.filterNot { it.id in protectedIds })
    }
}
