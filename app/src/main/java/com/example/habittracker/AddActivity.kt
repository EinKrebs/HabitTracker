package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.*
import kotlin.random.Random


val priorities = arrayOf(1, 2, 3, 4, 5)

class AddActivity : Activity(), AdapterView.OnItemSelectedListener {
    private var habit: Habit? = null
    private var index: Int? = null
    private var priority: Int = 1
    private var changing: Boolean? = null
    private var color: Int = -1

    fun fillFields(habit: Habit) {
        val name = findViewById<EditText>(R.id.add_name_input)
        val desc = findViewById<EditText>(R.id.add_description_input)
        val type = findViewById<RadioGroup>(R.id.choose_type)
        val periodicity = findViewById<EditText>(R.id.add_periodicity_time)
        val spinner = findViewById<Spinner>(R.id.choose_priority)
        name.setText(habit.name)
        desc.setText(habit.description)
        type.check(
            when (habit.type) {
                HabitType.HEALTHY -> R.id.healthy_button
                HabitType.NEUTRAL -> R.id.neutral_button
                else -> R.id.unhealthy_button
            }
        )
        periodicity.setText(habit.period)
        spinner.setSelection(habit.priority - 1)
    }

    override fun onStart() {
        super.onStart()
        habit = intent.getParcelableExtra<Habit>(habitKey)
        index = intent.getIntExtra(indexKey, -1)
        setContentView(R.layout.layout_add)
        val spinner = findViewById<Spinner>(R.id.choose_priority)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            priorities
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        val title = findViewById<TextView>(R.id.add_title)
        val btn = findViewById<Button>(R.id.create_button)
        if (habit != null) {
            title.text = "Изменение привычки"
            btn.text = "Применить"
            changing = true
            fillFields(habit!!)
            color = habit!!.color
        } else {
            changing = false
        }

        btn.setOnClickListener {
            val name = findViewById<EditText>(R.id.add_name_input)
            val desc = findViewById<EditText>(R.id.add_description_input)
            val type = findViewById<RadioGroup>(R.id.choose_type).checkedRadioButtonId
            val periodicity = findViewById<EditText>(R.id.add_periodicity_time)
            if (name.text.toString() == ""
                || type == -1
                || periodicity.text.toString() == ""
            ) {
                findViewById<TextView>(R.id.wrong_habit_msg).visibility = View.VISIBLE
                return@setOnClickListener
            }
            val typeValue = when (type) {
                R.id.healthy_button -> HabitType.HEALTHY
                R.id.neutral_button -> HabitType.NEUTRAL
                R.id.unhealthy_button -> HabitType.UNHEALTHY
                else -> null
            }!!
            var color = when (Random.nextInt() % 4) {
                0 -> R.color.red
                1 -> R.color.green
                3 -> R.color.teal_200
                else -> R.color.blue
            }
            if (changing!!)
                color = this.color
            val habit = Habit(
                name.text.toString(),
                desc.text.toString(),
                priority,
                periodicity.text.toString(),
                typeValue,
                color
            )
            if (changing!!) {
                setResult(
                    changeRequestCode,
                    Intent().apply {
                        putExtra(habitKey, habit)
                        putExtra(indexKey, index)
                    }
                )
                finish()
            }
            else {
                setResult(
                    addRequestCode,
                    Intent().apply {
                        putExtra(habitKey, habit)
                    }
                )
                finish()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        priority = priorities[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        priority = 1
    }
}