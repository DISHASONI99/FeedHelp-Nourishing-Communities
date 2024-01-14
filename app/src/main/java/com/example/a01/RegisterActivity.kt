package com.example.a01

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        val login = findViewById<TextView>(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        //Google
        val signInButton = findViewById<LinearLayout>(R.id.gsignin)
        signInButton.setOnClickListener {
            signIn()
        }


        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener{
            val name = findViewById<EditText>(R.id.name)
            val email = findViewById<EditText>(R.id.email)
            val password = findViewById<EditText>(R.id.pwd)
            val confirmPassword = findViewById<EditText>(R.id.cpwd)

            if(name.text.toString().isEmpty()) {
                name.error = "Please enter your name"
                name.requestFocus()
                return@setOnClickListener
            }
            else if(name.text.toString().contains("[0-9]".toRegex())) {
                name.error = "Name cannot contain digits"
                name.requestFocus()
                return@setOnClickListener
            }
            else if(name.text.toString().contains("[!@#$%^&*(),.?\":{}|<>]".toRegex())) {
                name.error = "Name cannot contain symbols"
                name.requestFocus()
                return@setOnClickListener
            }
            else if(email.text.toString().isEmpty()) {
                email.error = "Please enter your email"
                email.requestFocus()
                return@setOnClickListener
            }
            else if(!email.text.toString().contains("@")) {
                email.error = "Please enter a valid email"
                email.requestFocus()
                return@setOnClickListener
            }
            else if(password.text.toString().isEmpty()) {
                password.error = "Please enter your password"
                password.requestFocus()
                return@setOnClickListener
            }
            else if(password.text.toString().length < 8) {
                password.error = "Password should be at least 8 characters long"
                password.requestFocus()
                return@setOnClickListener
            }
            else if(confirmPassword.text.toString().isEmpty()) {
                confirmPassword.error = "Please confirm your password"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }
            else if(password.text.toString() != confirmPassword.text.toString()) {
                confirmPassword.error = "Passwords do not match"
                confirmPassword.requestFocus()
                return@setOnClickListener
            }
            else{
                createUserAccount()
            }
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RegisterActivity.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressDialog.setMessage("Signing In...")
        progressDialog.show()
        if (requestCode == RegisterActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid
                    val timestamp: String = System.currentTimeMillis().toString()
                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["uid"] = uid.toString()
                    hashMap["email"] = user?.email.toString()
                    hashMap["name"] = user?.displayName.toString()
                    hashMap["profileImage"] = ""
                    hashMap["userType"] = "user"
                    hashMap["timestamp"] = timestamp

                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                    val userRef = ref.child(uid.toString())

                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                progressDialog.dismiss()
                                Toast.makeText(this@RegisterActivity, "Login Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                userRef.setValue(hashMap)
                                    .addOnSuccessListener {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@RegisterActivity, "Login Success", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@RegisterActivity, "SignIn Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            progressDialog.dismiss()
                            Toast.makeText(this@RegisterActivity, "Error checking user data", Toast.LENGTH_SHORT).show()
                        }
                    })

                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createUserAccount() {
        progressDialog.setMessage("Creating account...")
        progressDialog.show()
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.pwd)
        val name = findViewById<EditText>(R.id.name).text.toString()

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful) {
                    progressDialog.setMessage("Saving user info...")
                    val currentUser = auth.currentUser
                    val uid = currentUser?.uid
                    val email = findViewById<EditText>(R.id.email).text.toString()
                    val password = findViewById<EditText>(R.id.pwd).text.toString()
                    val timestamp: String = System.currentTimeMillis().toString()
                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["uid"] = uid.toString()
                    hashMap["email"] = email
                    hashMap["password"] = password
                    hashMap["name"] = name
                    hashMap["profileImage"] = ""
                    hashMap["userType"] = "user"
                    hashMap["timestamp"] = timestamp

                    val ref = FirebaseDatabase.getInstance().getReference("Users")
                    ref.child(uid.toString()).setValue(hashMap)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Account created...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show()
                        }
                }
                else {
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                    progressDialog.dismiss()
                    if (task.exception is com.google.firebase.auth.FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginScreen::class.java)
                        startActivity(intent)
                    }
                    else {
                        email.error = "Please enter a valid email"
                        email.requestFocus()
                    }
                }
            }
    }
}