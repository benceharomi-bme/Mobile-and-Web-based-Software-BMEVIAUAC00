package hu.bme.aut.workplaceapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import hu.bme.aut.workplaceapp.R
import hu.bme.aut.workplaceapp.data.DataManager
import kotlinx.android.synthetic.main.profile_detail.*

class DetailsProfileFragment : Fragment(R.layout.profile_detail){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val person = DataManager.person
        tvId.text = person.id
        tvSSN.text = person.socialSecurityNumber
        tvTaxId.text = person.taxId
        tvRegistrationId.text = person.registrationId
    }
}