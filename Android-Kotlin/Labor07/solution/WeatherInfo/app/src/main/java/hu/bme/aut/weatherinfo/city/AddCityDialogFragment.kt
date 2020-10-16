package hu.bme.aut.weatherinfo.city

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import hu.bme.aut.weatherinfo.R

class AddCityDialogFragment : AppCompatDialogFragment() {
    private var listener: AddCityDialogListener? = null
    private var editText: EditText? = null

    interface AddCityDialogListener {
        fun onCityAdded(city: String)
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = if (activity is AddCityDialogListener) {
            activity as AddCityDialogListener?
        } else {
            throw RuntimeException("Activity must implement AddCityDialogListener interface!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_city)
            .setView(contentView)
            .setPositiveButton(R.string.ok) { _, _ -> }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as AlertDialog?
        if (dialog != null) {
            val positiveButton: Button = dialog.getButton(Dialog.BUTTON_POSITIVE) as Button
            positiveButton.setOnClickListener(View.OnClickListener {
                var wantToCloseDialog = false
                if (editText?.text?.isNotEmpty()!!) {
                    listener?.onCityAdded(editText?.text.toString())
                    wantToCloseDialog = true
                }
                if (wantToCloseDialog) {
                    dismiss()
                }
            })
        }
    }

    private val contentView: View
        get() {
            val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_new_city, null)
            editText = view.findViewById(R.id.NewCityDialogEditText)
            return view
        }
}