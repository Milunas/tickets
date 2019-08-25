package com.milunas.tickets.ticket.domain

case class Tickets (movie: String, var entries: Vector[Ticket] = Vector.empty[Ticket])
