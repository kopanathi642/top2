package com.example.fitx

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var isPasswordVisible = false // Track password visibility state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_CLIENT_ID_NUMBERS.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // --- INITIALIZE VIEWS ---
        val emailEditText = findViewById<EditText>(R.id.etEmail)
        val passwordEditText = findViewById<EditText>(R.id.etPassword)
        val passwordToggle = findViewById<ImageView>(R.id.ivPasswordToggle) // Eye Icon
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val forgotPasswordText = findViewById<TextView>(R.id.tvForgotPassword)
        val googleButton = findViewById<LinearLayout>(R.id.btnGoogleSignIn)

        // --- 1. HANDLE MANUAL PASSWORD TOGGLE ---
        passwordToggle.setOnClickListener {
            if (isPasswordVisible) {
                // Hide Password
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_eye) // Change to "Closed Eye" icon
                isPasswordVisible = false
            } else {
                // Show Password
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.ic_eye) // Change to "Open Eye" icon
                isPasswordVisible = true
            }
            // Move cursor to the end of text
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        // --- 2. HANDLE "ENTER" KEY ON PASSWORD ---
        passwordEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                // Programmatically click the login button
                loginButton.performClick()
                true
            } else {
                false
            }
        }

        // --- EMAIL LOGIN LOGIC ---
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else if (email == "kumar" && password == "1") {
                navigateToNextScreen()
            } else {
                // Uncomment for real Firebase Auth:
                /*
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                             navigateToNextScreen()
                        } else {
                             Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                */
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // --- GOOGLE SIGN IN LOGIC ---
        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        // --- FORGOT PASSWORD ---
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    navigateToNextScreen()
                } else {
                    Toast.makeText(this, "Firebase Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToNextScreen() {
        val intent = Intent(this, GenderSelectActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}