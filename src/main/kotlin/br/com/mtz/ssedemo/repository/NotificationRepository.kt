package br.com.mtz.ssedemo.repository

import br.com.mtz.ssedemo.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, String>