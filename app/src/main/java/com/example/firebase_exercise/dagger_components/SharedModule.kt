package com.example.firebase_exercise.dagger_components

import android.content.Context
import com.example.firebase_exercise.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class SharedModule {

    @Provides
    fun provideMoviesFirebaseDatabase(): DatabaseReference =
        FirebaseDatabase.getInstance().reference

    @Provides
    fun providesMoviesFirestoreDatabase(): CollectionReference =
        Firebase.firestore.collection("users")

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideGoogleSignClient(
        @ApplicationContext context: Context
    ): GoogleSignInClient =
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
}