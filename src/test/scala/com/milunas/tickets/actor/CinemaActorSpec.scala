package com.milunas.tickets.actor

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.milunas.tickets.SystemCleanAll
import com.milunas.tickets.cinema.actor.CinemaActor
import com.milunas.tickets.cinema.domain.{Movie, Movies}
import com.milunas.tickets.cinema.message.{CreateMovie, GetMovie, GetMovies, GetTickets}
import com.milunas.tickets.cinema.output.MovieResponse.MovieCreated
import com.milunas.tickets.ticket.domain.{Ticket, Tickets}
import org.scalatest.{MustMatchers, WordSpecLike}

class CinemaActorSpec extends TestKit(ActorSystem("testBoxOffice"))
  with WordSpecLike
  with MustMatchers
  with ImplicitSender
  with DefaultTimeout
  with SystemCleanAll {

  "Cinema" must {

    "Create a movie and get tickets from the Ticket Actor" in {
      val cinema: ActorRef = system.actorOf(CinemaActor.props)
      val movieName = "Rocky"

      cinema ! CreateMovie(movieName, 10)
      expectMsg(MovieCreated(Movie(movieName, 10)))

      cinema ! GetMovies
      expectMsg(Movies(Vector(Movie(movieName, 10))))

      cinema ! GetMovie(movieName)
      expectMsg(Some(Movie(movieName, 10)))

      cinema ! GetTickets(movieName, 1)
      expectMsg(Tickets(movieName, Vector(Ticket(1))))
    }
  }
}
