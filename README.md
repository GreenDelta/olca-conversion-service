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
		
	GET database/flows
	
	
License
-------
Unless stated otherwise, all source code of the openLCA project is licensed 
under the [Mozilla Public License, v. 2.0](http://www.mozilla.org/MPL/2.0/). 
Please see the LICENSE.txt file in the root directory of the source code.
    