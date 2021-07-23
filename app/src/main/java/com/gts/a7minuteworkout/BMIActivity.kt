package com.gts.a7minuteworkout

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_finish.*
import java.math.RoundingMode
import java.math.BigDecimal

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_BMI_activity)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.title = "CALCULATE BMI"
            }
            toolbar_BMI_activity.setNavigationOnClickListener {
                onBackPressed()
            }

            btnCalculateUnits.setOnClickListener {
                if (currentVisibleView.equals(METRIC_UNITS_VIEW)){
                    if (validateMetricUnits()){
                        val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                        val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                        val bmi = weightValue/ (heightValue*heightValue)
                        displayBMIResult(bmi)
                    }
                    else{
                        Toast.makeText(this@BMIActivity,
                            "Please enter valid values", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    if (validateUSUnits()) {

                        val usUnitHeightValueFeet: String =
                            etUsUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                        val usUnitHeightValueInch: String =
                            etUsUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                        val usUnitWeightValue: Float = etUsUnitWeight.text.toString()
                            .toFloat() // Weight value entered in EditText component.

                        // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                        val heightValue =
                            usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                        // This is the Formula for US UNITS result.
                        // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                        val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                        displayBMIResult(bmi) // Displaying the result into UI
                    } else {
                        Toast.makeText(
                            this@BMIActivity,
                            "Please enter valid values.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }

        makeVisibleMetricUnitsView()
        rgUnits.setOnCheckedChangeListener{ _, checkedId ->

            if (checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.GONE
        tilMetricUnitWeight.visibility = View.GONE

        etUsUnitWeight.text!!.clear()
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        llDiplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUnitsView(){
         currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitHeight.visibility = View.VISIBLE
        tilMetricUnitWeight.visibility = View.VISIBLE

        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()

        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE

        llDiplayBMIResult.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        llDiplayBMIResult.visibility = View.VISIBLE

        /*
        tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE */

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if (etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if (etMetricUnitHeight.text.toString().isEmpty())
            isValid = false

        return isValid
    }

    private fun validateUSUnits(): Boolean{
        var isValid = true

        when {
            etUsUnitWeight.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightFeet.text.toString().isEmpty() -> isValid = false
            etUsUnitHeightInch.text.toString().isEmpty() -> isValid = false
        }

        return isValid
    }
}