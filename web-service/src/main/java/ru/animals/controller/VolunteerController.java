package ru.animals.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.animals.entities.commonModel.WebResponseResultVerificationDTO;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.models.VolunteerWeb;
import ru.animals.repository.VolunteerRepository;
import ru.animals.service.VolunteerService;

import java.util.List;

@RestController
@RequestMapping("/web-volunteer") //localhost:8085/web-volunteer/adoption
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("verification-report")
    public List<WebVerificationResponseDTO> getReprotForVerification() {
        return volunteerService.getReprotForVerification();
    }

    @PostMapping(value="adoption", consumes={"multipart/form-data"})
    public String adoption(@ModelAttribute VolunteerWeb volunteerWeb) {
        return volunteerService.adoption(volunteerWeb);
    }

    @PostMapping(value="checking-report", consumes={"multipart/form-data"})
    public String verificationReport(@ModelAttribute WebResponseResultVerificationDTO verReport ) {

        var res = volunteerService.verificationReport(verReport);

        return res;
    }

}
