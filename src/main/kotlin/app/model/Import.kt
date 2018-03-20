package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process

interface Import {

    fun doIt(setup: ConversionSetup, db: IDatabase): Process

}
