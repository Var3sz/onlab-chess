package hu.bme.aut.android.monkeychess.Register

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel {
    private var lastname: MutableLiveData<String> = MutableLiveData("")
    private var email: MutableLiveData<String> = MutableLiveData("")
    private var username: MutableLiveData<String> = MutableLiveData("")
    private var password: MutableLiveData<String> = MutableLiveData("")
    private var confrimpassword: MutableLiveData<String> = MutableLiveData("")

    private lateinit var auth: FirebaseAuth

    fun registerUser(email: String, password: String){

    }

    fun settFullname(_fullname: String){
        lastname.value = _fullname
    }

    fun setEmail(_email: String) {
        email.value = _email
    }

    fun setUsername(_username: String){
        username.value = _username
    }

    fun setPassword(_password: String) {
        password.value = _password
    }

    fun setConfirmpassword(_confirmpassword: String){
        confrimpassword.value = _confirmpassword
    }

    fun getFullname(): String?{
        return lastname.value
    }

    fun getEmail(): String?{
        return email.value
    }

    fun getUsername(): String?{
        return username.value
    }

    fun getPassword(): String?{
        return password.value
    }


}