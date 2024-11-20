package ru.livetyping.zarina.core.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
public abstract class UserDao {
    public fun getUserFlow(): Flow<UserEntity?> {
        return getUserFlowImpl().distinctUntilChanged()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract suspend fun saveUser(user: UserEntity)

    @Query("DELETE FROM ${UserEntity.TABLE_NAME}")
    public abstract suspend fun clear()

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} LIMIT 1")
    protected abstract fun getUserFlowImpl(): Flow<UserEntity?>
}
