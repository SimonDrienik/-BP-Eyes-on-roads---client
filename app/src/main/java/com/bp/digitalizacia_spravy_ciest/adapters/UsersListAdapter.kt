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
import java.math.BigInteger
import java.time.LocalDate

class UsersListAdapter (private val context: Activity, private val names: Array<String?>,
                        private val roles: Array<BigInteger?>, private val dates: Array<LocalDate?>,
                        private val emails: Array<String?>, private val pocet: Int)
    : ArrayAdapter<String>(context, R.layout.adapter_users_list, emails)
{

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
        val rowView = inflater.inflate(R.layout.adapter_users_list, null, true)

        val name = rowView.findViewById(R.id.fullName) as TextView
        val role = rowView.findViewById(R.id.role) as TextView
        val date = rowView.findViewById(R.id.dateUser) as TextView
        val email = rowView.findViewById(R.id.mail) as TextView

        var rola = ""

        if (roles[position]!!.toInt() == 1)
        {
            rola = "Verejnosť"
        }
        else if (roles[position]!!.toInt() == 3)
        {
            rola = "Administrátor"
        }
        else if (roles[position]!!.toInt() == 4)
        {
            rola = "Dispečer"
        }
        else if (roles[position]!!.toInt() == 5)
        {
            rola = "Manažér"
        }

        name.text = names[position].toString()
        role.text = rola
        email.text = emails[position].toString()
        date.text = stringDates[position]


        return rowView
    }

}