package com.merveylcu.ngamingcase.feature.posts.data.mapper

import com.google.common.truth.Truth.assertThat
import com.merveylcu.ngamingcase.core.database.entity.PostEntity
import org.junit.Test

class PostMapperTest {

    @Test
    fun `post image url is built from the post id`() {
        val post = entity(id = 7).toPost()

        assertThat(post.imageUrl).isEqualTo("https://picsum.photos/300/300?random=7&grayscale")
    }

    @Test
    fun `image url does not depend on list position`() {
        val posts = listOf(entity(id = 3), entity(id = 5)).map { it.toPost() }
        val afterFirstRemoved = listOf(entity(id = 5)).map { it.toPost() }

        assertThat(afterFirstRemoved.single().imageUrl).isEqualTo(posts[1].imageUrl)
    }

    private fun entity(id: Int) = PostEntity(id = id, userId = 1, title = "title", body = "body")
}
