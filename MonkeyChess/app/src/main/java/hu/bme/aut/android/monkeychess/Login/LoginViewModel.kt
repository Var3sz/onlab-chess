package hu.bme.aut.android.monkeychess.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")

    fun setEmail(value: String) {
        email.value = value
    }

    fun setPassword(value: String) {
        password.value = value
    }

    fun getEmail(): String? {
        return  email.value
    }

    fun getPassword(): String?{
        return  email.value
    }
}

