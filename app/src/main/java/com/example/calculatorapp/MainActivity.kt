package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.view.isGone
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var lastNumeric = false
    var stateError = false
    var lastDOt = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.textView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 37.5f)
    }


    fun onDigitClick(view: View) {

        if(stateError){

            binding.textView.text = (view as Button).text
            stateError = false
        } else {
            binding.textView.append((view as Button).text)
        }
        lastNumeric =true
        onEqual()
    }


    fun onAllclearClick(view: View) {

        binding.textView.text = ""
        binding.textView2.text = ""
        lastNumeric = false
        stateError = false
        lastDOt = false
        binding.textView2.visibility = View.GONE
    }


    fun onOperatorClick(view: View) {

        if(!stateError && lastNumeric) {

            binding.textView.append((view as Button).text)
            lastDOt = false
            lastNumeric = false
            onEqual()


        }
    }


    fun onBackClick(view: View) {

        binding.textView.text = binding.textView.text.toString().dropLast(1)

        try {
            val lastChar = binding.textView.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }
        } catch(e: Exception) {
            binding.textView2.text = ""
            binding.textView2.visibility = View.GONE
            Log.e("last char error", e.toString())
        }
    }


    fun onClearClick(view: View) {

        binding.textView.text = ""
        lastNumeric = false
    }

    fun onEqual() {

        if(lastNumeric && !stateError){

            val txt = binding.textView.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {

                val result = expression.evaluate()
                var str = result.toString()

                if(str.endsWith(".0")) {
                    str = str.replace(".0","")
                }


                binding.textView2.visibility = View.VISIBLE

                binding.textView2.text = "=" + str
            } catch (ex: java.lang.ArithmeticException){

                Log.e("evaluate error", ex.toString())
                binding.textView2.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }

    }


}