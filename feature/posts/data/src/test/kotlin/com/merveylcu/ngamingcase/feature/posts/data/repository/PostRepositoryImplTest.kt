package com.merveylcu.ngamingcase.feature.posts.data.repository

import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.common.result.ErrorEntity
import com.merveylcu.ngamingcase.core.common.result.RestResult
import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import com.merveylcu.ngamingcase.feature.posts.data.remote.PostApi
import com.merveylcu.ngamingcase.feature.posts.data.remote.dto.PostDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class PostRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestScope(dispatcher)
    private val api: PostApi = mockk()

    private fun repository(dao: FakePostDao) =
        PostRepositoryImpl(api = api, dao = dao, ioDispatcher = dispatcher)

    @Test
    fun `refresh success writes posts to the database`() = testScope.runTest {
        val dao = FakePostDao()
        coEvery { api.getPosts() } returns listOf(dto(1), dto(2))

        val result = repository(dao).refresh()

        assertThat(result).isEqualTo(RestResult.Success(Unit))
        assertThat(dao.posts.map { it.id }).containsExactly(1, 2).inOrder()
    }

    @Test
    fun `refresh does not overwrite deleted or locally modified posts`() = testScope.runTest {
        val dao =
            FakePostDao(
                listOf(
                    entity(1, title = "deleted", isDeleted = true),
                    entity(2, title = "edited", isLocallyModified = true),
                    entity(3, title = "old"),
                ),
            )
        coEvery { api.getPosts() } returns
            listOf(
                dto(1, "remote 1"),
                dto(2, "remote 2"),
                dto(3, "remote 3"),
            )

        repository(dao).refresh()

        val byId = dao.posts.associateBy { it.id }
        assertThat(byId.getValue(1).isDeleted).isTrue()
        assertThat(byId.getValue(1).title).isEqualTo("deleted")
        assertThat(byId.getValue(2).title).isEqualTo("edited")
        assertThat(byId.getValue(3).title).isEqualTo("remote 3")
    }

    @Test
    fun `refresh maps an io failure to a network error`() = testScope.runTest {
        coEvery { api.getPosts() } throws UnknownHostException()

        val result = repository(FakePostDao()).refresh()

        val expected = ErrorEntity.Network(ErrorEntity.Network.NetworkReason.NO_INTERNET)
        assertThat(result).isEqualTo(RestResult.Error(expected))
    }

    @Test
    fun `refresh maps an http failure to an http error`() = testScope.runTest {
        coEvery { api.getPosts() } throws httpException(code = 500)

        val result = repository(FakePostDao()).refresh()

        assertThat(result).isEqualTo(RestResult.Error(ErrorEntity.Http(500)))
    }

    @Test
    fun `soft delete hides the post and restore brings it back`() = testScope.runTest {
        val dao = FakePostDao(listOf(entity(1)))
        val repository = repository(dao)

        repository.softDelete(1)
        assertThat(dao.posts.single().isDeleted).isTrue()

        repository.restore(1)
        assertThat(dao.posts.single().isDeleted).isFalse()
    }

    @Test
    fun `confirm delete keeps the tombstone when the api succeeds`() = testScope.runTest {
        val dao = FakePostDao(listOf(entity(1, isDeleted = true)))
        coEvery { api.deletePost(1) } returns Unit

        val result = repository(dao).confirmDelete(1)

        assertThat(result).isEqualTo(RestResult.Success(Unit))
        assertThat(dao.posts.single().isDeleted).isTrue()
    }

    @Test
    fun `confirm delete restores the post when the api fails`() = testScope.runTest {
        val dao = FakePostDao(listOf(entity(1, isDeleted = true)))
        coEvery { api.deletePost(1) } throws UnknownHostException()

        val result = repository(dao).confirmDelete(1)

        assertThat(result).isInstanceOf(RestResult.Error::class.java)
        assertThat(dao.posts.single().isDeleted).isFalse()
    }

    @Test
    fun `update post writes locally only after the api succeeds`() = testScope.runTest {
        val dao = FakePostDao(listOf(entity(1)))
        coEvery { api.updatePost(1, any()) } answers { secondArg() }

        val result = repository(dao).updatePost(id = 1, title = "new title", body = "new body")

        assertThat(result).isEqualTo(RestResult.Success(Unit))
        coVerify {
            api.updatePost(
                1,
                PostDto(userId = 1, id = 1, title = "new title", body = "new body"),
            )
        }
        with(dao.posts.single()) {
            assertThat(title).isEqualTo("new title")
            assertThat(body).isEqualTo("new body")
            assertThat(isLocallyModified).isTrue()
        }
    }

    @Test
    fun `update post leaves the database unchanged when the api fails`() = testScope.runTest {
        val original = entity(1)
        val dao = FakePostDao(listOf(original))
        coEvery { api.updatePost(1, any()) } throws httpException(code = 500)

        val result = repository(dao).updatePost(id = 1, title = "new title", body = "new body")

        assertThat(result).isEqualTo(RestResult.Error(ErrorEntity.Http(500)))
        assertThat(dao.posts.single()).isEqualTo(original)
    }

    private fun dto(id: Int, title: String = "title $id") =
        PostDto(userId = 1, id = id, title = title, body = "body $id")

    private fun entity(
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

    private fun httpException(code: Int) =
        HttpException(Response.error<Any>(code, "".toResponseBody()))
}
