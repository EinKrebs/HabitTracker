package com.example.habittracker

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.*


class AddActivity: Activity(), AdapterView.OnItemSelectedListener {
    private var habit: Habit? = null
    private var priority: Int? = null

    override fun onStart() {
        super.onStart()
        habit = intent.getParcelableExtra<Habit>(key)
        setContentView(R.layout.layout_add)
        val spinner = findViewById<Spinner>(R.id.choose_priority)
        val adapter = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.priorities,
            R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        val title = findViewById<TextView>(R.id.add_title)
        val btn = findViewById<Button>(R.id.create_button)
        if (habit != null) {
            title.text = "Изменение привычки"
            btn.text = "Применить"
        }

        btn.setOnClickListener {
            Log.i("Adder", "Btn clicked")
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priority = parent!!.getItemAtPosition(position) as Int
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }
}