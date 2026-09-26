package com.merveylcu.ngamingcase.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.merveylcu.ngamingcase.core.database.dao.PostDao
import com.merveylcu.ngamingcase.core.database.entity.PostEntity

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false,
)
public abstract class NgamingCaseDatabase : RoomDatabase() {
    public abstract fun postDao(): PostDao
}
