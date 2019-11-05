package br.com.mtz.ssedemo.service

import br.com.mtz.ssedemo.controller.request.NotificationRequest
import br.com.mtz.ssedemo.entity.Notification
import br.com.mtz.ssedemo.repository.NotificationRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificationService(private val notificationRepository: NotificationRepository) {

    fun create(createNotificationRequest: NotificationRequest): Notification {
        return notificationRepository.save(createNotificationRequest.toEntity())
    }

    private fun NotificationRequest.toEntity(): Notification {
        return Notification(
            id = UUID.randomUUID().toString(),
            name = this.name,
            content = this.content,
            author = this.author,
            destination = this.destination
        )
    }

}