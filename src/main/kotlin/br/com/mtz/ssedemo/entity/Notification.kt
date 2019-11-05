package br.com.mtz.ssedemo.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "notifications")
data class Notification(
    @field:Id
    @Column(name = "id", unique = true)
    val id: String,
    @Column(name = "name")
    val name: String,
    @Column(name = "content")
    val content: String,
    @Column(name = "author")
    val author: String,
    @Column(name = "destination")
    val destination: String
)