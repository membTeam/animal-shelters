package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.entities.commonModel.PhotoForAnimationDTO;
import ru.animals.utils.parser.StructForCollbackConfig;

import java.util.ArrayList;
import java.util.List;

public class CollbackComdAllanimalfree extends BaseObject {

    private InlineKeyboardButton createInlineKeyboardButton(String textButton,
                                                            String idCollback) {
        InlineKeyboardButton result = new InlineKeyboardButton();
        result.setText(textButton);
        result.setCallbackData(idCollback);

        return result;
    }

    @Override
    public SendMessage apply(DistrCollbackCommand repositoryServ, Long chatId, StructForCollbackConfig dataFromParser) {
        var breedsRepo = repositoryServ.getBreedsRepository();

        var lsPhotoAnimation = breedsRepo.getPhotoForAnimation(1L);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Для просмотра фотографии Click на кнопке животного");

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        var markupInline = new InlineKeyboardMarkup();
        var strPrefix = "pht-photoanimal-";

        lsPhotoAnimation.forEach(item -> {
            PhotoForAnimationDTO photoForAnimationDTO =
                    new PhotoForAnimationDTO(item);

            var textButton = String.format("(%s) %s  кличка: %s",
                    photoForAnimationDTO.getShortName(),
                    photoForAnimationDTO.getBreed(),
                    photoForAnimationDTO.getNickname());

            List<InlineKeyboardButton> rowInline = List.of(
                    createInlineKeyboardButton(textButton,
                            strPrefix + photoForAnimationDTO.getFilePath()) );

            rowsInline.add(rowInline);
        });

        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        return sendMessage;
    }
}
