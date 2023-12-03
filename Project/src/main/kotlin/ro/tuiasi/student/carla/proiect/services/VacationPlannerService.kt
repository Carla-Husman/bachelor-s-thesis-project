package ro.tuiasi.student.carla.proiect.services

import ro.tuiasi.student.carla.proiect.services.interfaces.IVacationPlannerService
import org.springframework.stereotype.Service
import ro.tuiasi.student.carla.proiect.models.Poi
import ro.tuiasi.student.carla.proiect.models.Map
import ro.tuiasi.student.carla.proiect.models.VacationPlannerInput
import ro.tuiasi.student.carla.proiect.models.VacationPlannerOutput

@Service
class VacationPlannerService : IVacationPlannerService {
    override fun vacationPlanner(vacationPlannerInput: VacationPlannerInput): VacationPlannerOutput {
        return VacationPlannerOutput(
            photo = "photo",
            name = "Vacation name",
            destination = "Destination",
            season = vacationPlannerInput.season,
            attendant = vacationPlannerInput.attendant,
            distance = 0.0,
            poisNumber = 2,
            highlights = listOf("Highlight1", "Highlight2", "Highlight3"),
            pois = listOf(
                Poi(
                    name = "Poi1",
                    photo = "photo1",
                    description = "Description1",
                    tags = listOf("tag1", "tag2", "tag3"),
                    stars = 2.2945,
                    address = "Address1",
                    phone = "Phone1",
                    website = "Website1",
                    schedule = "Schedule1",
                    map = Map(
                        latitude = 48.8584,
                        longitude = 2.2945
                    )
                ),
                Poi(
                    name = "Poi2",
                    photo = "photo2",
                    description = "Description2",
                    tags = listOf("tag1", "tag2", "tag3"),
                    stars = 2.2945,
                    address = "Address2",
                    phone = "Phone2",
                    website = "Website2",
                    schedule = "Schedule2",
                    map = Map(
                        latitude = 44.8584,
                        longitude = 44.2945
                    )
                )
            )
        )
    }
}