package hu.bme.aut.workplaceapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.workplaceapp.data.DataManager
import hu.bme.aut.workplaceapp.fragments.DatePickerDialogFragment
import kotlinx.android.synthetic.main.activity_holiday.*
import java.util.*

class HolidayActivity : AppCompatActivity(), DatePickerDialogFragment.OnDateSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)

        btnTakeHoliday.setOnClickListener {
            DatePickerDialogFragment().show(supportFragmentManager, "DATE_TAG")
        }
        loadHolidays()
    }

    private fun loadHolidays() {
        val entries = listOf(
            PieEntry(DataManager.holidays.toFloat(), "Taken"),
            PieEntry(DataManager.remainingHolidays.toFloat(), "Remaining")
        )

        val dataSet = PieDataSet(entries, "Holidays")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = PieData(dataSet)
        chartHoliday.data = data
        chartHoliday.invalidate()
    }

    override fun onDateSelected(year: Int, month: Int, day: Int) {
        val numHolidays = DataManager.holidays
        if (!isDateValid(year, month, day)) {
            Snackbar.make(holidayLayout,
                getString(R.string.warn_message),
                BaseTransientBottomBar.LENGTH_SHORT
            ).show()
        } else if (DataManager.remainingHolidays > 0) {
            DataManager.holidays = numHolidays + 1
        }
        if (DataManager.remainingHolidays == 0) {
            btnTakeHoliday.isEnabled = false
        }
        loadHolidays()
    }

    fun isDateValid(year: Int, month: Int, day: Int): Boolean {
        val today = Calendar.getInstance()
        val pickedDate = Calendar.getInstance()
        pickedDate.set(year, month, day)
        return pickedDate > today
    }
}