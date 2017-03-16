
/**
 *  Copyright 2017 Dale Coffing
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
 *  Hampton Bay Universal Ceiling Fan/Light Controller Model#MR101Z 
 *  This device handler is specifically for the Home Depot Hampton Bay Universal Ceiling Fan/Light Premier Remote Control model#9943241 
 *
 *  Author: Dale Coffing, Ranga Pedamallu
 *  
 
 Change Log
  2017-03-16 upload icons to github for testing
  2017-03-16 initial upload

 */


metadata {
    definition (
    	name: "Hampton Bay Universal Ceiling Fan/Light Controller", 
        namespace: "dcoffing", 
        author: "Dale Coffing")
{
        
        capability "Actuator"
        capability "Configuration"
        capability "Refresh"
        capability "Switch"
        capability "Switch Level"
        capability "Light"
        capability "Sensor" 
        
        attribute "fanMode", "string"
        
        command "fanOff"
        command "fanOne"
        command "fanTwo"
        command "fanThree"
        command "fanFour"
        command "fanAuto"

        fingerprint profileId: "0104", inClusters: "0000, 0003, 0004, 0005, 0006, 0008, 0202", outClusters: "0003, 0019", model: "HDC52EastwindFan"
    }

    tiles(scale: 2) {
        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"switch level.setLevel"
            }
            tileAttribute ("power", key: "SECONDARY_CONTROL") {
                attributeState "power", label:'${currentValue}'
            }
        }
        standardTile("fanOff", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'Off', action: "fanOff", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningOff"
			state "fanOff", label:'Off', action: "fanOff", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningOff"
			state "turningOff", label:'Off', action: "fanOff", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningOff"
		}
		standardTile("fanOne", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
        	state "default", label: 'One', action: "fanOne", icon:"st.Lighting.light24", backgroundColor: "#ffffff", nextState: "turningOne"
			state "fanOne", label:'One', action: "fanOne", icon:"st.Lighting.light24", backgroundColor: "#79b821", nextState: "turningOne"
			state "turningOne", label:'One', action: "fanOne", icon:"st.Lighting.light24", backgroundColor: "#2179b8", nextState: "turningOne"
  		}
		standardTile("fanTwo", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'Two', action: "fanTwo", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningTwo"
			state "fanTwo", label: 'Two', action: "fanTwo", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningTwo"
            state "turningTwo", label:'Two', action: "fanTwo", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningTwo"
		}
		standardTile("fanThree", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'Three', action: "fanThree", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningThree"
			state "fanThree", label: 'Three', action: "fanThree", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningThree"
            state "turningThree", label:'Three', action: "fanThree", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningThree"
		}
        standardTile("fanFour", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'Four', action: "fanFour", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningFour"
			state "fanFour", label:'Four', action: "fanFour", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningFour"
			state "turningFour", label:'Four', action: "fanFour", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningFour"
		}
        standardTile("fanBreeze", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'Breeze', action: "fanAuto", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningBreeze"
			state "fanAuto", label:'Breeze', action: "fanAuto", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningBreeze"
			state "turningBreeze", label:'Breeze', action: "fanAuto", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningBreeze"
		}
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 6, height: 2) {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
        }


		main(["switch"])
		details(["switch", "fanOff", "fanOne", "fanTwo", "fanThree", "fanFour", "fanBreeze", "refresh"])
}
}
// Parse incoming device messages to generate events
def parse(String description) {
	log.debug "Parse description $description"
        def event = zigbee.getEvent(description)
    if (event) {
        log.info event
        if (event.name == "power") {
                event.value = (event.value as Integer) / 10
                sendEvent(event)
        }
        else {
            sendEvent(event)
        }
    }
	else {
	def map = [:]
	if (description?.startsWith("read attr -")) 
	{
		def descMap = zigbee.parseDescriptionAsMap(description)
		// Fan Control Cluster Attribute Read Response
		if (descMap.cluster == "0202" && descMap.attrId == "0000") 
		{
			map.name = "fanMode"
			map.value = getFanModeMap()[descMap.value]
		} 
	}// End of Read Attribute Response
	def result = null
	if (map) {
		result = createEvent(map)
	}
	log.debug "Parse returned $map"
	return result
    }
}

def getFanModeMap() { 
	[
    "00":"fanOff",
    "01":"fanOne",
    "02":"fanTwo",
    "03":"fanThree",
	"04":"fanFour",
	"06":"fanAuto"
	]
}

def off() {
    zigbee.off()
}

def on() {
    zigbee.on()
}

def setLevel(value) {
    zigbee.setLevel(value) + (value?.toInteger() > 0 ? zigbee.on() : [])
}

def ping() {
    return zigbee.onOffRefresh()
}

def refresh() {
    zigbee.onOffRefresh() + zigbee.levelRefresh() + zigbee.readAttribute(0x0202, 0x0000)
}

def configure() {
	log.info "Configuring Reporting and Bindings."
	def cmd = 
    [
	  //Set long poll interval
	  "raw 0x0020 {11 00 02 02 00 00 00}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  //Bindings for Fan Control
      "zdo bind 0x${device.deviceNetworkId} 1 1 0x006 {${device.zigbeeId}} {}", "delay 100",
      "zdo bind 0x${device.deviceNetworkId} 1 1 0x008 {${device.zigbeeId}} {}", "delay 100",
	  "zdo bind 0x${device.deviceNetworkId} 1 1 0x202 {${device.zigbeeId}} {}", "delay 100",
	  //Fan Control - Configure Report
      "zcl global send-me-a-report 0x006 0 0x10 1 300 {}", "delay 100",
       "send 0x${device.deviceNetworkId} 1 1", "delay 100",
      "zcl global send-me-a-report 0x008 0 0x20 1 300 {}", "delay 100",
       "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  "zcl global send-me-a-report 0x202 0 0x30 1 300 {}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100",
	  //Update values
      "st rattr 0x${device.deviceNetworkId} 1 0x006 0", "delay 100",
      "st rattr 0x${device.deviceNetworkId} 1 0x008 0", "delay 100",
	  "st rattr 0x${device.deviceNetworkId} 1 0x202 0", "delay 100",
	 //Set long poll interval
	  "raw 0x0020 {11 00 02 1C 00 00 00}", "delay 100",
	  "send 0x${device.deviceNetworkId} 1 1", "delay 100"
	]
    return cmd + refresh()
}

def fanAuto() {
	sendEvent("name":"fanMode", "value":"fanAuto")
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {06}"
    ]
    return cmds
    log.info "Turning On Breeze mode"
}
def fanOff() {
	sendEvent("name":"fanMode", "value":"fanOff")
	def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {00}"
    ]
    return cmds
    log.info "Turning fan Off"
}
def fanOne() {
	sendEvent("name":"fanMode", "value":"fanOne")
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {01}"
    ]
    return cmds
    log.info "Setting fan speed to One"
}
def fanTwo() {
	sendEvent("name":"fanMode", "value":"fanTwo")
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {02}"
    ]
    return cmds
    log.info "Setting fan speed to Two"
}
def fanThree() {
	sendEvent("name":"fanMode", "value":"fanThree")
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {03}"
    ]
    return cmds
    log.info "Setting fan speed to Three"
}
def fanFour() {
	sendEvent("name":"fanMode", "value":"fanFour")
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {04}"
    ]
    return cmds
    log.info "Setting fan speed to Four"
}