package com.bp.digitalizacia_spravy_ciest

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.time.LocalDate

class MyListAdapter(private val context: Activity, private val IDs: Array<BigInteger?>, private val categories: Array<String?>, private val dates: Array<LocalDate?>, private val pocet: Int)
    : ArrayAdapter<String>(context, R.layout.custom_list, categories)  {

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
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val category = rowView.findViewById(R.id.category) as TextView
        val date = rowView.findViewById(R.id.date) as TextView

        category.text = categories[position]
        date.text = stringDates[position]
        Log.d("TAG", "jajajajajajajajjajajajajajajajajajajajajajajajajajajajajajja")
        return rowView
    }
}