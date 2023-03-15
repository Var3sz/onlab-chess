package hu.bme.aut.android.monkeychess.Login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")

    private lateinit var auth: FirebaseAuth

    fun loginUser(){

    }

    fun isLoginInputValid(): String{
        return ""
    }

    fun setEmail(_email: String) {
        email.value = _email
    }

    fun setPassword(_password: String) {
        password.value = _password
    }

    fun getEmail(): String? {
        return  email.value
    }

    fun getPassword(): String?{
        return  email.value
    }
}

