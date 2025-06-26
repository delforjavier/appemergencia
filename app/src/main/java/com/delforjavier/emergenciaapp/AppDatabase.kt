// Define el paquete donde se encuentra esta clase
package com.delforjavier.emergenciaapp.data

// Importaciones necesarias para trabajar con Room y el contexto de la app
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Anotación que indica que esta clase es una base de datos de Room
// Se especifican las entidades (tablas), la versión de la base de datos y si se exporta el esquema

@Database(
    entities = [RegistroEmergenciaEntity::class, UserEntity::class], // Tablas: registro de emergencias y usuarios
    version = 1, // Versión inicial de la base de datos
    exportSchema = false // No exportar el esquema de la base de datos a un archivo
)
abstract class AppDatabase : RoomDatabase() {

    // Métodos abstractos para acceder a los DAOs (Data Access Objects)
    // DAO = clases que contienen los métodos para interactuar con la base de datos

    abstract fun registroEmergenciaDao(): RegistroEmergenciaDao
    abstract fun userDao(): UserDao

    // Objeto companion que permite crear y acceder a una única instancia de la base de datos (Singleton)

    companion object {

        // Volatile asegura que los cambios en INSTANCE se reflejen en todos los hilos

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Metodo para obtener la instancia de la base de datos

        fun getDatabase(context: Context): AppDatabase {

            // Si la instancia ya existe, la devuelve. Si no, la crea de forma segura (synchronized)

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Se usa el contexto de la aplicación
                    AppDatabase::class.java, // Clase de la base de datos
                    "emergencia_db"  // Nombre del archivo de la base de datos
                ).build()
                INSTANCE = instance // Guarda la instancia para reutilizarla
                instance  // Devuelve la instancia
            }
        }
    }
}