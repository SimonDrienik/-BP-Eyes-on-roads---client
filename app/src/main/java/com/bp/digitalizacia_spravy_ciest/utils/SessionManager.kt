package com.bp.digitalizacia_spravy_ciest.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.bp.digitalizacia_spravy_ciest.R

//session manager: save and fetch data from SharedPreferences
class SessionManager (context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_NAME = "user_name"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_ROLE_ID = "user_role_id"
    }

    //LogOut and clear session
    @SuppressLint("CommitPrefEdits")
    fun clear(){
        val editor = prefs.edit()
        editor.clear()
        editor
            .remove(USER_TOKEN)
            .remove(USER_NAME)
            .remove(USER_ID)
            .remove(USER_EMAIL)
            .remove(USER_ROLE_ID)
        editor.apply()
    }

    //save user name
    fun saveUserName(userName: String){
        val editor = prefs.edit()
        editor.putString(USER_NAME, userName)
        editor.apply()
    }

    //save user id
    fun saveUserId(userId: String){
        val editor = prefs.edit()
        editor.putString(USER_ID, userId)
        editor.apply()
    }

    //save user email
    fun saveUserEmail(userEmail: String){
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, userEmail)
        editor.apply()
    }

    //save user role id
    fun saveUserRoleId(userRoleId: String){
        val editor = prefs.edit()
        editor.putString(USER_ROLE_ID, userRoleId)
        editor.apply()
    }

    //save auth token
    fun saveAuthToken(token: String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    //fetch auth token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, "Neprihlásený")
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun fetchUserRoleId(): String? {
        return prefs.getString(USER_ROLE_ID, "2")
    }
}