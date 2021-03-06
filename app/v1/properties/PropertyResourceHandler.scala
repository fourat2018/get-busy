package v1.properties

import scala.concurrent.{ExecutionContext, Future}

import play.api.MarkerContext

import javax.inject.{Inject, Provider}

import java.sql.Date

import v1.properties.forms._
import v1.properties.resource._


class PropertyResourceHandler @Inject()(
    routerProvider: Provider[PropertyRouter],
    propertyRepository: PropertyRepository)(implicit ec: ExecutionContext) {

  def addProperty(propertyInput: PropertyFormInput)(implicit mc: MarkerContext): Future[PropertyResource] = {
    propertyRepository.insertProperty(propertyInput).map { prop => createPropertyResource(prop)}
  }


  def propertyLookup(propertyId: Long)(implicit mc: MarkerContext): Future[Option[PropertyResource]] = {
    val propertyFuture = propertyRepository.getProperty(propertyId)
    propertyFuture.map { maybePropertyData =>
      maybePropertyData.map { propertyData =>
        createPropertyResource( propertyData)
      }
    }
  }

  def propertyDelete(propertyId: Long)(implicit mc: MarkerContext)= {
    propertyRepository.deleteProperty(propertyId)
  }

  def propertiesList(implicit mc: MarkerContext): Future[Seq[PropertyResource]] = {
    propertyRepository.listProperties().map { properties =>
      properties.map(property => createPropertyResource(property))
    }
  }


  def updateProperty(propertyId:Long,propertyInput: PropertyFormInput)(implicit mc: MarkerContext)  = {
    propertyRepository.updateProperty(propertyId,propertyInput)

  }

/*******************************Prices************************************************/
  def addPropertyPrice(propertyId:Long,priceInput:PriceFormInput)(implicit mc: MarkerContext)  = {
    propertyRepository.addPrice(propertyId,priceInput)

  }

  def getPropertyPrices(propertyId:Long)(implicit mc: MarkerContext): Future[PricesResource ] = {
    propertyRepository.listPrices(propertyId).map{pricesList =>  createPricesResource(pricesList,propertyId)
    }
  }


  private def createPricesResource(prices: Seq[PriceData], propertyId:Long): PricesResource = {
    PricesResource(prices.map(p=>PriceResource(p.price,p.date.toString())),routerProvider.get.link(propertyId))
  }


  private def createPropertyResource(p: PropertyData): PropertyResource = {
    PropertyResource(p.id, p.address, p.postCode,p.latitude,p.longitude,p.bedroomCount,p.surface,routerProvider.get.link(p.id))
  }



}
