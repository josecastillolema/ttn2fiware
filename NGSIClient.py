import requests
import json

siteTTN = "http://thethingsnetwork.org/api/v0/nodes/012AE716"
#siteFIWARE = "http://orion.pad.lsi.usp.br:11026/"
siteFIWARE = "http://130.206.117.167:1026/"

def getInfoFromTTN():
  headers = {
        "Accept": "application/json",
  }
  r = requests.get(siteTTN, headers=headers)
  print "The response code is " + str(r.status_code)

  if r.status_code == 200:
    print "Message sent succesfully"
    #print r.text
    #print '-----'
    #print r.json()
    print '----------------'
    #print r.json()[0]
    #print r.json()[0]['data']
    return r.json()[0]

  elif r.status_code == 405:
    print "There was an error"

  else:
    print "Unknown result"

def createContext(type, id, attributes):
  body =  {
        "contextElements" : [
            {
                  "type": type,
                  "isPattern": "false",
                  "id": id,
	          "attributes": attributes
            }
        ],
	"updateAction": "APPEND"
  }
  print
  print json.dumps(body)
  print
  headers = {
        "Accept": "application/json",
        "Content-type": "application/json"
  }
  r = requests.post(siteFIWARE+"v1/updateContext", data=json.dumps(body), headers=headers)
  print "The response code is " + str(r.status_code)

  if r.status_code == 200:
    print "Message sent succesfully"
    print r.text

  elif r.status_code == 405:
    print "There was an error"

  else:
    print "Unknown result"


def getContext(type, id):
  body =  { 
	"entities" : [ 
		{
  			"type": type,
			"isPattern": "false",
			"id": id
		}
	] 
  }
  headers = {
	"Accept": "application/json",
	"Content-type": "application/json"
  }
  r = requests.post("http://130.206.82.80:1026/NGSI10/queryContext", data=json.dumps(body), headers=headers)
  print "The response code is " + str(r.status_code)

  if r.status_code == 200:
    print "The body is: " + r.text

  elif r.status_code == 405:
    print "There was an error"

  else:
    print "Unknown result"


def createMeasureArray(measureType, measureName, arrayValues):
  values = []
  for i in range(len(arrayValues)):
    valueJson = {
	"name": measureName + str(i),
	"type": measureType,
	"value": arrayValues[i]
    }
    values.append(valueJson)

  return values

def createAtributeArray(r):
  values = []
  for i in r:
    valueJson = {
	"name": str(i),
	"type": "string",
	"value": r[i]
    }
    values.append(valueJson)

  return values
