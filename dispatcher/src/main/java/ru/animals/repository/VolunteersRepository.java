package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.animals.entities.Breeds;
import ru.animals.entities.Volunteers;

import java.util.Collection;
import java.util.List;

public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {

    @Query(value = "select vl From Volunteers vl where vl.chartName = :chartName")
    List<Volunteers> volonterForChartName(String chartName);

}
