package dal.exceptions

class StationNotFoundException (stationId: Int): NotFoundExceptionBase("Станции с id $stationId нет в базе") {}