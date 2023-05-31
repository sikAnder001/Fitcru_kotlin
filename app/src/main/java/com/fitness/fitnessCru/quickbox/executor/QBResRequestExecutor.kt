package com.fitness.fitnessCru.quickbox.executor

import com.quickblox.core.QBEntityCallback
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser

fun signUp(newQbUser: QBUser, callback: QBEntityCallback<QBUser>) {
    QBUsers.signUp(newQbUser).performAsync(callback)
}

fun signInUser(currentQbUser: QBUser, callback: QBEntityCallback<QBUser>) {
    QBUsers.signIn(currentQbUser).performAsync(callback)
}

