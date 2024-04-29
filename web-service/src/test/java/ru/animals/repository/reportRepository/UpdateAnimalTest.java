package ru.animals.repository.reportRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.animals.entities.enumEntity.EnumTypeAnimation;
import ru.animals.repository.AnimalsRepository;

@SpringBootTest
public class UpdateAnimalTest {
    @Autowired
    private AnimalsRepository animalsRepository;

    @Test
    public void updateFiledHathmetadata() {

        // ТОЛЬКО ОДИН РАЗ ДЛЯ ОБНОВЛЕНИЯ ПОЛЯ hashmetadata

    }
//         var ls = animalsRepository.findAll();

      /*  ls.forEach(item -> {
            var strTypeAnimation = item.getMetaDataPhoto().getFile().substring(0,3);

            var hashCode = String.format("%d%d",
                    item.getId(), item.getBreedId()).hashCode();

            var strInfo = hashCode < 0
                    ? "animal-" + strTypeAnimation + "-img-" + hashCode
                    : "animal-" + strTypeAnimation + "-" + hashCode;

            item.setHashmetadata(strInfo);
            System.out.println(strInfo);
        });

        animalsRepository.saveAll(ls);

    }*/

}
