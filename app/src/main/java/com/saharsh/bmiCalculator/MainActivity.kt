package com.saharsh.bmiCalculator

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.kevalpatel2106.rulerpicker.RulerValuePicker
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Shader.TileMode
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    lateinit var weightPicker : RulerValuePicker
    lateinit var heightPicker : RulerValuePicker
    lateinit var calculate : Button
    lateinit var male_btn : Button
    lateinit var female_btn : Button
    lateinit var weight_et : EditText
    lateinit var height_et : EditText
    lateinit var bmi_tv : TextView


    var weight : Double = 0.0
    var height : Double = 0.0
    var result : Double = 0.0
    var gender : Int = 0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        listeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(){
        bmi_tv =  findViewById(R.id.bmi_tv)

        male_btn= findViewById(R.id.male_btn)
        female_btn= findViewById(R.id.female_btn)

        weightPicker = findViewById(R.id.weight_picker)
        heightPicker = findViewById(R.id.height_picker)

        weight_et = findViewById(R.id.weight_et)
        height_et = findViewById(R.id.height_et)
        calculate = findViewById(R.id.calculate_btn)


        weightPicker.selectValue(60)
        heightPicker.selectValue(175)
        weight_et.setText(weightPicker.currentValue.toString())
        height_et.setText(heightPicker.currentValue.toString())

        val textShader = LinearGradient(
            0f, 0f, 0f, bmi_tv.textSize,
            intArrayOf(getColor(R.color.end_color), getColor(R.color.start_color)),
            floatArrayOf(0f, 1f), TileMode.CLAMP
        )
        bmi_tv.getPaint().setShader(textShader)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    fun listeners(){

        male_btn.setOnClickListener(View.OnClickListener {

            gender = 0
            male_btn.setBackgroundResource(R.drawable.selected_round)
            male_btn.setTextColor(getColor(R.color.white))

            female_btn.setBackgroundResource(R.drawable.unselected_round)
            female_btn.setTextColor(getColor(R.color.colorAccent))

        })
        female_btn.setOnClickListener(View.OnClickListener {
            gender = 1
            male_btn.setBackgroundResource(R.drawable.unselected_round)
            male_btn.setTextColor(getColor(R.color.colorAccent))

            female_btn.setBackgroundResource(R.drawable.selected_round)
            female_btn.setTextColor(getColor(R.color.white))

        })

        weightPicker.setValuePickerListener(object : RulerValuePickerListener{
            override fun onValueChange(selectedValue: Int) {
                weight = selectedValue.toDouble()
                weight_et.setText(selectedValue.toString())
                if(selectedValue ==0){
                    error_tv.visibility = View.VISIBLE
                }
                else{
                    if(heightPicker.currentValue!=0){
                    error_tv.visibility = View.INVISIBLE}
                }
            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                weight_et.setText(selectedValue.toString())

            }

        })

        heightPicker.setValuePickerListener(object : RulerValuePickerListener{
            override fun onValueChange(selectedValue: Int) {
                height = selectedValue.toDouble()
                height_et.setText(selectedValue.toString())

                if(selectedValue ==0){
                    error_tv.visibility = View.VISIBLE
                }
                else{
                    if(weightPicker.currentValue!=0){
                    error_tv.visibility = View.INVISIBLE}

                }

            }

            override fun onIntermediateValueChange(selectedValue: Int) {
                height_et.setText(selectedValue.toString())
            }

        })

       /* height_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                height = p0.toString().trim().toDouble()
                heightPicker.selectValue(p0.toString().trim().toInt())
            }
        })

        weight_et.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                weight = p0.toString().trim().toDouble()
                weightPicker.selectValue(p0.toString().trim().toInt())
            }
        })*/



        calculate.setOnClickListener(View.OnClickListener {

            if(height>0 && weight>0){
                if(gender==0){
                    bmiCalMale()
                }
                else if(gender == 1){
                    bmiCalMale()
                }
            showBottomSheet()
            }

        } )
    }

    fun bmiCalMale(){
        result = ((weight/(height*height))*10000)
    }
    fun bmiCalFemale(){
        result = ((weight/(height*height))*10000)
    }

    fun showBottomSheet(){
        val bottomSheetFragment = BMIScoreFragment(result, this)
        bottomSheetFragment.setCancelable(true)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

}
