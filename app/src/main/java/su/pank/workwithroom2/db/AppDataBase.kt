package su.pank.workwithroom.db

import androidx.room.Database
import androidx.room.RoomDatabase
import su.pank.workwithroom.dao.UserDao
import su.pank.workwithroom.enities.User

@Database(entities = [User::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
}