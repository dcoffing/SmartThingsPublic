/**
 *  Zigbee Ceiling Fan-Light Controller - PARENT
 *  Copyright 2017 Stephan Hackett
 *
 *  Model#MR101Z by Hampton Bay
 *  This device handler is designed for the Home Depot Hampton Bay or
 *  Home Decorators Collection Universal Ceiling Fan/Light Premier Remote Control model#9943241 
 *  Incorporates contributions from:
 *
 *  Ranga Pedamallu
 *  Dale Coffing 
 *
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
  
 17-04-09 Added version tile
          Changed color back to darker to make it easier to read speeds.
          Remove fanOff tile; Not needed since fanOff is accomplished now at each speed button
          move Master off Thing View back to device details view
          Move multiAttribute back to Thing View and modify the actions to see what may be best. I like the master action here.
*/


metadata {
	definition (name: "Zigbee Ceiling Fan-Light", namespace: "dcoffing", author: "Stephan Hackett, Ranga Pedamallu, Dale Coffing") {
	capability "Actuator"
        capability "Configuration"
        capability "Refresh"
        capability "Switch"
        capability "Switch Level"
        capability "Light"
        capability "Sensor" 
        capability "Polling"
        capability "Health Check"
   
        command "fanOff"
        command "fanOne"
        command "fanTwo"
        command "fanThree"
        command "fanFour"
        command "fanAuto"
        command "powerOn"
        command "powerOff"
        
        attribute "fanMode", "string"
        attribute "masterMode", "string"
        attribute "lastFanMode", "string"
        attribute "lastLightMode", "string"
      
	fingerprint profileId: "0104", inClusters: "0000, 0003, 0004, 0005, 0006, 0008, 0202", outClusters: "0003, 0019", model: "HDC52EastwindFan"
    }
    
    preferences {
    	page(name:"modesToExpose", title:"Please select the fan options you wish to expose to SmartApps")
            section("test") {
            input(name: "f0", type: "bool", title: "Expose Fan Mode Off")
         	input(name: "f1", type: "bool", title: "Expose Fan Mode Low")
            input(name: "f2", type: "bool", title: "Expose Fan Mode Med")
            input(name: "f3", type: "bool", title: "Expose Fan Mode Med-Hi")
            input(name: "f4", type: "bool", title: "Expose Fan Mode High")
            input(name: "f5", type: "bool", title: "Expose Fan Mode Breeze")
            input(name: "f6", type: "bool", title: "Expose Fan Master Power On")
            input(name: "f7", type: "bool", title: "Expose Fan Master Power Off")            
       }
   // }
    }	
    
    
    
    tiles(scale: 2) {    	
	multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {        	
		tileAttribute ("fanMode", key: "PRIMARY_CONTROL") {			
			attributeState "fanFour", label:"HIGH", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#486e13", nextState: "turningOff"
			attributeState "fanThree", label:"MED-HI", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#558216", nextState: "turningOff"
			attributeState "fanTwo", label:"MED", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#669c1c", nextState: "turningOff"
			attributeState "fanOne", label:"LOW", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"
			attributeState "fanAuto", label:"BREEZE", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#00A0DC", nextState: "turningOff"
        	attributeState "fanOff", label:"FAN OFF", action:"powerOn", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"
			attributeState "turningOn", action:"fanFour", label:"TURNING ON", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"
			attributeState "turningOff", action:"fanOff", label:"TURNING OFF", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
        }  
        tileAttribute ("level", key: "SLIDER_CONTROL") {
			attributeState "level", action:"switch level.setLevel"
		}         
	} 
/*	standardTile("fanLight", "switch", width: 2, height: 2, decoration: "flat") {
		state "Off", label:"Off", action: "switch.on", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff", nextState:"turningOn"
		state "On", label:"On", action: "switch.off", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821", nextState:"turningOff"
		state "turningOn",  label:"Turning On", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#79b821", nextState: "turningOff"
		state "turningOff", label:"Turning Off",icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#ffffff", nextState: "turningOn" 
	} 
*/  
		standardTile("fanLight", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "off", label:'${name}', action: "switch.on",
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff", nextState:"turningOn"
			state "on", label:'${name}', action: "switch.off", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821", nextState:"turningOff"
			state "turningOn",  label:"TURN ON", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#79b821", nextState: "turningOff"
			state "turningOff", label:"TURN OFF",icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#ffffff", nextState: "turningOn" 
		} 
 	standardTile("fanOne", "fanMode", width: 2, height: 2) {
        state "default", label:"LOW", action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#ffffff", nextState: "turningOn"
		state "fanOne", label: "LOW", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#79b821", nextState: "turningOff"
        state "turningOne", label:"ADJUST", action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#2179b8"
        } 		
	standardTile("fanTwo", "fanMode", width: 2, height: 2) {
		state "default", label:"MED", action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#ffffff", nextState: "turningTwo"
		state "fanTwo", label: "MED", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#669c1c", nextState: "turningTwo"
        state "turningTwo", label:"ADJUST", action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#2179b8"
        }         
 	standardTile("fanThree", "fanMode", width: 2, height: 2) {
		state "default", label: "MED-HI", action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#ffffff", nextState: "turningThree"
		state "fanThree", label:"MED-HI", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#558216", nextState: "turningThree"
        state "turningThree", label:"ADJUST", action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#2179b8"
        }        
   	standardTile("fanFour", "fanMode", width: 2, height:2) {
        state "default", label:"HIGH", action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#ffffff", nextState: "turningFour"
		state "fanFour", label:"HIGH", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#486e13", nextState: "turningFour"
		state "turningFour", label:"ADJUST", action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png",
        backgroundColor: "#2179b8"
        }        		
	standardTile("fanBreeze", "fanMode", width:2, height:2, decoration: "flat") {
        state "default", label:"Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png",
        backgroundColor: "#ffffff", nextState: "turningBreeze"
		state "fanAuto", label:"Breeze", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png",
        backgroundColor: "#00A0DC", nextState: "turningBreeze"
		state "turningBreeze", label:"ADJUST", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png",
        backgroundColor: "#2179b8"
	} 
/*  Not needed since fanOff is accomplished now at each speed button
standardTile("fanOff", "fanMode", width:2, height:2) {
        state "default", label:"FAN OFF",action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#ffffff", nextState: "turningOff"
		state "fanOff", label:"FAN OFF", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#79b821", nextState: "turningOff"
        state "turningOff", label:"ADJUST", action: "", icon:"st.Home.home30", backgroundColor: "#2179b8"
    }
*/    
    standardTile("masterPower", "masterMode", width:2, height:2, decoration: "flat") {        
		state "on", label:"ON", action:"powerOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png",
        backgroundColor:"#79b821", nextState: "turningOff"
		state "off", label:"OFF", action:"powerOn", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png",
        backgroundColor:"#ffffff", nextState: "turningOn"
		state "turningOn", action:"", label:"TURNING ON", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png",
        backgroundColor:"#2179b8"
		state "turningOff", action:"", label:"TURNING OFF", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png",
        backgroundColor:"#2179b8"
    }
   	standardTile("refresh", "refresh", decoration: "flat", width: 2, height: 2) {
		state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
	}
	valueTile("version", "version", width: 2, height: 2) {
    	state "version", label:"Beta Version"+"\r\n"+"0.041017a"+"\r\r\n"+"OFF to ON restores"+"\r\n"+"to last state"
		}
    
	main(["switch"])
        
	details(["switch", "fanLight", "fanFour", "fanThree", "fanBreeze", "fanTwo", "fanOne", "masterPower", "version", "refresh"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
		log.debug "Parse description $description"           
        def event = zigbee.getEvent(description)
    	if (event) {
        	log.info "ENTER LIGHT"
        	//log.info event
        	if (event.name == "power") {
            	log.info "LIGHT - SPECIAL POWER"
                event.value = (event.value as Integer) / 10                
                sendEvent(event)
        	}
        	else {
            	log.info "LIGHT - SEND EVENT"
                log.info event
            	sendEvent(event) 
                if(event.value == on || event.value == off) sendEvent("name":"masterMode","value":setMaster(event.value))
              
        	}        
    	}
		else {
        	log.info "ENTER FAN"
			def map = [:]
			if (description?.startsWith("read attr -")) {
            	log.info "FAN - READ"
				def descMap = zigbee.parseDescriptionAsMap(description)
				// Fan Control Cluster Attribute Read Response               
                log.info descMap
				if (descMap.cluster == "0202" && descMap.attrId == "0000") {
                	log.info "FAN - READ - MODE"
					map.name = "fanMode"
					map.value = getFanModeMap()[descMap.value]
				} 
			}	// End of Read Attribute Response
			def result = null
            def result2 = null
            if (map) {
            	log.info "FAN - CREATE EVENT"                
				result = createEvent(map)
                result2 = createEvent("name":"masterMode","value":setMaster(map.value))                 
			}
			log.debug "Parse returned $map"          	          
            log.info "EXIT FAN and PARSE"
			return [result, result2] 
    	}
        
        log.info "EXIT PARSE"        
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

def getFanName() { 
	[
    0:"Fan Off",
    1:"Low",
    2:"Med",
    3:"Med-Hi",
	4:"High",
    5:"Breeze",
    6:"Master On",
    7:"Master Off"
	]
}

def off() {
	log.info "SWITCH OFF RUN"
    zigbee.off()
    
    
}

def on() {
	log.info "SWITCH ON RUN"
    zigbee.on()
    
}

def setLevel(value) {
	log.info "SWITCH LEVEL RUN"
    zigbee.setLevel(value) + (value?.toInteger() > 0 ? zigbee.on() : [])        
}

def ping() {
	log.info "PING RUN"
    return zigbee.onOffRefresh()
}

def updated() {
	log.info "UPADATE RUN"
	initialize()
}

def installed() {
	log.info "INSTALLED RUN"
	initialize()

	//createChildDevices()
    //response(refresh() + configure())
}

def initialize() {
log.info "START INITIALIZE"
//app.label==app.name?app.updateLabel(defaultLabel()):app.updateLabel(app.label)
// Save the device label for updates by updated()
state.oldLabel = device.label
// Add child devices for all five fam modes   
    
for(i in 0..7) {
   	log.info "create Fanmode Loop ${i}"
    def childDevice = getChildDevices()?.find {
        it.device.deviceNetworkId == "${device.deviceNetworkId}-${i}"
    }
    if(settings."f${i}"){                   
        if (!childDevice) {        
        	childDevice = addChildDevice("Home Decorators Fan Mode", "${device.deviceNetworkId}-${i}", null,[completedSetup: true, label: "${device.displayName} (${getFanName()[i]})", isComponent: false, componentName: "fanMode${i}", componentLabel: "FanMode ${i}"])
         	response(refresh() + configure())
            log.info "Creating child fan mode ${childDevice}"  
		}
       	else {
          log.info "Child already exists"
          
		}
	}
    else {
    	if (childDevice) {        
        	childDevice = deleteChildDevice("${device.deviceNetworkId}-${i}")
         	//childDevice.refresh()
            response(refresh() + configure())
            log.info "Deleting child fan mode ${childDevice}" 
        }
    }
}
        log.info "END INITIALIZE"
}


//private void createChildDevices() {
//log.info "START CHILD DEVICE CREATE"
        // Save the device label for updates by updated()
//        state.oldLabel = device.label
        // Add child devices for all five outlets of Zooz Power Strip
//        for (i in 1..4) {
//        log.info "START LOOP CHILD"
//        	addChildDevice("Home Decorators Fan Mode", "${device.deviceNetworkId}-${i}", null,[completedSetup: true, label: "${device.displayName} (FANMODE${i})", isComponent: true, componentName: "fanMode$i", componentLabel: "FanMode $i"])
//        }
//}




def refresh() {
log.info "REFRESH RUN"
log.info getChildDevices()
log.debug "settings are ${settings}"
//def test="f1"
//for (i in 1..3) {
//log.info settings."f${i}"
//}
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
log.info "FAN AUTO RUN"
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {06}"
    ]
    log.info "Turning On Breeze mode"    
    return cmds    
}

def fanOff() {
log.info "FAN OFF RUN"
log.info "${device.deviceNetworkId}"
	def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {00}"
    ]
    log.info "Turning fan Off"    
    return cmds    
}

def fanOne() {
log.info "FAN ONE RUN"
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {01}"
    ]
    log.info "Setting fan speed to One"
    return cmds    
}

def fanTwo() {    
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {02}"
    ]
    log.info "Setting fan speed to Two"
    return cmds    
}

