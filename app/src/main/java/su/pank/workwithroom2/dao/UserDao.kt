package su.pank.workwithroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import su.pank.workwithroom.enities.User

@Dao
interface UserDao {
    @Query("Select * FROM user")
    fun getAll(): List<User>

    @Insert
    fun addUser(user: User)
}