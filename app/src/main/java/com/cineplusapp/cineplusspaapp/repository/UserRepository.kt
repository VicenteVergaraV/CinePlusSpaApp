package com.cineplusapp.cineplusspaapp.repository

import com.cineplusapp.cineplusspaapp.data.local.dao.UserDao
import com.cineplusapp.cineplusspaapp.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getUserById(userId: Int): Flow<User?> {
        return userDao.getUserById(userId)
    }
}
