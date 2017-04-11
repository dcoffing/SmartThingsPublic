/**
 *  Zigbee Ceiling Fan-Light Controller - CHILD
 *  Copyright 2017 Stephan Hackett
 *
 *  Model#MR101Z by Hampton Bay
 *  This device handler is designed for the Home Depot Hampton Bay or
 *  Home Decorators Collection Universal Ceiling Fan/Light Premier Remote Control model#9943241 
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  
 *
 Change Log
  
 17-04-11 Renamed and changed Parent app name

**/

metadata {
	definition (name: "Zigbee Ceiling Fan-Light CHILD", namespace: "dcoffing", author: "Stephan Hackett") {
		capability "Actuator"
        capability "Refresh"
        capability "Switch"
        capability "Light"
        capability "Sensor" 
        capability "Momentary"
   }     


tiles(scale: 2) {    	
	
	standardTile("fanMod", "switch", width: 2, height: 2) {
		state "Off", label:"Push", action: "push", backgroundColor: "#ffffff", nextState: "on"
		state "On", label:"Push", action: "push", backgroundColor: "#79b821"		 
	} 
    	standardTile("refresh", "refresh", width: 2, height: 2, decoration: "flat") {
		state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
	}
    
    main(["fanMod"])
        
	details(["fanMod", "refresh"])
    
    
}    


}
      
def push() {
log.info "CHILD PUSH RECEIVED"
	sendEvent(name: "switch", value: "on", isStateChange: true, displayed: false)
	sendEvent(name: "switch", value: "off", isStateChange: true, displayed: false)
	sendEvent(name: "momentary", value: "pushed", isStateChange: true)
    
	def myChildId = device.deviceNetworkId
	def myParentId = parent.deviceNetworkId

	switch(myChildId) {
		case "${myParentId}-0":
    		parent.fanOff()
      	break
        case "${myParentId}-1":
    		parent.fanOne()
    	break
    	case "${myParentId}-2":
    		parent.fanTwo()
    	break
    	case "${myParentId}-3":
    		parent.fanThree()
   		break
    	case "${myParentId}-4":
    		parent.fanFour()
    	break
    	case "${myParentId}-5":
    		parent.Auto()
      	break
        case "${myParentId}-6":
    		parent.powerOn()
      	break
        case "${myParentId}-7":
    		parent.powerOff()
      	break
 }
        
}

def on() {
	push()        
}

def off() {
	push()        
}

def refresh() {

}
