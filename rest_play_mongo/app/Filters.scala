import javax.inject._

import filters.RequestFilter
import play.api.http.{DefaultHttpFilters, EnabledFilters}
import play.filters.gzip.GzipFilter
import play.api._

@Singleton
class Filters @Inject()(defaultFilters: EnabledFilters, gzipFilter: GzipFilter, env: Environment, requestLog: RequestFilter)
  extends DefaultHttpFilters(defaultFilters.filters :+ gzipFilter :+ requestLog : _* ){

}
