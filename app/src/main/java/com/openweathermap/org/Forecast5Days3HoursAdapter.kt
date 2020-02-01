package com.openweathermap.org

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openweathermap.org.model.ListItem
import kotlinx.android.synthetic.main.forecast_row.view.*

class Forecast5Days3HoursAdapter(val weatherList:List<ListItem>) :RecyclerView.Adapter<ForeCastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder {
        return ForeCastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecast_row, parent, false))
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        holder?.temp?.text = weatherList.get(position).main.temp.toString()
        holder.tempMin.text = weatherList.get(position).main.tempMin.toString()
        holder.tempMax.text = weatherList.get(position).main.tempMax.toString()
        holder.weather.text = weatherList.get(position).weather.get(0).description
        holder.wind.text = weatherList.get(position).wind.speed.toString()
        holder.date.text = weatherList.get(position).dtTxt
    }
}

class ForeCastViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val temp = view.tempVal
    val tempMin = view.minVal
    val tempMax = view.maxVal
    val weather = view.weatherVal
    val wind = view.windVal
    val date = view.dateVal
}