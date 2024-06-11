package dal.repositories

import dal.exceptions.StationNotFoundException
import dal.models.Order
import dal.models.Station
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Repository
class RepositoryPostgre(private val jdbcTemplate: JdbcTemplate) : dal.repositories.Repository {
    override fun create (
        userId: Int,
        fromStationId: Int,
        toStationId: Int,
        status: Int): Int  {
        val sql = """
            insert into orders (user_id, from_station_id, to_station_id, status, created)
                   VALUES (?, ?, ?, ?, ?)
            returning id
        """.trimIndent()

        try {
            return jdbcTemplate.queryForObject(sql, arrayOf(userId, fromStationId, toStationId, status, LocalDateTime.now()), Int::class.java)
        }
        catch (ex: DataIntegrityViolationException) {
            if (ex.message == null || !ex.message!!.contains("violates foreign key constraint")) {
                throw ex
            }
            // Extracting the incorrect id of the station
            val parts = ex.message!!.split("=(")
            if (parts.size != 2) {throw ex}
            val part = parts[1]
            val index = part.indexOf(")")
            if (index == -1) {throw ex}
            val numberStr = part.substring(0..<index)
            val number = numberStr.toIntOrNull() ?: throw ex

            throw StationNotFoundException(number)
        }
    }

    override fun getById(id: Int): Order? {
        val sql = """
            select orders.id as id,
                   orders.user_id as user_id,
                   orders.status as status,
                   orders.created as created,
                   from_station.id as from_station_id,
                   from_station.station as from_station_name,
                   to_station.id as to_station_id,
                   to_station.station as to_station_name
            
            from orders.public.orders
            join public.station from_station on orders.from_station_id = from_station.id
            join public.station to_station on orders.to_station_id = to_station.id
            where orders.id = ?
        """.trimMargin()

        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (ex: EmptyResultDataAccessException) {
            null
        }
    }

    override fun getChecked(): List<Order> {
        val sql = """
                select orders.id as id,
                       orders.user_id as user_id,
                       orders.status as status,
                       orders.created as created,
                       from_station.id as from_station_id,
                       from_station.station as from_station_name,
                       to_station.id as to_station_id,
                       to_station.station as to_station_name

                from orders.public.orders
                         join public.station from_station on orders.from_station_id = from_station.id
                         join public.station to_station on orders.to_station_id = to_station.id
                where orders.status = ?
            """.trimIndent()
        return jdbcTemplate.query(sql, rowMapper, 1)
    }

    override fun update(id: Int, newValue: Order) {
        val sql = """
            update orders set
                user_id = ?,
                from_station_id = ?,
                to_station_id = ?,
                status = ?,
                created = ?
            where id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, newValue.userId, newValue.fromStation.id, newValue.toStation.id, newValue.status, newValue.created, newValue.id)
    }

    private val rowMapper = RowMapper<Order> { rs: ResultSet, _: Int ->
        Order(
            id = rs.getInt("id"),
            userId = rs.getInt("user_id"),
            fromStation = Station(
                id = rs.getInt("from_station_id"),
                name = rs.getString("from_station_name")
            ),
            toStation = Station(
                id = rs.getInt("to_station_id"),
                name = rs.getString("to_station_name")
            ),
            status = rs.getInt("status"),
            created = rs.getTimestamp("created").toLocalDateTime()
        )
    }
}
