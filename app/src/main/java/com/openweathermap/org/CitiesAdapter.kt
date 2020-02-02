package com.openweathermap.org

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openweathermap.org.model.CurrentWeatherResponse
import kotlinx.android.synthetic.main.forecast_row_item.view.*

class CitiesAdapter(val weatherList:List<CurrentWeatherResponse>) :RecyclerView.Adapter<CitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        return CitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecast_row_item, parent, false))
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder?.temp?.text = weatherList.get(position).main.temp.toString()
        holder.tempMin.text = weatherList.get(position).main.tempMin.toString()
        holder.tempMax.text = weatherList.get(position).main.tempMax.toString()
        holder.weather.text = weatherList.get(position).weather.get(0).description
        holder.wind.text = weatherList.get(position).wind.speed.toString()
        holder.dateLabel.visibility = View.GONE
        holder.date.visibility = View.GONE
    }
}

class CitiesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val temp = view.tempVal
    val tempMin = view.minVal
    val tempMax = view.maxVal
    val weather = view.weatherVal
    val wind = view.windVal
    val dateLabel = view.date
    val date = view.dateVal
}