package hu.bme.aut.android.monkeychess.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    var emial: MutableLiveData<String> = MutableLiveData("")
    var passWord: MutableLiveData<String> = MutableLiveData("")

    fun setEmail(value: String) {
        emial.value = value
    }

    fun setPassWord(value: String) {
        passWord.value = value
    }

    fun getEmail(): String? {
        return  emial.value
    }

    fun getPassword(): String?{
        return  passWord.value
    }
}

