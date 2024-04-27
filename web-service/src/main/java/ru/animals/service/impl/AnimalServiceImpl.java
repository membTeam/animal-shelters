package ru.animals.service.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import ru.animals.entities.Animals;
import ru.animals.entities.Breeds;
import ru.animals.entities.commonModel.MetaDataPhoto;
import ru.animals.models.FileWebAPI;
import ru.animals.models.WebAnimal;
import ru.animals.models.WebDTO;
import ru.animals.models.WebResultData;
import ru.animals.repository.AnimalsRepository;
import ru.animals.repository.BreedsRepository;
import ru.animals.repository.ReportsRepository;
import ru.animals.service.AnimalService;
import ru.animals.service.AnimalServiceExt;
import ru.animals.utilsDEVL.ValueFromMethod;


@Service
public class AnimalServiceImpl implements AnimalService, AnimalServiceExt {

    private final Path imageStorageDir;
    private final BreedsRepository breedsRepository;
    private final AnimalsRepository animalsRepository;
    private final ReportsRepository reportsRepository;

    private final int port;

    public AnimalServiceImpl(@Value("${image-storage-dir}") Path imageStorageDir,
                             @Value("${server.port}") int port,
                             BreedsRepository breedsRepository, AnimalsRepository animalsRepository, ReportsRepository reportsRepository) {
        this.imageStorageDir = imageStorageDir;
        this.port = port;
        this.breedsRepository = breedsRepository;
        this.animalsRepository = animalsRepository;
        this.reportsRepository = reportsRepository;
    }

    @PostConstruct
    public void ensureDirectoryExists() throws IOException  {
        if (!Files.exists(this.imageStorageDir)) {
            Files.createDirectories(this.imageStorageDir);
        }
    }

    @Override
    public List<Breeds> getListBreeds(Long typeAnimationsId) {
        return breedsRepository.findAllByTypeAnimationsId(typeAnimationsId);
    }

    @Override
    public ValueFromMethod addAnimal(WebAnimal webAnimal) {

        WebDTO webDTO = FileWebAPI.preparationAnimalData(this, webAnimal);
        if (!webDTO.isResult()) {
            return new ValueFromMethod(webDTO.getMesError());
        }

        webDTO.setSavedAnimal(animalsRepository.save(webDTO.getAnimals()));

        FileWebAPI.preparationAnimalData(this, webDTO);
        if (!webDTO.isResult()) {
            return new ValueFromMethod(webDTO.getMesError());
        }

        var imageFile = webAnimal.getPhoto();
        try (InputStream in = imageFile.getInputStream()) {
            Animals animalSaved = webDTO.getSavedAnimal();
            animalSaved.setMetaDataPhoto(webDTO.getMetaDataPhoto());

            animalsRepository.save(animalSaved);
            Path targetPath = Paths.get(webDTO.getSavedAnimal().getMetaDataPhoto().getFilepath());
            OutputStream out = Files.newOutputStream(targetPath, StandardOpenOption.CREATE);
            in.transferTo(out);

        } catch (IOException e) {
            return new ValueFromMethod("Internal error");
        }

        return new ValueFromMethod(true, webDTO.getSavedAnimal().getMetaDataPhoto().getFile());
    }

    @Override
    public WebResultData getPhotReport(String info) {

        // rep-dog-48688

        var report = reportsRepository.findByHashmetadata(info);
        if (report == null) {
            return new WebResultData("Отчет не найден");
        }

        MetaDataPhoto metaDataPhoto = report.getMetaDataPhoto();

        var strPathFile = metaDataPhoto.getFilepath();
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(strPathFile));
        } catch (IOException es) {
            new WebResultData("Нет файла");
        }

        HttpHeaders headers;
        headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(metaDataPhoto.getMetatype()));
        headers.setContentLength(metaDataPhoto.getFilesize());

        var webResultData = new WebResultData(bytes, headers);

        return webResultData;
    }

    @Override
    public List<String> getListAnimals() {
        var lsAnimal = animalsRepository.findAll();
        List<String> result = new ArrayList<>();
        lsAnimal.forEach(item->
                {
                    var data = String.format("%s кличка:%s",
                            item.getMetaDataPhoto().getOtherinf(),
                            item.getNickname()
                            );
                    result.add(data);
                });

        return result;
    }

    @Override
    public BreedsRepository getBreedsRepository() {
        return breedsRepository;
    }

    @Override
    public AnimalsRepository getAnimalRepository() {
        return animalsRepository;
    }

    @Override
    public Path getImageStorageDir() {
        return imageStorageDir;
    }

    @Override
    public int getPort() {
        return port;
    }
}
