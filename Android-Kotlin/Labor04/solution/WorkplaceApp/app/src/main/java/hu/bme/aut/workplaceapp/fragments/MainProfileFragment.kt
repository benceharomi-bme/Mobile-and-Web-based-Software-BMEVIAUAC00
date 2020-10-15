package hu.bme.aut.workplaceapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.aut.workplaceapp.R
import hu.bme.aut.workplaceapp.data.DataManager
import kotlinx.android.synthetic.main.profile_main.*

class MainProfileFragment : Fragment(R.layout.profile_main){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val person = DataManager.person
        tvName.text = person.name
        tvEmail.text = person.email
        tvAddress.text = person.address
    }
}