package ru.animals.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * Используется для json формат файла фото
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataPhoto {
    private String filepath;
    private String metatype;
    private int filesize;
}
