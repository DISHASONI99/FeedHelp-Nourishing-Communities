package com.example.a01

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
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

class LoginScreen : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)


        // get input from login here
        val email = findViewById<EditText>(R.id.logemail)
        val password = findViewById<EditText>(R.id.log_pwd)


        //Google
        val signInButton = findViewById<LinearLayout>(R.id.gsignin)
        signInButton.setOnClickListener {
            signIn()
        }

        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener{
            val email = findViewById<EditText>(R.id.logemail)
            val password = findViewById<EditText>(R.id.log_pwd)

            if(email.text.toString().isEmpty())
            {
                email.error = "Please Enter an email"
                email.requestFocus()
                return@setOnClickListener
            }
            else if(!email.text.toString().contains("@")) {
                email.error = "Please enter a valid email"
                email.requestFocus()
                return@setOnClickListener
            }
            else if(password.text.toString().isEmpty())
            {
                password.error = "Please Enter a password"
                password.requestFocus()
                return@setOnClickListener
            }
            else if(password.text.toString().length < 8) {
                password.error = "Password should be at least 8 characters long"
                password.requestFocus()
                return@setOnClickListener
            }
            else{
                loginUser()
            }
        }


        val register = findViewById<TextView>(R.id.register)
        register.setOnClickListener {
            startActivity(Intent(this@LoginScreen, RegisterActivity::class.java))
            finish()
        }
        val forgotPassword = findViewById<TextView>(R.id.fpwd)
        forgotPassword.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

    }

    private fun loginUser() {
        progressDialog.setMessage("Logging In...")
        progressDialog.show()
        val email = findViewById<EditText>(R.id.logemail)
        val password = findViewById<EditText>(R.id.log_pwd)

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnSuccessListener(this) {
                Toast.makeText(this@LoginScreen, "Signed in", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginScreen, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener(this){
                progressDialog.dismiss()
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        progressDialog.setMessage("Logging In...")
        progressDialog.show()
        if (requestCode == RC_SIGN_IN) {
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
                                Toast.makeText(this@LoginScreen, "Login Success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginScreen, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else {
                                userRef.setValue(hashMap)
                                    .addOnSuccessListener {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LoginScreen, "Login Success", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@LoginScreen, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        progressDialog.dismiss()
                                        Toast.makeText(this@LoginScreen, "SignIn Failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            progressDialog.dismiss()
                            Toast.makeText(this@LoginScreen, "Error checking user data", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
                else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

}