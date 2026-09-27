package com.merveylcu.ngamingcase.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
public data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isDeleted: Boolean = false,
    val isLocallyModified: Boolean = false,
)
