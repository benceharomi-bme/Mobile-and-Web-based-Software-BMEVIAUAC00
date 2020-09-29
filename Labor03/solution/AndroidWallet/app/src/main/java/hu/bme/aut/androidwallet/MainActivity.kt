package hu.bme.aut.androidwallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.forEach
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.salary_row.view.*

class MainActivity : AppCompatActivity() {
    var sum = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                list_of_rows.removeAllViews()
                sum = 0
                summary_text.text = "$sum"
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save_button.setOnClickListener {
            if (salary_name.text.isEmpty() || salary_amount.text.isEmpty()) {
                Snackbar.make(
                    main_layout,
                    getString(R.string.warn_message),
                    BaseTransientBottomBar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val rowItem = LayoutInflater.from(this).inflate(R.layout.salary_row, null)
            rowItem.salary_direction_icon.setImageResource(
                if (expense_or_income.isChecked) {
                    sum -= salary_amount.text.toString().toInt()
                    R.drawable.expense
                } else {
                    sum += salary_amount.text.toString().toInt()
                    R.drawable.income
                }
            )
            rowItem.row_salary_name.text = salary_name.text.toString()
            rowItem.row_salary_amount.text = salary_amount.text.toString()
            list_of_rows.addView(rowItem)
            summary_text.text = "$sum"
        }
    }
}