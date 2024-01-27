package cl.nvh.estudio.serviciosbasicos_app.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Data Access Object
@Dao
interface RegistroDao {

    @Insert
    suspend fun insert(registro: Registro)

    @Update
    suspend fun update(registro:Registro)

    @Delete
    suspend fun borrarUnRegistro(registro: Registro)

    @Query("DELETE FROM Registro")
    suspend fun delete()

    @Query("SELECT * FROM Registro ORDER BY fecha DESC")
    suspend fun obtenerTodos():List<Registro>

    @Query("SELECT * FROM Registro where id = :id")
    suspend fun obtenerPorId(id:Int): Registro?



}