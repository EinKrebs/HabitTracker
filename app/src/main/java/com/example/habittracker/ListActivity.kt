package com.example.habittracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

const val habitKey: String = "data"
const val indexKey: String = "index"
const val addRequestCode = 1
const val changeRequestCode = 2

class CustomAdapter(
    private val habits: List<Habit>,
    private val onClick: (Habit, Int) -> Unit
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
        private var currentHabit: Habit? = null
        private val habitName = itemView.findViewById<TextView>(R.id.habit_name);
        private val habitDescription =
            itemView.findViewById<TextView>(R.id.habit_description_value);
        private val habitPriority = itemView.findViewById<TextView>(R.id.habit_priority_value);
        private val habitPeriodicity =
            itemView.findViewById<TextView>(R.id.habit_periodicity_value);
        private val habitType = itemView.findViewById<TextView>(R.id.habit_type_value);
        private val habitColor = itemView.findViewById<TextView>(R.id.habit_color);

        fun bind(habit: Habit) {
            currentHabit = habit

            habitName.text = habit.name;
            habitColor.setBackgroundResource(habit.color);
            habitDescription.text = habit.description;
            habitType.text = habit.type.value;
            habitPeriodicity.text = habit.period;
            habitPriority.text = habit.priority.toString();
        }
    }

    override fun getItemCount() = habits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = habits[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick(item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.habit_desc, parent, false))
    }
}

class ListActivity : AppCompatActivity() {
    private var habits: MutableList<Habit> = emptyList<Habit>().toMutableList()
    private lateinit var recyclerView: RecyclerView
    val onCLick: (Habit, Int) -> Unit = { habit: Habit, ind: Int ->
        val intent = Intent(applicationContext, AddActivity::class.java).apply {
            putExtra(habitKey, habit)
            putExtra(indexKey, ind)
        }
        startActivityForResult(intent, 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycle_view)
        habits.add(
            Habit(
                "Пример",
                "ААААААА",
                3,
                "каждый день",
                "по пять раз",
                HabitType.NEUTRAL,
                R.color.green
            )
        )
        recyclerView.adapter = CustomAdapter(habits, onCLick)

        findViewById<Button>(R.id.add_button).setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, AddActivity::class.java),
                addRequestCode
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null)
            return
        if (requestCode == addRequestCode) {
            val newHabit = data.getParcelableExtra<Habit>(habitKey)
            if (newHabit != null) {
                habits.add(newHabit)
                recyclerView.adapter!!.notifyItemInserted(habits.size)
            }
        } else {
            val habit = data.getParcelableExtra<Habit>(habitKey)!!
            val index = data.getIntExtra(indexKey, -1)
            habits[index] = habit
            recyclerView.adapter!!.notifyItemChanged(index)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val hab = savedInstanceState.getParcelableArrayList<Habit>(habitKey)
        habits = hab ?: emptyList<Habit>().toMutableList()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(habitKey, ArrayList<Habit>(habits))
    }
}