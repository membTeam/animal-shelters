package ru.animals.session;

import ru.animals.session.stateImpl.DataBufferReport;

/**
 * Используется для создания объекта ContentReport
 * в FilePhotoAPIю.preparationContentRepository
 */
public interface StateReportServ {
    DataBufferReport getDataBufferReport();
}
