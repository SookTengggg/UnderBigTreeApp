package com.example.underbigtreeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy
import com.example.underbigtreeapp.model.MenuEntity

@Dao
interface MenuDao{
    @Query("SELECT * FROM Menu")
    fun getAllMenus(): Flow<List<MenuEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenus(menus: List<MenuEntity>)
}