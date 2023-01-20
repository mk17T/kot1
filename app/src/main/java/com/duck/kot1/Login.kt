package com.duck.kot1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: TextView
    var atoz: TextView? = null
    var AtoZ:TextView? = null
    var num:TextView? = null
    var charcount:TextView? = null
    var passTick:TextView? = null
    private lateinit var edtPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    var isAllFieldsChecked = false
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edt_email)
        atoz = findViewById(R.id.atoz)
        AtoZ = findViewById(R.id.AtoZ)
        num = findViewById(R.id.num)
        charcount = findViewById(R.id.charcount)
        passTick = findViewById(R.id.passTick)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)


        btnSignUp.setOnClickListener{
            val intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked){
                login(email,password)
            }

        }

        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {
                // get the password when we start typing
                val password = edtPassword.text.toString()
                validatepass(password)
            }
            override fun afterTextChanged(editable: Editable?) {}
        })


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
           // reload();
        }
    }

    private fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun login(email : String, password : String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    // Log.d(TAG, "signInWithEmail:success")
                    // val user = mAuth.currentUser
                   // updateUI(user)
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }


    // function which checks all the text fields
    // when user clicks on the login button
    private fun CheckAllFields(): Boolean {
        /*
        if (ettName!!.length() == 0) {
            etName!!.error = "This field is required"
            return false
        }
         */
        var emailToText = edtEmail.text.toString()
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$")


        if (edtEmail!!.length() == 0) {
            edtEmail!!.error = "Email is required"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            edtEmail!!.error = "Enter proper email"
            return false
        }

        if (edtPassword!!.length() == 0) {
            edtPassword!!.error = "Password is required"
            return false
        } else if (!edtPassword.text.toString().matches(passwordRegex)) {
            edtPassword!!.error = "Password must contain:\n" +
                    "At least one lowercase letter [a-z]\n" +
                    "At least one uppercase letter [A-Z]\n" +
                    "At least one digit ([0-9]\n" +
                    "Minimum 8 characters"
            return false
        }

        // after all validation return true.
        return true
    }


    fun validatepass(password: String){
        var passtick = 0
        if (!password.matches(Regex(".*[a-z].*"))) {
            atoz?.setTextColor(Color.RED)
        } else {
            atoz?.setTextColor(Color.GREEN)
            passtick +=1
        }

        if (!password.matches(Regex(".*[A-Z].*"))){
            AtoZ?.setTextColor(Color.RED)
        } else {
            AtoZ?.setTextColor(Color.GREEN)
            passtick +=1
        }

        if (!password.matches(Regex(".*[0-9].*"))){
            num?.setTextColor(Color.RED)
        } else {
            num?.setTextColor(Color.GREEN)
            passtick +=1
        }

        if (password.length < 8) {
            charcount?.setTextColor(Color.RED)
        } else {
            charcount?.setTextColor(Color.GREEN)
            passtick +=1
        }
        if (passtick != 4) {
            passTick?.setTextColor(Color.RED)

            passTick?.text = "x"
        } else {

            passTick?.setTextColor(Color.GREEN)
            passTick?.text = "✓"
        }
    }

}





