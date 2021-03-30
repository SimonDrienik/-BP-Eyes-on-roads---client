package com.bp.digitalizacia_spravy_ciest.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bp.digitalizacia_spravy_ciest.R
import java.time.LocalDate

class HistoryAdapter(private val context: Activity, private val name: Array<String?>, private val dates: Array<LocalDate?>, pocet: Int)
    : ArrayAdapter<String>(context, R.layout.history_adapter, name) {


    var stringDates = arrayOfNulls<String>(pocet)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewHolder")

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var i = 0
        for (item in dates){
            stringDates[i] = item.toString()
            i += 1
        }

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.history_adapter, null, true)

        val nameText = rowView.findViewById(R.id.name) as TextView
        val date = rowView.findViewById(R.id.date) as TextView

        nameText.text = name[position]
        date.text = stringDates[position]

        return rowView
    }

}