package com.example.demo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.util.*

@Table("test_entity")
data class Entity(
    @Id val id: UUID? = null,
    val name: String,
)

interface EntityDao : ReactiveCrudRepository<Entity, UUID>
