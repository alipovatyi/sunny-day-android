package dev.arli.sunnyday.model.weather

enum class HourlyForecastVariable(val key: String) {
    Temperature2m("temperature_2m"),
    RelativeHumidity2m("relativehumidity_2m"),
    DewPoint2m("dewpoint_2m"),
    ApparentTemperature("apparent_temperature"),
    PrecipitationProbability("precipitation_probability"),
    Precipitation("precipitation"),
    WeatherCode("weathercode"),
    PressureMsl("pressure_msl"),
    WindSpeed10m("windspeed_10m"),
    WindDirection10m("winddirection_10m"),
    UVIndex("uv_index")
}
