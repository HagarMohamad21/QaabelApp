package com.Qaabel.org.view

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import java.util.Calendar
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import com.Qaabel.org.R
import kotlinx.android.synthetic.main.date_dialog_layout.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DateDialog"
class DateDialog :DialogFragment(){

    private var mMonth = 0
    private  var mYear:Int = 0
    private  var mDay:Int = 0
    var isAgePrivate=false
    var dateString=""

    interface DateDialogDismissListener{
        fun onDismiss(isAgePrivate:Boolean,DateArray:IntArray)
    }
    lateinit var dateDialogDismissListener:DateDialogDismissListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun init(){
        if (isAgePrivate)
            datePrivacyBtn.text="Make public"
        else
            datePrivacyBtn.text="Make private"

        System.out.println("------------INIT --------------------------------"+dateString)
        if(dateString.trim()!=""){
            val strs = dateString.split("-").toTypedArray()
           yearEditTxt.setText(strs[0])
           monthEditTxt.setText(strs[1])
            dayEditTxt.setText(strs[2])
        }
    }

    private fun setListeners() {
      datePrivacyBtn.setOnClickListener {
          if(isAgePrivate){
              isAgePrivate=false
              datePrivacyBtn.text="Make private"
          }
          else{
              isAgePrivate=true
              datePrivacyBtn.text="Make public"
          }
      }
      saveBtn.setOnClickListener { checkDate() }
      openCalendarBtn.setOnClickListener { openCalendar() }
      dayEditTxt.addTextChangedListener(object :TextWatcher{
          override fun afterTextChanged(s: Editable?) {
             var text=s.toString()
              try{
                  if(text!=""&&text.toInt()>32){
                      dayEditTxt.text.clear()
                      dayEditTxt.error="Please enter valid day"
                  }
              }
              catch (e:Exception){
                  e.printStackTrace()
              }

          }

          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

          }

          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

          }
      })
      monthEditTxt.addTextChangedListener(object:TextWatcher{
          override fun afterTextChanged(s: Editable?) {
              var text=s.toString()
              try {
                  if(text!=""&&text.toInt()>12){
                      monthEditTxt.text.clear()
                      monthEditTxt.error="Please enter valid month"
                  }
              }
              catch (e:Exception){
                  e.printStackTrace()
              }

          }

          override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

          }

          override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          }
      })

    }

    private fun checkDate() {
        if(dayEditTxt.text.isEmpty()){
            dayEditTxt.error="This field is required"
            return
        }
        else if(monthEditTxt.text.isEmpty()){
            monthEditTxt.error="This field is required"
            return
        }
        else if(yearEditTxt.text.isEmpty()){
            yearEditTxt.error="This field is required"
            return
        }
        updateUserData()
    }

    private fun updateUserData() {
        if(dateDialogDismissListener!=null)
        {
            mDay=(dayEditTxt.text.toString()).toInt()
            mMonth=(monthEditTxt.text.toString()).toInt()
            mYear=(yearEditTxt.text.toString()).toInt()
            dateDialogDismissListener.onDismiss(isAgePrivate, intArrayOf(mDay, mMonth, mYear))
            Log.d(TAG, "updateUserData: -----------------------ON DISMISS------------------"+isAgePrivate)
            dialog.dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.date_dialog_layout,null,false)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE);
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        var b=arguments
        if(b!=null){

            dateString=b.getString("BIRTHDAY")
            isAgePrivate=b.getBoolean("AGE PRIVATE KEY")
            Log.d(TAG, "onCreateView: -----------------------------"+isAgePrivate)
            Log.d(TAG, "onCreateView: ")
        }


        return view
    }

    fun openCalendar(){
        dayEditTxt.error=null
        monthEditTxt.error=null
        yearEditTxt.error=null
        val mcurrentDate = Calendar.getInstance()
        mYear = mcurrentDate[Calendar.YEAR]
        mMonth = mcurrentDate[Calendar.MONTH]
        mDay = mcurrentDate[Calendar.DAY_OF_MONTH]

        val mDatePicker = DatePickerDialog(activity, OnDateSetListener { datepicker: DatePicker?, selectedyear: Int, selectedmonth: Int, selectedday: Int ->
            val myCalendar = Calendar.getInstance()
            myCalendar.time = Date()
            myCalendar[Calendar.YEAR] = selectedyear
            myCalendar[Calendar.MONTH] = selectedmonth
            myCalendar[Calendar.DAY_OF_MONTH] = selectedday
            val myFormat = "dd/MM/yyyy" //Change as you need
            val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
            mDay = selectedday
            mMonth = selectedmonth+1
            mYear = selectedyear
           System.out.println("----------------------------------------"+selectedmonth)
            dayEditTxt.setText(mDay.toString())
            monthEditTxt.setText(mMonth.toString())
            yearEditTxt.setText(mYear.toString())

        }, mYear, mMonth, mDay)
        mDatePicker.show()
    }
}