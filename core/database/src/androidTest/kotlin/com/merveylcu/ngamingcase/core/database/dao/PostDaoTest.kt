package com.merveylcu.ngamingcase.core.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.database.NgamingCaseDatabase
import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostDaoTest {

    private lateinit var database: NgamingCaseDatabase
    private lateinit var dao: PostDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NgamingCaseDatabase::class.java,
        ).build()
        dao = database.postDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun observeVisible_hidesDeletedPosts() = runTest {
        dao.upsertAll(listOf(post(1), post(2), post(3)))

        dao.setDeleted(2, deleted = true)

        assertThat(dao.observeVisible().first().map { it.id }).containsExactly(1, 3).inOrder()
        assertThat(dao.observeById(2).first()).isNull()
    }

    @Test
    fun setDeleted_false_restoresThePost() = runTest {
        dao.upsertAll(listOf(post(1)))
        dao.setDeleted(1, deleted = true)

        dao.setDeleted(1, deleted = false)

        assertThat(dao.observeVisible().first().map { it.id }).containsExactly(1)
    }

    @Test
    fun mergeRemote_doesNotTouchDeletedOrLocallyModifiedPosts() = runTest {
        dao.upsertAll(
            listOf(
                post(1, title = "deleted", isDeleted = true),
                post(2, title = "edited", isLocallyModified = true),
                post(3, title = "old"),
            ),
        )

        dao.mergeRemote(listOf(post(1, "remote 1"), post(2, "remote 2"), post(3, "remote 3"), post(4, "remote 4")))

        assertThat(dao.getById(1)).isEqualTo(post(1, title = "deleted", isDeleted = true))
        assertThat(dao.getById(2)).isEqualTo(post(2, title = "edited", isLocallyModified = true))
        assertThat(dao.getById(3)?.title).isEqualTo("remote 3")
        assertThat(dao.getById(4)?.title).isEqualTo("remote 4")
        assertThat(dao.observeVisible().first().map { it.id }).containsExactly(2, 3, 4).inOrder()
    }

    @Test
    fun count_includesDeletedPosts() = runTest {
        dao.upsertAll(listOf(post(1), post(2, isDeleted = true)))

        assertThat(dao.count()).isEqualTo(2)
    }

    private fun post(
        id: Int,
        title: String = "title $id",
        isDeleted: Boolean = false,
        isLocallyModified: Boolean = false,
    ) = PostEntity(
        id = id,
        userId = 1,
        title = title,
        body = "body $id",
        isDeleted = isDeleted,
        isLocallyModified = isLocallyModified,
    )
}
