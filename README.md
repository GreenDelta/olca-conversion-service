openLCA conversion service
==========================
This project provides a web-service for converting LCA process data sets. It is
a Java application using the [Grizzly HTTP Server](https://javaee.github.io/grizzly/)
and the import and export functions of the
[openLCA core modules](https://github.com/GreenDelta/olca-modules). For caching
data sets, it can be configured to use an 
[embedded Derby database](https://db.apache.org/derby/papers/DerbyTut/embedded_intro.html)
or a MySQL database.

### Implementation status

* [x] EcoSpold 1 -> JSON LD
* [x] EcoSpold 1 -> ILCD
* [x] ILCD -> EcoSpold 1
* [x] ILCD -> JSON LD
* [x] JSON LD -> EcoSpold 1
* [x] JSON LD -> ILCD

## Installation
In order to install the application a Java Runtime Environment >= 8 needs to be
installed. Then, unzip the distribution package. It contains the application as
a single jar file, a startup script, a basic web UI, and a configuration file.
Running the startup script should directly start the server with the default
configurations:

```bash
cd olca-conv...
run
```

You should then be able to open the web UI in a browser at
[http://localhost](http://localhost).

### Configuration
The default configuration file looks like this (without the comments):

```javascript
{
  // the port of the application
  "port": 80,

  // path to a folder which is used as file cache and working directory 
  "workspace": "./workspace",

  // an optional path to a folder with the web UI
  "ui": "./web",
  
  // configuration of an embedded Derby database
  "derbyDB": {

    // path to a database folder, if it does not exist, an empty database is
    // created
    "folder": "./database"
  }
}
```

A configuration with a MySQL database could look like this (again without the
comments):

```javascript
// TODO: ...
```

## How it works
A client sends a conversion request to the conversion server which includes an
URL to the respective process data set, the format of this data set, and the
target format to which this data set should be converted. The conversion server
then fetches all the required data from the data server using a format specific
protocol. The converted data set with all associated resources (e.g. flow data
sets etc.) are cached on the conversion server and a link to this data set is
send back to the client:

![How it works](./conversion-service.png)

Note that the client could be itself the back-end of an HTTP server that sends
back the link to another client. The following data provider are currently
supported:

| Format | Data provider | Example URL |
|--------| -------------|------------ |
| EcoSpold 1 | Plain HTTP server providing the data set | [example from the Needs project](http://www.needs-project.org/needswebdb/scripts/download.php?fileid=4&type=xml) |
| ILCD | [soda4LCA](https://bitbucket.org/okusche/soda4lca) | [example from the ELCD database](http://eplca.jrc.ec.europa.eu/ELCD3/resource/processes/1a7da06d-e8b7-4ff1-920c-209e9009dbe0) |
| JSON LD | [openLCA CS](http://www.openlca.org/collaboration-server/) | e.g. http://localhost:8080/ws/public/browse/gdelta/refdata/PROCESS/e33fb2ad-5db5-4ee7-9486-515fce6fd46d |


## Building from source
This is a standard [Maven](https://maven.apache.org/) project using the Kotlin
compiler as Maven plugin. Thus you need to have Maven installed in order to
compile the project. Also, it depends on the [openLCA core modules](https://github.com/GreenDelta/olca-modules)
which need to be installed first. For Windows, there is a build script
`build.bat` which generates the distribution package:

```bash
cd olca-conversion-service
build
```

This will create the distribution package as `olca-conv_<version+date>.zip` in
the `target` folder. To include the user interface, you need to also build it
from source in the `ui` folder. We use [Gulp](https://gulpjs.com/) as build tool
for that:

```bash
cd olca-conversion-service/ui
npm install
gulp
```

The build script will package the UI in the distribution package if it can find
it.


## API

### Conversion Request

* **URL**

  `/api/convert`

* **Method**

  `POST`

* **Data Parameters**
        
  An JSON object that describes the conversion, e.g.:

```javascript
  {
    // the URL to the source data set
    "url": "http://eplca.jrc.ec.europa.eu/ELCD3/...",
    
    // the format of the source data set
    // possible values: "EcoSpold 1", "JSON LD", or "ILCD"
    "sourceFormat": "ILCD",
    
    // the target format into which the data set should be converted
    "targetFormat": "EcoSpold 1"
  }
```

* **Success Response:**

  * **Code**: 200 <br />
    **Content:** Name of the conversion result as plain text, e.g.
    `"123...2434_ECOSPOLD_1.zip"`

* **Error Response:**

  * **Code**: 501, Not Implemented <br />
    **Content:** Message about unimplemented data conversion or unknown format.
  
  * **Code**: 500, Internal Server Error <br />
      **Content:** conversion error


### Get Conversion Result

* **URL**

  `/api/result/:file`

* **Method**

  `GET`

* **URL Parameters**
        
  The file name of a conversion result, e.g. `"123...2434_ECOSPOLD_1.zip"`

* **Success Response:**

  * **Code**: 200 <br />
    **Content:** A zip file.

* **Error Response:**

  * **Code:** 404, Not Found <br />
    **Content:** `"File <name> does not exist."`
  

### Get Cached Data

* **URL**

```
  /api/database/processes
  /api/database/flows
  /api/database/flowProperties
  /api/database/unitGroups
```

* **Method**

  `GET`

License
-------
Unless stated otherwise, all source code of the openLCA project is licensed 
under the [Mozilla Public License, v. 2.0](http://www.mozilla.org/MPL/2.0/). 
Please see the LICENSE.txt file in the root directory of the source code.
    