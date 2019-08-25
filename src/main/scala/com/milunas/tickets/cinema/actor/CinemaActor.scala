package com.milunas.tickets.cinema.actor

import akka.actor.{Actor, ActorRef}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.milunas.tickets.cinema.domain.{Movie, Movies}
import com.milunas.tickets.cinema.message.{CreateMovie, GetMovie, GetMovies, GetTickets}
import com.milunas.tickets.cinema.output.MovieExists
import com.milunas.tickets.cinema.output.MovieResponse.MovieCreated
import com.milunas.tickets.ticket.TicketActor
import com.milunas.tickets.ticket.domain.{Ticket, Tickets}
import com.milunas.tickets.ticket.message.{AddTickets, BuyTickets}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CinemaActor(implicit timeout: Timeout) extends Actor {

  def createTicketActor(name: String): ActorRef = {
    context.actorOf(TicketActor.props(name), name)
  }

  override def receive: Receive = {

    case CreateMovie(name, tickets) =>
      def create(): Unit = createMovie(name, tickets)
      context.child(name).fold(create())(_ => sender() ! MovieExists)

    case GetTickets(movie, tickets) =>
      def notFound(): Unit = sender() ! Tickets(movie)
      def buy(child: ActorRef): Unit = {
        child.forward(BuyTickets(tickets))
      }
      context.child(movie).fold(notFound())(buy)

    case GetMovie(movie) =>
      def notFound(): Unit = sender() ! None
      def getEvent(child: ActorRef): Unit = child forward GetMovie
      context.child(movie).fold(notFound())(getEvent)

    case GetMovies =>
      def getMovies = {
        context.children.map { child =>
          self.ask(GetMovie(child.path.name)).mapTo[Option[Movie]]
        }
      }
      def convertToMovies(f: Future[Iterable[Option[Movie]]]): Future[Movies] = {
        f.map(_.flatten).map(iterable ⇒ Movies(iterable.toVector))
      }
      pipe(convertToMovies(Future.sequence(getMovies))) to sender()
  }

  private def createMovie(name: String, tickets: Int) : Unit = {
    val movieTickets = createTicketActor(name)
    val newTickets = (1 to tickets).map {
      ticketId => Ticket(ticketId)
    }.toVector
    movieTickets ! AddTickets(newTickets)
    sender() ! MovieCreated(Movie(name, tickets))
  }

}