def fanThree() {	
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {03}"
    ]
    log.info "Setting fan speed to Three"
    return cmds    
}

def fanFour() {	
    def cmds=[
	"st wattr 0x${device.deviceNetworkId} 1 0x202 0 0x30 {04}"
    ]
    log.info "Setting fan speed to Four"    
    return cmds    
}

def powerOn(){
log.info "POWER ON RUN"
	def lastFan =  device.currentValue("lastFanMode")
	def lastLight = device.currentValue("lastLightMode")
    return [zigbee."$lastLight"(), "$lastFan"()]
	
}

def powerOff() {
log.info "POWER OFF RUN"
	def fanNow = device.currentValue("fanMode")
    def lightNow = device.currentValue("switch")
	sendEvent("name":"lastFanMode", "value":fanNow)
    sendEvent("name":"lastLightMode", "value":lightNow)
    sendEvent("name":"masterMode", "value":"off", displayed:false)
    return [zigbee.off(),fanOff()]
}

def setMaster(myVal) {
log.info "SETMASTER RUN"
    def newMode = null
	def fanNow = device.currentValue("fanMode")
	def lightNow = device.currentValue("switch")
    switch(myVal) {
    	case ["on", "fanOne", "fanTwo", "fanThree", "fanFour", "fanAuto"]:
        log.info "CASEon1234 myVal=${myVal}"
        log.info "CASE fannow=${fanNow}"
        log.info "CASE lightNow=${lightNow}"
        	newMode = "on"
        break
        case "off":
        log.info "CASElo myVal=${myVal}"
        log.info "CASE fannow=${fanNow}"
        log.info "CASE lightNow=${lightNow}"
        	if(fanNow=="fanOff") newMode = "off" else newMode = "on"
        break
        case "fanOff":
        log.info "CASEfo myVal=${myVal}"
        log.info "CASE fannow=${fanNow}"
        log.info "CASE lightNow=${lightNow}"
        	if(lightNow == "off") newMode = "off" else newMode = "on"
        break
        default:  
        log.info "CASEdef myVal=${myVal}"
        log.info "CASE fannow=${fanNow}"
        log.info "CASE lightNow=${lightNow}"
        return
        break
	} 
    log.info "NEWMODE RETURNED: ${newMode}"
	return newMode
}
