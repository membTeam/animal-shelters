package ru.animals.service;

import ru.animals.entities.commonModel.WebResponseResultVerificationDTO;
import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.models.VolunteerWeb;

import java.util.List;

public interface VolunteerService {
    String adoption(VolunteerWeb volunteerWeb);

    List<WebVerificationResponseDTO> getReprotForVerification();

    String verificationReport(WebResponseResultVerificationDTO verReport);
}
