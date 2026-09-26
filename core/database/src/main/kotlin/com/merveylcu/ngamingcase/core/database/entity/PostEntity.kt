package com.merveylcu.ngamingcase.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local copy of a post. Room is the single source of truth for the UI.
 *
 * @property isDeleted Tombstone flag. Deleted posts are hidden but kept so a refresh cannot bring them back.
 * @property isLocallyModified Set after a successful edit so a refresh cannot overwrite the local change.
 */
@Entity(tableName = "posts")
public data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isDeleted: Boolean = false,
    val isLocallyModified: Boolean = false,
)
