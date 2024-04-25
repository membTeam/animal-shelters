package ru.animals.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.animals.models.VolunteerWeb;
import ru.animals.repository.VolunteerRepository;
import ru.animals.service.VolunteerService;

@RestController
@RequestMapping("/web-volunteer") //localhost:8085/web-volunteer/adoption
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping(value="adoption", consumes={"multipart/form-data"})
    public String adoption(@ModelAttribute VolunteerWeb volunteerWeb) {
        return volunteerService.adoption(volunteerWeb);
    }

}
