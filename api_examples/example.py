# An example for using the conversion API from Python using the requests
# package (http://docs.python-requests.org/en/master/)

import requests

API_ENDPOINT = "http://localhost/api/"


def main():
    setup = {
        "url": "http://localhost:8080/ILCD/processes/e33fb2ad-5db5-4ee7-9486-515fce6fd46d",
        "sourceFormat": "ILCD",
        "targetFormat": "JSON LD"
    }

    # convert the data set
    r = requests.post(API_ENDPOINT + "convert", json=setup)
    if r.status_code != 200:
        print("Conversion failed: " + r.text)
        exit(1)
    data = r.json()
    # data = {"zipFile": "...", "process": "...", "format": "..."}

    # download the converted data set
    r = requests.get(API_ENDPOINT + "result/" + data["zipFile"], stream=True)
    if r.status_code != 200:
        print("Failed to get result package: " + r.text)
        exit(1)
    with open(data["zipFile"], 'wb') as f:
        for chunk in r:
            f.write(chunk)


if __name__ == "__main__":
    main()
