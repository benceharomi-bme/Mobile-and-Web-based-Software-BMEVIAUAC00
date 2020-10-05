package hu.bme.aut.workplaceapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.workplaceapp.fragments.DetailsProfileFragment
import hu.bme.aut.workplaceapp.fragments.MainProfileFragment

class ProfilePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = when(position){
        0 -> MainProfileFragment()
        1 -> DetailsProfileFragment()
        else -> MainProfileFragment()
    }

    override fun getCount() : Int = NUM_PAGES

    companion object{
        const val NUM_PAGES = 2
    }
}