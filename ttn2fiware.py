#!/usr/bin/env python
# Jose Castillo Lema - josecastillolema@gmail.com
from time import sleep
import NGSIClient


r = NGSIClient.getInfoFromTTN()
NGSIClient.createContext("node", "012AE716", NGSIClient.createAtributeArray(r))

