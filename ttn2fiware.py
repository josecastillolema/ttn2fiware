#!/usr/bin/env python
# Jose Castillo Lema - josecastillolema@gmail.com
from time import sleep
import NGSIClient

r = NGSIClient.getInfoFromTTN()
r = NGSIClient.cleanInfoFromTTN(r)
NGSIClient.createContext("node", "012AE716", NGSIClient.createAtributeArray(r))
NGSIClient.getContext("node", "012AE716")

