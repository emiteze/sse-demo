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
        val notifier = SseEmitter(86400000)
        notifiers[id] = notifier
        notifier.onCompletion { notifiers.remove(id) }
        notifier.onTimeout {
            notifier.complete()
            notifiers.remove(id)
        }
        return notifier
    }

    @PostMapping(value = ["/notifications"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody createNotificationRequest: NotificationRequest) {
        val deadNotifiers: MutableMap<String, SseEmitter> = mutableMapOf()
        val entity = notificationService.create(createNotificationRequest)
        notifiers.filterKeys { it == entity.destination }.forEach { notifier ->
            try {
                notifier.value.send(SseEmitter.event().data(entity).name("NOTIFICATION").id(entity.destination))
            } catch (exception: Exception) {
                deadNotifiers[entity.destination] = notifier.value
            }
        }
        deadNotifiers.forEach {
            notifiers.remove(it.key)
        }
    }

}