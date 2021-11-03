package uz.murodjon_sattorov.yotoqxona_task.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import uz.murodjon_sattorov.firebasechatapp.dialogs.LoadProgressDialog
import uz.murodjon_sattorov.yotoqxona_task.activities.MainActivity
import uz.murodjon_sattorov.yotoqxona_task.activities.ProfileActivity
import uz.murodjon_sattorov.yotoqxona_task.databinding.FragmentSignUpBinding
import kotlin.random.Random


/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author Murodjon
 * @date 10/27/2021
 * @project Yotoqxona-task
 */
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    private lateinit var dialog: LoadProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        dialog = LoadProgressDialog(requireContext())

        //animation block
        run {
            binding.textInputLayoutFullName.translationX = 800F
            binding.textInputLayoutUniversitetName.translationX = 800F
            binding.textInputLayoutGroupName.translationX = 800F
            binding.textInputLayoutEmail.translationX = 800F
            binding.textInputLayoutPassword.translationX = 800F
            binding.signUpBtn.translationX = 800F

            binding.textInputLayoutFullName.alpha = 0F
            binding.textInputLayoutUniversitetName.alpha = 0F
            binding.textInputLayoutGroupName.alpha = 0F
            binding.textInputLayoutEmail.alpha = 0F
            binding.textInputLayoutPassword.alpha = 0F
            binding.signUpBtn.alpha = 0F

            binding.textInputLayoutFullName.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(300).start()
            binding.textInputLayoutUniversitetName.animate().translationX(0F).alpha(1F)
                .setDuration(800)
                .setStartDelay(400).start()
            binding.textInputLayoutGroupName.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(500).start()
            binding.textInputLayoutEmail.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(600)
                .start()
            binding.textInputLayoutPassword.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(700)
                .start()
            binding.signUpBtn.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(800)
                .start()

        }

        binding.signUpBtn.setOnClickListener {
            dialog.loadDialog()
            checkFields(
                binding.inputFulName.text.toString(),
                binding.inputUniverName.text.toString(),
                binding.inputGroupName.text.toString(),
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        }

        return binding.root
    }

    private fun checkFields(
        fullName: String,
        univerName: String,
        groupName: String,
        email: String,
        password: String
    ) {
        if (fullName.isNotEmpty() && fullName.length > 1) {
            if (univerName.isNotEmpty()) {
                if (groupName.isNotEmpty()) {
                    if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                            .matches()
                    ) {
                        if (password.length >= 6) {
                            dismissKeyboard()
                            registerUser(fullName, univerName, groupName, email, password)
                        } else {
                            binding.inputPassword.error = "At least 6 characters"
                            binding.inputPassword.requestFocus()
                        }
                    } else {
                        binding.inputEmail.error = "Invalid Email"
                        binding.inputEmail.requestFocus()
                    }
                } else {
                    binding.inputGroupName.error = "Empty space"
                    binding.inputGroupName.requestFocus()
                }
            } else {
                binding.inputUniverName.error = "Empty space"
                binding.inputUniverName.requestFocus()
            }
        } else {
            binding.inputFulName.error = "Empty space"
            binding.inputFulName.requestFocus()
        }
    }

    private fun registerUser(
        fullName: String,
        univerName: String,
        groupName: String,
        email: String,
        password: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = auth.currentUser!!
                    val userId: String = user.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap["id"] = userId
                    hashMap["userFullName"] = fullName
                    hashMap["userUniversitetName"] = univerName
                    hashMap["userGroupName"] = groupName
                    hashMap["userPassword"] = password
                    hashMap["userStudentId"] = "200" + Random.nextInt(1000000, 9999999).toString()
                    hashMap["userViloyat"] = ""
                    hashMap["userTuman"] = ""
                    hashMap["userManzil"] = ""
                    hashMap["userPhoneNumber"] = ""

                    reference.setValue(hashMap).addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            dialog.dismissDialog()
                            val intent = Intent(context, ProfileActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }

                } else {
                    view?.let {
                        dialog.dismissDialog()
                        Snackbar.make(
                            it,
                            "Could not register. Please try again!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }


    private fun dismissKeyboard() {
        val imm: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
    }

}