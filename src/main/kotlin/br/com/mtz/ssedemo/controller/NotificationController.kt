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
        private val notifiers: MutableMap<String, SseEmitter> = mutableMapOf()
    }

    @CrossOrigin
    @RequestMapping("/connect/{id}")
    fun connect(@PathVariable id: String): SseEmitter {
        return SseEmitter(86400000)
            .apply {
                onCompletion { notifiers.remove(id) }
                onTimeout {
                    this.complete()
                    notifiers.remove(id)
                }
                notifiers[id] = this
            }
    }

    @PostMapping(value = ["/notifications"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody createNotificationRequest: NotificationRequest) {
        val entity = notificationService.create(createNotificationRequest)
        notifiers[entity.destination]?.send(SseEmitter.event().data(entity).name("NOTIFICATION"))
    }

}