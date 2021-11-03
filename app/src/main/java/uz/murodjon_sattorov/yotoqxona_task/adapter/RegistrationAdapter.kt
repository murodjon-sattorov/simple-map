package uz.murodjon_sattorov.yotoqxona_task.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author Murodjon
 * @date 10/27/2021
 * @project Yotoqxona-task
 */
class RegistrationAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var fragmentArrayList = ArrayList<Fragment>()
    var stringArrayList = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getCount(): Int {
        return stringArrayList.size
    }

    fun addPagerFragment(fragment: Fragment, s: String) {
        fragmentArrayList.add(fragment)
        stringArrayList.add(s)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return stringArrayList[position]
    }
}