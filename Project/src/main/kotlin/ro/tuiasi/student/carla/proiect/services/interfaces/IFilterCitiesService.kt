package ro.tuiasi.student.carla.proiect.services.interfaces

interface IFilterCitiesService {
    fun filterCities(text: String): List<String>

    fun existsCity(city: String): Boolean

    fun uploadCities()
}