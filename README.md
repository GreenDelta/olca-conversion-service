openLCA conversion service
==========================
This project provides a RESTful web-service for the openLCA data conversion
functionalities. The service is linked to an openLCA Derby database for storing 
mappings, reference data, and processes. You can configure the service to use
an existing database. See the 
[sample configuration file](./olca-conversion-service/config.ini) for more 
information regarding the server setup. In the distribution package, just 
execute the `run` script to start the service.  


Current API
-----------

	GET  database/processes
		
		Returns descriptors of the processes in the openLCA database.
		
		MIME type: JSON
		Example: http://localhost:8080/database/processes
		
		
	GET database/flows
	
		Same as database/processes but for flows.
		
	
	GET database/flowProperties
	
		Same as database/processes but for flow properties.
	
	
	GET database/unitGroups
	
		Same as database/processes but for unit groups.
		
	
	GET database/process/ecoSpold1?uuid=[process UUID]&mime=[MIME type]
	
		Returns the process with the given UUID as EcoSpold 01 formatted 
		data set. The mime-parameter is optional,. If mime=json, the result
		is returned as an JSON object, otherwise the XML data set is returned.
		Returning an JSON object allows to embed conversion logs and other 
		information.
		
		MIME type: 	XML (default) or JSON
		Example: http://localhost:8080/database/process/ecoSpold1?uuit=76d6aaa4-37e2-40b2-994c-03292b600074
		
		
	GET database/process/ecoSpold2?uuid=[process UUID]&mime=[MIME type]
	
		Same as database/process/ecoSpold1 but for EcoSpold 02.
		
		
	GET database/process/ilcd?uuid=[process UUID]&mime=[MIME type]
		
		Same as database/process/ecoSpold1 but for ILCD.
		
	
	GET soda/process?baseUrl=[URL of the soda service]&uuid=[process UUID]&format=[LCA data format]&mime=[MIME type]
	
		Pulls a process data set from an soda4LCA web-service, caches it in the
		internal openLCA database, and returns it in the given format. For the
		format parameter the following values are allowed: ecoSpold1, ecoSpold2, 
		or ilcd.
	
		Example: http://localhost:8080/soda/process?baseUrl=http://eplca.jrc.ec.europa.eu/ELCD3/resource&uuid=76d6aaa4-37e2-40b2-994c-03292b600074&format=ecoSpold2
	
License
-------
Unless stated otherwise, all source code of the openLCA project is licensed 
under the [Mozilla Public License, v. 2.0](http://www.mozilla.org/MPL/2.0/). 
Please see the LICENSE.txt file in the root directory of the source code.
    