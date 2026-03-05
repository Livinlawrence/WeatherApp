package com.weatherapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weatherapp.data.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT EXISTS(SELECT 1 FROM locations WHERE id = :id AND isFavorite = 1)")
    suspend fun isLocationFavorite(id: String): Boolean

    @Query("SELECT * FROM locations WHERE isFavorite = 1")
    suspend fun getFavorites(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocation(id: String): LocationEntity?

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("UPDATE locations SET isFavorite = :isFavorite WHERE id = :locationId")
    suspend fun updateFavoriteStatus(locationId: String, isFavorite: Boolean)

    @Query("DELETE FROM locations WHERE id = :id")
    suspend fun deleteLocation(id: String)
}