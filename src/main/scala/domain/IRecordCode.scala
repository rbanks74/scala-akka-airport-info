package domain


/**
 *
 * @param code is the Iata Code recieved from user posting to route;
 */
case class IRecordCode(code: String)

object IRecordCode {
  import scalaz._
  import scalaz.Scalaz._
  import play.api.libs.json._

  implicit val formatter: Format[IRecordCode] = Json.format[IRecordCode]

  def validate(ir: IRecordCode): Validation[String, IRecordCode] = {
    if (ir.code.isEmpty) "No record found".failure else ir.success
  }
}