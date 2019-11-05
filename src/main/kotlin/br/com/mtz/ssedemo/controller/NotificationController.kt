package br.com.mtz.ssedemo.controller

import br.com.mtz.ssedemo.controller.request.NotificationRequest
import br.com.mtz.ssedemo.service.NotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class NotificationController(private val notificationService: NotificationService) {

    companion object {
        private val notifier = SseEmitter(86400000)
    }

    @RequestMapping("/connect")
    fun connect(): SseEmitter = notifier

    @PostMapping(value = ["/notifications"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody createNotificationRequest: NotificationRequest) {
        val entity = notificationService.create(createNotificationRequest)
        notifier.send(entity)
    }

}