package com.example.firebase_exercise.common

class MovieAddingFailedException : Exception("Movie Adding Failed!")
class MovieExistenceException : Exception("Movie Exists")
class UserSignInFailedException : Exception("Sign In Failed")
class UserSignOutFailedException : Exception("Sign Out Failed")