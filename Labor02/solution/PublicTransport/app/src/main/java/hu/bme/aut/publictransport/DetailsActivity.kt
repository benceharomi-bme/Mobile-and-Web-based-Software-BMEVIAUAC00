package hu.bme.aut.publictransport

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class DetailsActivity : AppCompatActivity() {

    companion object {
        const val KEY_TRANSPORT_TYPE = "KEY_TRANSPORT_TYPE"
    }

    private var daysCount = 1
    private var discountMultiplier = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val transportType = this.intent.getIntExtra(KEY_TRANSPORT_TYPE, -1)
        tvTicketType.text = getTypeString(transportType)
        val dailyPrice = getDailyPrice(transportType)
        tvPrice.text = "$dailyPrice"

        btnPurchase.setOnClickListener {
            if (daysCount < 0) {
                val toast = Toast.makeText(
                    applicationContext,
                    "End date should be later than start date",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            } else {
                val typeString = getTypeString(transportType)
                val dateString = getDateFrom(dpStartDate) + " - " + getDateFrom(dpEndDate)

                val intent = Intent(this, PassActivity::class.java)
                intent.putExtra(PassActivity.KEY_TYPE_STRING, typeString)
                intent.putExtra(PassActivity.KEY_DATE_STRING, dateString)
                startActivity(intent)
            }
        }

        val today = Calendar.getInstance()
        dpStartDate.init(today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            OnDateChangedListener { dpStartDate, _, _, _ ->
                daysCount = countDays(dpStartDate, dpEndDate)
                tvPrice.text = "${calculatePrice(daysCount, dailyPrice)}"
            })
        dpEndDate.init(today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            OnDateChangedListener { dpEndDate, _, _, _ ->
                daysCount = countDays(dpStartDate, dpEndDate)
                tvPrice.text = "${calculatePrice(daysCount, dailyPrice)}"
            })

        rgPriceCategory.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById(checkedId) as RadioButton
            when (checkedRadioButton.text) {
                rbFullPrice.text -> {
                    discountMultiplier = 1 - (Discount.NO_DISCOUNT.percentage.toDouble() / 100)
                }
                rbSenior.text -> {
                    discountMultiplier = 1 - (Discount.SENIOR.percentage.toDouble() / 100)
                }
                rbPublicServant.text -> {
                    discountMultiplier = 1 - (Discount.PUBLIC_SERVANT.percentage.toDouble() / 100)
                }
            }
            tvPrice.text = "${calculatePrice(daysCount, dailyPrice)}"
        }
    }

    private fun getTypeString(transportType: Int): String? {
        return when (transportType) {
            ListActivity.TYPE_BUS -> "Bus pass"
            ListActivity.TYPE_TRAIN -> "Train pass"
            ListActivity.TYPE_BIKE -> "Bike pass"
            ListActivity.TYPE_BOAT -> "Boat pass"

            else -> "Unknown pass type"
        }
    }

    private fun getDailyPrice(transportType: Int): Int {
        return when (transportType) {
            ListActivity.TYPE_BIKE -> DailyPrice.BIKE.amount
            ListActivity.TYPE_BUS -> DailyPrice.BIKE.amount
            ListActivity.TYPE_TRAIN -> DailyPrice.TRAIN.amount
            ListActivity.TYPE_BOAT -> DailyPrice.BOAT.amount

            else -> throw Error()
        }
    }

    private fun getDateFrom(picker: DatePicker): String? {
        return String.format(
            Locale.getDefault(), "%04d.%02d.%02d.",
            picker.year, picker.month + 1, picker.dayOfMonth
        )
    }

    private fun countDays(startDatePicker: DatePicker, endDatePicker: DatePicker): Int {
        val startTime = Calendar.getInstance()
        startTime.set(startDatePicker.year, startDatePicker.month, startDatePicker.dayOfMonth)
        val endTime = Calendar.getInstance()
        endTime.set(endDatePicker.year, endDatePicker.month, endDatePicker.dayOfMonth)
        return TimeUnit.MILLISECONDS.toDays(endTime.timeInMillis - startTime.timeInMillis)
            .toInt() + 1
    }

    private fun calculatePrice(daysCount: Int, dailyPrice: Int): Int {
        return if (daysCount > 0) ((daysCount * dailyPrice * discountMultiplier).roundToInt())
        else 0
    }
}

enum class Discount(val percentage: Int) {
    SENIOR(90),
    PUBLIC_SERVANT(50),
    NO_DISCOUNT(0)
}

enum class DailyPrice(val amount: Int) {
    BIKE(700),
    BUS(1000),
    TRAIN(1500),
    BOAT(2500)
}