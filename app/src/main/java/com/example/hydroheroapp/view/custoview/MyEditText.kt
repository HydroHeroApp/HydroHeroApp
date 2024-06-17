package com.example.hydroheroapp.view.custoview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.hydroheroapp.R

class MyEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when (id) {

                    R.id.ed_register_username, R.id.ed_register_username -> {
                        if (s.length < 5) {
                            error = context.getString(R.string.invalid_username)
                        } else {
                            error = null
                        }
                    }

                    R.id.ed_login_email, R.id.ed_register_email -> {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                            error = context.getString(R.string.invalid_email)
                        } else {
                            error = null
                        }
                    }

                    R.id.ed_login_password, R.id.ed_register_password -> {
                        if (s.length < 8) {
                            error = context.getString(R.string.invalid_password)
                        } else {
                            error = null
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        TODO("Not yet implemented")
    }
}