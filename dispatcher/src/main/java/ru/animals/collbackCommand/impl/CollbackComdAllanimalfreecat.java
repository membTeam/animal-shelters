package ru.animals.collbackCommand.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.animals.collbackCommand.DistrCollbackCommand;
import ru.animals.utils.parser.StructForCollbackConfig;


/**
 * Создание кнопочного меню списка кошек
 */
public class CollbackComdAllanimalfreecat extends BaseObject{

    @Override
    public SendMessage apply(DistrCollbackCommand repositoryServ, Long chatId, StructForCollbackConfig dataFromParser) {
        var breedsRepo = repositoryServ.getBreedsRepository();

        var lsPhotoAnimation = breedsRepo.getPhotoForAnimation(2L);

        if (lsPhotoAnimation.size() == 0) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Нет данных по животным")
                    .build();
        }

        return AnimalsFreeService.createSendMessage(chatId, lsPhotoAnimation);
    }
}
