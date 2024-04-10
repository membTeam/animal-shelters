package ru.animals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.animals.entities.ContentReport;

public interface ReportsRepository extends JpaRepository<ContentReport, Long> {
}
