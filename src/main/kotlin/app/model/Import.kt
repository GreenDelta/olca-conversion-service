package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process

/**
 * Imports the data as defined in a conversion setup into the given database.
 */
interface Import {

    fun doIt(setup: ConversionSetup, db: IDatabase)

}
