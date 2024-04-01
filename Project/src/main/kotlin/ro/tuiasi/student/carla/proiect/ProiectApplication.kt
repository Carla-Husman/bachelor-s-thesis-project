package ro.tuiasi.student.carla.proiect

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ro.tuiasi.student.carla.proiect.services.FilterCitiesService

@SpringBootApplication
class ProiectApplication

fun main(args: Array<String>) {
    FilterCitiesService().uploadCities()
    runApplication<ProiectApplication>(*args)
}
