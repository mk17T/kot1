package com.duck.kot1

/*
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val isValid = MediatorLiveData<Boolean>()

    init {
        val emailRegex = Regex(pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        val passwordRegex = Regex(pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")

        isValid.addSource(email) {
            isValid.value = emailRegex.matches(it)
        }
        isValid.addSource(password) {
            isValid.value = passwordRegex.matches(it)
        }
    }
}


 */