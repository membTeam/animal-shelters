package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.ContentReport;
import ru.animals.entities.commonModel.MetaDataPhoto;

public interface ReportsRepository extends JpaRepository<ContentReport, Long> {
    //@Query(value = "select cr.meta_data_photo from content_report cr where cr.meta_data_photo ->> 'otherinf' = :info",
    /*@Query(value = "select cr.metaDataPhoto from content_report cr where cr.metaDataPhoto ->> 'otherinf' = :info")
    Object getMetaDataPhoto(String info);*/

    ContentReport findByHashmetadata(String hashmetadata);

}
