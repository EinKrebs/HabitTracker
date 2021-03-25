package com.example.habittracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

const val key: String = "data"

class CustomAdapter (
    private val habits: List<Habit>,
    private val onClick: (Habit) -> Unit
): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(containerView: View, val onClick: (Habit) -> Unit): RecyclerView.ViewHolder(containerView) {
        private var currentHabit: Habit? = null
        private val habitName = itemView.findViewById<TextView>(R.id.habit_name);
        private val habitDescription = itemView.findViewById<TextView>(R.id.habit_description_value);
        private val habitPriority = itemView.findViewById<TextView>(R.id.habit_priority_value);
        private val habitPeriodicity = itemView.findViewById<TextView>(R.id.habit_periodicity_value);
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
        holder.bind(habits[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.habit_desc, parent, false), onClick)
    }
}

class ListActivity : AppCompatActivity() {
    var habits: MutableList<Habit> = emptyList<Habit>().toMutableList()
    var newHabit: Habit? = null
    lateinit var recyclerView: RecyclerView
    val onCLick: (Habit) -> Unit = {
        val intent = Intent(applicationContext, AddActivity::class.java).apply {
            putExtra(key, it)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newHabit = intent.getParcelableExtra(key)
        recyclerView = findViewById(R.id.recycle_view)
        habits.add(Habit(
        "Проёбывать дедлайны",
        "Это просто пиздец",
        1,
        "каждый день",
        HabitType.UNHEALTHY,
        R.color.black))
        recyclerView.adapter = CustomAdapter(habits, onCLick)

        findViewById<Button>(R.id.add_button).setOnClickListener {
            startActivity(Intent(applicationContext, AddActivity::class.java))
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val hab = savedInstanceState.getParcelableArrayList<Habit>(key)
        habits = hab ?: emptyList<Habit>().toMutableList()
        if (newHabit != null)
            habits.add(newHabit!!)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(key, ArrayList<Habit> (habits))
    }
}

object HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.name == newItem.name
    }
}