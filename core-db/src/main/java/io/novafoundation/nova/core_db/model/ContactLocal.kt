package io.novafoundation.nova.core_db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
class ContactLocal(
    @PrimaryKey
    val id: String,
    val address: String,
    val name: String,
    val memo: String?
)
