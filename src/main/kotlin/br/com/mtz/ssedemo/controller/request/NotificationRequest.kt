package br.com.mtz.ssedemo.controller.request

data class NotificationRequest(
    val name: String,
    val content: String,
    val author: String,
    val destination: String
)