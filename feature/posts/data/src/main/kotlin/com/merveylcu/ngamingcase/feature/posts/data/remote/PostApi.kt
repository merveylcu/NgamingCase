package com.merveylcu.ngamingcase.feature.posts.data.remote

import com.merveylcu.ngamingcase.feature.posts.data.remote.dto.PostDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface PostApi {

    @GET("posts")
    suspend fun getPosts(): List<PostDto>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: PostDto): PostDto

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}
