package ru.animals.session.stateImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.animals.entities.commonModel.MetaDataPhoto;

enum StateSession {SEND_MESSAGE, RESULT_OBTAINED; }

@NoArgsConstructor
@Getter
@Setter
public class DataBufferReport {

    private StateSession stateSession;
    private String errMessage;

    private Long chatId;
    private String animalDiet;
    private String generalWellBeing;
    private String changeInBehavior;
    private MetaDataPhoto metaDataPhoto;

    public boolean setProperty(DataBufferReportDTO reportDTO, String textFromUpdate) {

        boolean result = true;
        final String strFromReportDTO = reportDTO.getStrProperty();

        if (strFromReportDTO.equals("animalDiet")) {
            animalDiet = textFromUpdate;
        } else if (strFromReportDTO.equals("generalWellBeing")) {
            generalWellBeing = textFromUpdate;
        } else if (strFromReportDTO.equals("changeInBehavior")) {
            changeInBehavior = textFromUpdate;
        } else {
            result = false;
        }

        return result;
    }

    public void sendMessage() {
        stateSession = StateSession.SEND_MESSAGE;
        errMessage = "ok";
    }

    public void resultObtainded() {
        stateSession = StateSession.RESULT_OBTAINED;
        errMessage = "ok";
    }

}
