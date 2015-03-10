package co.s4n.comision

import org.scalatest.FunSuite
import scala.util.{Failure, Success, Try}
import co.s4n.comision.domain._

class ComisionServicesTest extends FunSuite {

  val c: Comision[Nueva] = new Comision( id = None, valorComision = 1l, iva = 1l, Nueva( ), new Cliente( "CC1234567", "Pepito" ) )

  ignore( "Typing example" ) {
//    val facturar = ComisionServices.facturar( c ) // <= No compila.
    assert( true )
  }

  test( "Functional composition: flatMap" ) {
    import ComisionServices._

    val comisionProcesada: Try[Comision[Facturada]] =
      liquidar( c )
        .flatMap( aprobar( _ )
        .flatMap( facturar( _ ) ) )

    comisionProcesada match {
      case Success( procesada ) => assert( procesada.estado == Facturada( ) )
      case Failure( ex ) => fail( ex )
    }
  }

  test( "Functional composition: for-comprehension" ) {
    import ComisionServices._

    val comisionProcesada: Try[Comision[Facturada]] = for {
      liquidada <- liquidar( c )
      aprobada <- aprobar( liquidada )
      facturada <- facturar( aprobada )
    } yield facturada

    comisionProcesada match {
      case Success( procesada ) => assert( procesada.estado == Facturada( ) )
      case Failure( ex ) => fail( ex )
    }
  }

}
