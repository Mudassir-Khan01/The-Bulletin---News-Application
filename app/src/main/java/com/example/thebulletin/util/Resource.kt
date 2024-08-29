package com.example.thebulletin.util

sealed class Resource<T>(// ans is successful or error //wrap your responses
    val data:T?=null,
    val message:String?=null
) {
    class Success<T>(data: T):Resource<T>(data)
    class Error<T>(message: String, data: T?=null):Resource<T>(data,message)
    class Loading<T> : Resource<T>()
}