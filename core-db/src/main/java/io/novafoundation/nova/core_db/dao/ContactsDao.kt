package io.novafoundation.nova.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.novafoundation.nova.core_db.model.ContactLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactLocal)


    @Query("select * from contacts")
    fun getContacts(): Flow<List<ContactLocal>>

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContact(id: String)
}
