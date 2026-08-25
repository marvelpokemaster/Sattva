package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import com.example.core.ui.theme.SattvaTheme
import com.example.features.main.MainScreen
import com.example.features.main.SattvaViewModel
import java.security.MessageDigest
import java.util.UUID

class MainActivity : ComponentActivity() {
    private val viewModel by lazy { SattvaViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.core.config.applicationContext = this.applicationContext
        enableEdgeToEdge()
        setContent {
            SattvaTheme {
                MainScreen(
                    viewModel = viewModel,
                    onGoogleSignIn = { launchGoogleSignIn() }
                )
            }
        }
    }

    private fun launchGoogleSignIn() {
        lifecycleScope.launch {
            try {
                val credentialManager = CredentialManager.create(this@MainActivity)
                
                // Fetch Web Client ID from resources if available
                val webClientId = resources.getString(resources.getIdentifier("default_web_client_id", "string", packageName))
                
                val rawNonce = UUID.randomUUID().toString()
                val bytes = rawNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(bytes)
                val hashedNonce = digest.joinToString("") { "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .setNonce(hashedNonce)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    context = this@MainActivity,
                    request = request
                )
                
                val credential = result.credential
                if (credential is GoogleIdTokenCredential) {
                    val idToken = credential.idToken
                    viewModel.signInWithGoogleToken(idToken) { success, error ->
                        if (!success) {
                            Log.e("MainActivity", "Firebase auth failed: $error")
                        }
                    }
                }
            } catch (e: GetCredentialException) {
                Log.e("MainActivity", "Google Sign in failed", e)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error finding web client id or other", e)
            }
        }
    }
}
