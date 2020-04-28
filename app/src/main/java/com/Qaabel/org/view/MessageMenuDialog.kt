package com.Qaabel.org.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.Qaabel.org.R
import com.Qaabel.org.interfaces.DialogItemClicked
import kotlinx.android.synthetic.main.message_menu.*


class MessageMenuDialog(var cxt:Context) :Dialog(cxt) {
    var dialogItemClicked:DialogItemClicked?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.requestFeature(Window.FEATURE_NO_TITLE);
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val wlp=window?.attributes as WindowManager.LayoutParams
        wlp.gravity = Gravity.TOP or Gravity.END
        wlp.x=20
        wlp.y=20
        window?.attributes=wlp
        setContentView(R.layout.message_menu)
        actions()


    }
  fun actions(){
      selectAllTxt.setOnClickListener {
          // notify fragment dialog clicked
          dialogItemClicked?.ItemClicked("SELECT_ALL")
          dismiss()
      }
      markUnreadTxt.setOnClickListener {
          dialogItemClicked?.ItemClicked("MARK UNREAD")
          dismiss()
      }

  }
}