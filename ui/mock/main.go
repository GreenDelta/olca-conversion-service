// A simple mock server for testing the user interface
package main

import (
	"fmt"
	"net/http"
	"time"
)

var requestCount = 0

func convert(w http.ResponseWriter, r *http.Request) {
	requestCount++
	fmt.Println("Called convert and wait a bit...")
	time.Sleep(5 * time.Second)
	w.Header().Set("Content-Type", "text/plain")
	if requestCount%2 == 0 {
		fmt.Println("respond with file")
		w.Write([]byte("abc...123_ILCD.zip"))
	} else {
		fmt.Println("respond with error")
		http.Error(w, "Some error", http.StatusNotAcceptable)
	}
}

func main() {
	h := http.NewServeMux()
	fs := http.FileServer(http.Dir("../build"))
	h.Handle("/", NoCache(fs))
	h.HandleFunc("/api/convert", convert)
	fmt.Println("starting mock server ...")
	http.ListenAndServe(":80", h)
}
