package uz.murodjon_sattorov.yotoqxona_task.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import uz.murodjon_sattorov.yotoqxona_task.R
import uz.murodjon_sattorov.yotoqxona_task.adapter.RegistrationAdapter
import uz.murodjon_sattorov.yotoqxona_task.databinding.ActivityRegistrationBinding
import uz.murodjon_sattorov.yotoqxona_task.fragment.SignInFragment
import uz.murodjon_sattorov.yotoqxona_task.fragment.SignUpFragment

class RegistrationActivity : AppCompatActivity() {

    private lateinit var registrationBinding: ActivityRegistrationBinding

    private var adapter: RegistrationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        registrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(registrationBinding.root)

        adapter = RegistrationAdapter(supportFragmentManager)

        adapter!!.addPagerFragment(SignUpFragment(), getString(R.string.ro_yhatdan_o_tish))
        adapter!!.addPagerFragment(SignInFragment(), getString(R.string.tizimga_kirish))


        registrationBinding.viewPager.adapter = adapter
        registrationBinding.tabLayout.setupWithViewPager(registrationBinding.viewPager)

    }
}