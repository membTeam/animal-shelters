package ru.animals.models.serviceWebModels;

import ru.animals.entities.commonModel.WebVerificationResponseDTO;
import ru.animals.repository.ReportsRepository;

import java.util.ArrayList;
import java.util.List;

public class WebAnimalsResponseServ {

    public static List<WebVerificationResponseDTO> getListWebVerification (ReportsRepository repo) {
        var lsReport = repo.verificationReport();

        List<WebVerificationResponseDTO> lsResult = new ArrayList<>();
        lsReport.forEach(list-> lsResult.add(new WebVerificationResponseDTO(list)) );

        return lsResult;
    }

}
