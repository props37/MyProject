package ru.livetyping.zarina.data.user.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.livetyping.zarina.data.user.local.database.entity.UserEntity

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun saveUser(user: UserEntity)

    fun getUserFlow(userId: String): Flow<UserEntity?> {
        return getUserFlowImpl(userId).distinctUntilChanged()
    }

    @Query("DELETE FROM ${UserEntity.TABLE_NAME}")
    abstract suspend fun clear()

    @Query(
        """
            SELECT *
            FROM ${UserEntity.TABLE_NAME}
            WHERE ${UserEntity.FIELD_ID} = :userId
        """
    )
    protected abstract fun getUserFlowImpl(userId: String): Flow<UserEntity?>
}
