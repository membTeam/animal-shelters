package ru.animals;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.animals.repository.BreedsRepository;

import ru.animals.entities.commonModel.PhotoForAnimationDTO;

import java.util.List;
import java.util.ArrayList;


@SpringBootTest
public class CreateBottomOnRunTimeTest {
    @Autowired
    private BreedsRepository breedsRepository;

    private InlineKeyboardButton createInlineKeyboardButton(String textButton,
                                                            String idCollback) {
        InlineKeyboardButton result = new InlineKeyboardButton();
        result.setText(textButton);
        result.setCallbackData(idCollback);

        return result;
    }


    /**
     * Создание шаблона метода для кнопочного меню, связанных с показом фотографий
     */
    @Test
    public void createButtonMenuPhoto() {

        var chatId = "1000";

        var lsPhotoAnimation = breedsRepository.getPhotoForAnimation(1L);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Для просмотра фотографии Click на кнопке животного");

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        var markupInline = new InlineKeyboardMarkup();
        var strPrefix = "pht-photoanimal-";

        lsPhotoAnimation.forEach(item -> {
            PhotoForAnimationDTO photoForAnimationDTO =
                    new PhotoForAnimationDTO(item);

            var textButton = String.format("%s %s %s",
                    photoForAnimationDTO.getShortName(),
                    photoForAnimationDTO.getBreed(),
                    photoForAnimationDTO.getNickname());

            List<InlineKeyboardButton> rowInline = List.of(
                    createInlineKeyboardButton(textButton,
                            strPrefix + photoForAnimationDTO.getFilePath()) );

            rowsInline.add(rowInline);
        });

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        assertNotNull(message);

        assertEquals("1000", message.getChatId());

        assertEquals(lsPhotoAnimation.size(), rowsInline.size());
    }

}
