package com.gtp01.group01.android.recipesmobileapp.repository

import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    /**
     * Asynchronously signs in a user with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @return A [Task] representing the result of the sign-in operation, containing an [AuthResult].
     */
    suspend fun signIn(email: String, password: String): Task<AuthResult>

    /**
     * Asynchronously signs up a new user with the provided email and password.
     *
     * @param email The new user's email.
     * @param password The new user's password.
     * @return A [Task] representing the result of the sign-up operation, containing an [AuthResult].
     */
    suspend fun signUp(email: String, password: String): Task<AuthResult>

    /**
     * Asynchronously signs up a new user with Google Sign-In.
     *
     * @param account The Google [SignInCredential] obtained from the sign-in process.
     * @return A [Task] representing the result of the sign-up operation, containing an [AuthResult].
     */
    suspend fun signUpWithGoogle(account: SignInCredential): Task<AuthResult>

    /**
     * Get the currently signed-in user.
     *
     * @return A [FirebaseUser] representing the currently signed-in user, or null if no user is signed in.
     */
    fun getCurrentUser(): FirebaseUser?

    /**
     * Signs out the current user.
     */
    fun signOut()
}