package com.duck.kot1

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: TextView
    private lateinit var edtName: TextView
    private var atoz: TextView? = null
    private var AtoZ:TextView? = null
    private var num:TextView? = null
    private var charcount:TextView? = null
    private var passTick:TextView? = null
    private lateinit var edtPassword: TextView
    private lateinit var btnSignUp: Button
    private lateinit var btnLogin: Button

    private var isAllFieldsChecked = false
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = Firebase.auth
        edtEmail = findViewById(R.id.edt_email)
        edtName = findViewById(R.id.edt_name)
        atoz = findViewById(R.id.atoz)
        AtoZ = findViewById(R.id.AtoZ)
        num = findViewById(R.id.num)
        charcount = findViewById(R.id.charcount)
        passTick = findViewById(R.id.passTick)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogin = findViewById(R.id.btnLogin)


        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            isAllFieldsChecked = CheckAllFields()
            if(isAllFieldsChecked){
                signUp(name , email , password)
            }

        }

        btnLogin.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
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
        /*
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if(currentUser != null){
           // reload();
        }
         */
    }

    private fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun signUp(name : String, email : String, password : String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    //jump to main activity
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email, uid))

    }

    private fun CheckAllFields(): Boolean {
        var emailToText = edtEmail.text.toString()
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$")
        val nameRegex = Regex("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")

        if (edtName!!.length() == 0) {
            edtName!!.error = "This field is required"
            return false
        } else if(!edtName.text.toString().matches(nameRegex)) {
            edtName!!.error = "Name should contain only alphabets(a-z)"
            return false
        }

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