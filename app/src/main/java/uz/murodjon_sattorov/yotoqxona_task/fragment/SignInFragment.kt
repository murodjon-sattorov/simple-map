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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import uz.murodjon_sattorov.firebasechatapp.dialogs.LoadProgressDialog
import uz.murodjon_sattorov.yotoqxona_task.activities.ProfileActivity
import uz.murodjon_sattorov.yotoqxona_task.databinding.FragmentSignInBinding
import uz.murodjon_sattorov.yotoqxona_task.model.User

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author Murodjon
 * @date 10/27/2021
 * @project Yotoqxona-task
 */
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var reference: DatabaseReference

    private lateinit var dialog: LoadProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        run {
            binding.textInputLayoutStudentId.translationX = 800F
            binding.textInputLayoutEmail.translationX = 800F
            binding.textInputLayoutPassword.translationX = 800F
            binding.signInBtn.translationX = 800F

            binding.textInputLayoutStudentId.alpha = 0F
            binding.textInputLayoutEmail.alpha = 0F
            binding.textInputLayoutPassword.alpha = 0F
            binding.signInBtn.alpha = 0F

            binding.textInputLayoutStudentId.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(300).start()
            binding.textInputLayoutEmail.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(400).start()
            binding.textInputLayoutPassword.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(500).start()
            binding.signInBtn.animate().translationX(0F).alpha(1F).setDuration(800)
                .setStartDelay(600)
                .start()

        }

        binding.signInBtn.setOnClickListener {
            checkFields(
                binding.inputStudentId.text.toString(),
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        dialog = LoadProgressDialog(requireContext())
    }

    private fun checkFields(
        studentId: String,
        email: String,
        password: String,
    ) {
        if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            if (password.length >= 6) {

                dismissKeyboard()
                dialog.loadDialog()
                loginUser(studentId, email, password)

            } else {
                binding.inputPassword.error = "At least 6 characters"
                binding.inputPassword.requestFocus()
            }
        } else {
            binding.inputEmail.error = "Invalid Email"
            binding.inputEmail.requestFocus()
        }
    }

    private fun loginUser(studentId: String, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dialog.dismissDialog()

                    firebaseUser = auth.currentUser

                    reference =
                        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (studentId == user!!.userStudentId) {
                                startActivity(Intent(context, ProfileActivity::class.java))
                            } else {
                                dialog.dismissDialog()
                                view?.let {
                                    binding.inputStudentId.error = "Invalid ID"
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                } else {
                    dialog.dismissDialog()
                    view?.let {
                        Snackbar.make(it, "Authentication failed", Snackbar.LENGTH_SHORT).show()
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