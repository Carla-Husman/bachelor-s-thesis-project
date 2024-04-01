package ro.tuiasi.student.carla.proiect.services

import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.services.interfaces.IFilterCitiesService
import java.io.InputStream
import java.text.Normalizer
import java.util.*

@Service
class FilterCitiesService : IFilterCitiesService {
    companion object {
        var cities = mutableListOf<String>()
    }

    override fun filterCities(text: String): List<String> {
        return cities.filter { it.removeDiacritics().contains(text.removeDiacritics(), ignoreCase = true) }.shuffled()
            .take(10)
    }

    fun uploadCities() {
        val inputStream: InputStream? = javaClass.classLoader.getResourceAsStream("city_country.txt")
        val inputString = inputStream?.bufferedReader().use { it?.readText() ?: "" }
        cities = inputString.split("\n").toMutableList()
    }

    fun String.removeDiacritics(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    fun existsCity(city: String): Boolean {
        val normalizedCity = city.trim().removeDiacritics().lowercase(Locale.getDefault())
        return cities.any { it.trim().removeDiacritics().lowercase(Locale.getDefault()) == normalizedCity }
    }
}