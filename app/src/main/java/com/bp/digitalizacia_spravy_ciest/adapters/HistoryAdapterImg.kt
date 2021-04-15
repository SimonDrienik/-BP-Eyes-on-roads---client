package com.bp.digitalizacia_spravy_ciest.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bp.digitalizacia_spravy_ciest.R
import com.squareup.picasso.Picasso
import java.time.LocalDate

class HistoryAdapterImg (private val context: Activity, private val name: Array<String?>,
                         private val dates: Array<LocalDate?>, private val pocet: Int)
    : ArrayAdapter<String>(context, R.layout.history_adapter_img, name) {


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
        val rowView = inflater.inflate(R.layout.history_adapter_img, null, true)

        val foto = rowView.findViewById(R.id.fotoRieseniaHistory) as ImageView
        val date = rowView.findViewById(R.id.date) as TextView

        Picasso.get().load("http://147.175.204.24/"+name[position]).into(foto)
        date.text = stringDates[position]

        return rowView
    }

}