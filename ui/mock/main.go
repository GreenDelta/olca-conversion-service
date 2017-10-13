// A simple mock server for testing the user interface
package main

import (
	"net/http"
)

func main() {
	h := http.NewServeMux()
	fs := http.FileServer(http.Dir("../build"))
	h.Handle("/", NoCache(fs))
	http.ListenAndServe(":80", h)
}
