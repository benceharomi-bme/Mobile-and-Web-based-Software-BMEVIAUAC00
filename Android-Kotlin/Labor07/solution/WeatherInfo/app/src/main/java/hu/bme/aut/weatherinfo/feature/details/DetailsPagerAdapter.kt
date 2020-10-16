package hu.bme.aut.weatherinfo.feature.details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hu.bme.aut.weatherinfo.R

class DetailsPagerAdapter(fragmentManager: FragmentManager, private val context: Context) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailsMainFragment()
            1 -> DetailsMoreFragment()
            else -> throw IllegalStateException("There is no fragment with this position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.main)
            1 -> context.getString(R.string.details)
            else -> ""
        }
    }

    override fun getCount(): Int = 2

}