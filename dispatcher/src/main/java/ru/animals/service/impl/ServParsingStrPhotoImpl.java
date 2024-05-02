package ru.animals.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.animals.entities.Animals;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.service.ServParsingStrPhoto;
import ru.animals.service.ServParsingStrPhotoNext;
import ru.animals.utilsDEVL.entitiesenum.ParsingStrPhoto;

@Service
@RequiredArgsConstructor
public class ServParsingStrPhotoImpl implements ServParsingStrPhoto, ServParsingStrPhotoNext {

    private final ReportsRepository reportsRepository;
    private final AnimalsRepository animalsRepository;

    @Value("${image-storage-dir-any}")
    private String pathStorageDirAny;

    @Override
    public AnimalsRepository getAnimalsRepository() {
        return animalsRepository;
    }

    @Override
    public ReportsRepository getReportsRepository() {
        return reportsRepository;
    }

    @Override
    public String getPathPhoto(String strTempl) {

        ParsingStrPhoto parsingStrPhoto = ParsingStrPhoto.of(strTempl);

        var strResult = switch (parsingStrPhoto.getDistination()) {
            case "photoany" -> pathStorageDirAny + "/" + parsingStrPhoto.getResource();
            case "photoanimal" -> getStrPathFromAnimal(parsingStrPhoto.getResource());
            case "photorep" -> getStrPathFromReport(parsingStrPhoto.getResource());
            default -> "empty";
        };

        if (strResult.equals("empty")) {
            throw new UnsupportedOperationException("тип источника фото не определен");
        }

        return strResult;
    }
    
    private String getStrPathFromReport(String resouce) {
        MetaDataPhoto metaDataPhoto = reportsRepository.findByHashmetadata(resouce);

        if (metaDataPhoto == null) {
            throw new UnsupportedOperationException("Нет данных по отчетам");
        }

        return metaDataPhoto.getFilepath();

    }

    private String getStrPathFromAnimal(String resouce) {
        Animals animals = animalsRepository.findAnimalsByHashmetadataNext(resouce);

        if (animals == null) {
            throw new UnsupportedOperationException("Нет данных по животным");
        }

        var nickname = animals.getNickname();
        var filePath = animals.getMetaDataPhoto().getFilepath();

        return nickname + "##" + filePath;
    }
    
}
