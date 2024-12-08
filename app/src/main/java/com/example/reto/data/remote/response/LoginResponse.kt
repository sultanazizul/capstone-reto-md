package com.example.reto.data.remote.response

data class LoginResponse(
	val message: String,
	val userCredential: UserCredential,
)

data class UserCredential(
	val user: UserResponse,
	val providerId: String?,
	val tokenResponse: TokenResponse?,
	val operationType: String,
)

data class UserResponse(
	val uid: String,
	val email: String,
	val emailVerified: Boolean,
	val isAnonymous: Boolean,
	val providerData: List<ProviderData>,
	val stsTokenManager: StsTokenManager,
	val createdAt: Long,
	val lastLoginAt: Long,
	val apiKey: String,
	val appName: String,
)

data class StsTokenManager(
	val refreshToken: String,
	val accessToken: String,
	val expirationTime: Long,
)

data class ProviderData(
	val providerId: String,
	val uid: String,
	val displayName: String?,
	val email: String,
	val phoneNumber: String?,
	val photoUrl: String?,
)

data class TokenResponse(
	val kind: String,
	val localId: String,
	val email: String,
	val displayName: String,
	val idToken: String,
	val registered: Boolean,
	val refreshToken: String,
	val expiresIn: Long,
)
