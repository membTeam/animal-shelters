package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.ContentReport;

import java.util.Collection;
import java.util.List;

public interface ReportsRepository extends JpaRepository<ContentReport, Long> {
    //@Query(value = "select cr.meta_data_photo from content_report cr where cr.meta_data_photo ->> 'otherinf' = :info",
    /*@Query(value = "select cr.metaDataPhoto from content_report cr where cr.metaDataPhoto ->> 'otherinf' = :info")
    Object getMetaDataPhoto(String info);*/

    ContentReport getReportByHashmetadata(String info);

    /*@Query(value = "SELECT new ru.animals.entities.commonModel.WebReportSelectDTO( ad.id, ct.date, ad.dateStart, ad.dateFinish, ct.animalDiet, ct.changeBehavior " +
            "ct.generalWellBeing) " +
            "FROM content_report ct join flex adoption ad on ct.adoptionId = ad.id " +
            "where ct.volunteerId is null")
    List<WebReportSelectDTO> getReports();*/

/*    @Query(value = "SELECT new ru.animals.entities.commonModel.WebReportSelectDTO" +
            "( ad.id, ct.date, ad.dateStart, ad.dateFinish, ct.animalDiet, ct.changeBehavior, ct.generalWellBeing) " +
            "FROM adoption ad join fetch ad.lsContentReport " )
    WebReportSelectDTO getReports2();*/

/*    @Query(value = "SELECT ad.lsContentReport " +
            "FROM adoption ad join fetch ad.lsContentReport ct where ct.volunteerId is null" )
    Collection<ContentReport> getReports2();*/


    @Query(value = "SELECT ad.id, ct.date, ad.date_start, ad.date_finish, ct.animal_diet, ct.change_behavior, " +
            "ct.general_well_being " +
            "FROM content_report ct join adoption ad on ct.adoption_id = ad.id " +
            "where ct.volunteer_id is null", nativeQuery = true)
    List<Object> getReports();

    // ru.animals.entities.commonModel.WebReportSelectDTO

}
