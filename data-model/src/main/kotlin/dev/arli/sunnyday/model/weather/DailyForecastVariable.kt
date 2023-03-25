package dev.arli.sunnyday.model.weather

enum class DailyForecastVariable(val key: String) {
    WeatherCode("weathercode"),
    Temperature2mMax("temperature_2m_max"),
    Temperature2mMin("temperature_2m_min"),
    ApparentTemperature2mMax("apparent_temperature_max"),
    ApparentTemperature2mMin("apparent_temperature_min"),
    Sunrise("sunrise"),
    Sunset("sunset"),
    UVIndexMax("uv_index_max")
}
