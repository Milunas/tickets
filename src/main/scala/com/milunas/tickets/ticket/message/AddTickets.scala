package com.milunas.tickets.ticket.message

import com.milunas.tickets.ticket.domain.Ticket

case class AddTickets(tickets: Vector[Ticket])
