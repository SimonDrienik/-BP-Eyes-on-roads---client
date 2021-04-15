package com.bp.digitalizacia_spravy_ciest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bp.digitalizacia_spravy_ciest.R


class AddCommentDialogFragment: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.add_comment_activity, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)



    }

}