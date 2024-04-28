package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.ContentReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ReportsRepository extends JpaRepository<ContentReport, Long> {

    ContentReport getReportByHashmetadata(String info);

/*    @Query(value = "SELECT ad.id, ct.date, ad.date_start, ad.date_finish, ct.animal_diet, ct.change_behavior, " +
            "ct.general_well_being " +
            "FROM ct join adoption ad on ct.adoption_id = ad.id " +
            "where ct.volunteer_id is null", nativeQuery = true)
    List<Object> getReports();*/

    @Query(value = "select id, animal_diet, change_behavior, general_well_being, date_start, date_finish, url from verification_report;", nativeQuery = true)
    List<List<String>> verificationReport();

    @Query(value = "select rep.adoption.dateFinish from ContentReport rep where rep.id = :id")
    LocalDateTime getDateFinishReport(Long id);

}
