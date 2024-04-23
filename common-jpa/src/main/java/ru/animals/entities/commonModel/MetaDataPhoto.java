package ru.animals.entities.commonModel;


import lombok.*;


/**
 * Используется для json формат файла фото
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataPhoto {
    private String file;
    private String filepath;
    private Long filesize;
    private String metatype;
}
