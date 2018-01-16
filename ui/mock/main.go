// A simple mock server for testing the user interface
package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

var requestCount = 0

// The Result of a conversion
type Result struct {
	ZipFile string `json:"zipFile"`
	Process string `json:"process"`
	Format  string `json:"format"`
}

func convert(w http.ResponseWriter, r *http.Request) {
	requestCount++
	fmt.Println("Called convert and wait a bit...")
	time.Sleep(2 * time.Second)
	w.Header().Set("Content-Type", "text/plain")
	if requestCount%2 == 0 {
		fmt.Println("respond with result")
		data, _ := json.Marshal(getSampleResult())
		w.Header().Set("Content-Type", "application/json")
		w.Write(data)
	} else {
		fmt.Println("respond with error")
		http.Error(w, "Some error", http.StatusNotAcceptable)
	}
}

func getSampleResult() *Result {
	r := Result{
		Format:  "ILCD",
		ZipFile: "abc...123_ILCD.zip"}
	r.Process = `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<processDataSet xmlns:common="http://lca.jrc.it/ILCD/Common"
  xmlns="http://lca.jrc.it/ILCD/Process" xmlns:fp="http://lca.jrc.it/ILCD/FlowProperty" xmlns:u="http://lca.jrc.it/ILCD/UnitGroup" version="1.1">
    <processInformation>
	  <dataSetInformation>
	    <common:UUID>e33fb2ad-5db5-4ee7-9486-515fce6fd46d</common:UUID>
		  <name>
		    <baseName xml:lang="en">p1</baseName>
		  </name>
	  </dataSetInformation>
	</processInformation>
</processDataSet>
`
	return &r
}

func main() {
	h := http.NewServeMux()
	fs := http.FileServer(http.Dir("../build"))
	h.Handle("/", NoCache(fs))
	h.HandleFunc("/api/convert", convert)
	fmt.Println("starting mock server ...")
	http.ListenAndServe(":80", h)
}
