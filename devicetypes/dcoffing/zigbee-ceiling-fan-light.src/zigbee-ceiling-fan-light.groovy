/*
 *  Zigbee Ceiling Fan/Light Controller Model#MR101Z by Hampton Bay
 *  This device handler is designed for the Home Depot Hampton Bay or
 *  Home Decorators Collection Universal Ceiling Fan/Light Premier Remote Control model#9943241 
 *
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
 *  A huge thanks to the SmartThings community specifically @Ranga for the foundational body of work to get it all going; 
 *  @chadck @johnconstantelo for the techniques used on Z-wave GE Smart Fan controller device handler,  
 *  all the forum developers like @erocm1231 @bamarayne @sticks18 that gave me direction.
 *  
 *

 Change Log
 17-03-23
            - 
            - all states are now syncing correctly across multiAttribute and standard tiles
            -nextState is not changing to blue background on all tiles
 17-03-22   -attempting to use dynamic label for when restting fan Speed to mimic remote
 			-need new command "fanOn" or goes back to last command state for fan
            -bug on main tile not changing to correct status 
            -main tile doesn't turn off fans speed
 17-03-21   -change color backgrounds on multiAttributes to show gray on OFF
 			-get Breeze to light blue for ON
            -experiment with Tile placement and decorator "flat" look
            -start building new multiAttribute tile for fan speed in first position 
 17-03-20   -fixed nextState defintions; transistioning to next state not correctly identified to next state
			-balance green hex valuses across all speeds; High #486e13 , MedHi #558216 , Med #669c1c , Low #79b821
  			-new multiAttribute primary for fan speed 
 17-03-17	-modifying primary tile for testing functionality
			-Tweaking the icons to have transparency in black layers to match gray, resizing icons again to fit tiles better
			-modifying primary tile for testing functionality
            -icons modified to 30% opacity on image layer, makes multiple icons for states unnecessary now
 17-03-16	-fixing icons scaling, changing text labels for testing look
			-upload icons to github for testing
 17-03-16	-initial upload

Things to Do / BUGS
 - 
 - main tile is still not operating fanON correctly due to; maybe dynamic state will fix this
 - transitioning to blue background is not happening on all standard tiles, it does on the multiAttribute if I manually trigger it there.
X - redo colors in each of the fan speeds in the multiAttribute tile
X- what is state "default" for on all tiles?
X - what is inactiveLabel: false for? ; deprecated
    Found this note cached in SmartThings Documentation
    http://stdavedemo.readthedocs.io/en/latest/device-type-developers-guide/tiles-metadata.html
        "* Note: You may see Device Handlers that use the inactiveLabel property. This is deprecated and has no effect."
X - can't change icon in primary tile
X - is bracket necessary on main([switch])
X - lots of buggy operation on tiles not staying synced or OnOff tile freezing up?
  - Can OnOff tile be made to toggle both fan and light simultaneously
X - what is , nextState: "turningOff" on onOFF tile for?
X - new icons are unreadable in some states? 
X - new icons are out of scale and too large for 2x2 tiles
 
 *
 */


metadata {
    definition (
    	name: "ZigBee Ceiling Fan-Light", namespace: "dcoffing", author: "Dale Coffing"){
        
        capability "Actuator"
        capability "Configuration"
        capability "Refresh"
        capability "Switch"
        capability "Switch Level"
        capability "Light"
        capability "Sensor" 
        capability "Polling"
        capability "Health Check"
        
/*		command "lowSpeed"
		command "medSpeed"
		command "highSpeed"
        attribute "currentState", "string"
        
        
		command "onOff"
        command "medHiSpeed"
        command "breezeSpeed"
		command "fanOn"
*/        
        command "fanOff"
        command "fanOne"
        command "fanTwo"
        command "fanThree"
        command "fanFour"
        command "fanAuto"
        attribute "fanMode", "string"
      
        

        fingerprint profileId: "0104", inClusters: "0000, 0003, 0004, 0005, 0006, 0008, 0202", outClusters: "0003, 0019", model: "HDC52EastwindFan"
    }
    
  
    tiles(scale: 2) {
    	// FAN SWITCH multiple attributes tile with actions named
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        	// All tiles must define a PRIMARY_CONTROL. Controls the background color of tile, and specifies the attribute to show on the Device list views
			tileAttribute ("device.fanMode", key: "PRIMARY_CONTROL") {
            
                //We can use the nextState option in state (single-attribute tiles) or attributeState (Multi-Attribute Tiles) to show 
                //that the device is transitioning to a next state. This is useful to provide visual feedback that the device state is transitioning.
                //When the attribute’s state does change, the tile will be updated according to the state defined for that attribute.
                //To define a transition state, simply define a state for the transition, and reference that state using the nextState option.
              
                attributeState "default", label:'ADJUSTING', action:"refresh.refresh", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"	//light blue bckgrd
				attributeState "fanFour", label:'HIGH', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#486e13", nextState: "turningOff"			// green4 bckgrnd
				attributeState "fanThree", label: 'MED-HI', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#558216", nextState: "turningOff"		//green3 bckground
				attributeState "fanTwo", label: 'MED', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#669c1c", nextState: "turningOff"			//green2 bckground
				attributeState "fanOne", label:'LOW', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"			//green1 bckgrnd
				attributeState "fanAuto", label:"BREEZE", action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#00A0DC", nextState: "turningOff"		//blue bckgrd
        		attributeState "fanOff", label:"FAN OFF", action:"fanOne", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"		//gray bckgrnd 
				attributeState "turningOn", action:"fanOne", label:"TURNINGON", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"	//light blue bckgrd
				attributeState "turningOff", action:"fanOff",     label:"TURNINGOFF", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"	//light blue bckgrd
   
            }

           	// LIGHT control dimmer tile with slider actions  
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"switch level.setLevel"
			}         
		}
        
      
    	// LIGHT standard flat tile with actions 
		standardTile("fanLight", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "off", label:'${name}', action: "switch.on",
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff" //, nextState:"turningOn"
			state "on", label:'${name}', action: "switch.off", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821" //, nextState:"turningOff"
//          state "turningOn", label:'Turning on', icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#79b821", nextState: "turningOff"
//   		state "turningOff", label:'Turning off', icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#ffffff", nextState: "turningOn" 
		}   
   
 		// fanSPEED standard tile with actions 
 		standardTile("fanOne", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
        	state "default", label: 'LOW', action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanOne", label:'LOW', action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
//            state "turningTwo", label:'ADJ MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
// 		 	  state "ADJUSTING.LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"       
			state "ADJUSTING.fanOne", label:'ADJ_LOW', action: "fanOne", icon:"st.Home.home30", backgroundColor: "#2179b8" 
        }
		
		standardTile("fanTwo", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanTwo", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
//           state "turningThree", label:'ADJ MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
//           state "ADJUSTING.MED", label:'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
//           state "ADJUSTING.MED", label:'MED', action: "fanTwo", icon:"st.Home.home30", backgroundColor: "#2179b8"        
        }
         
        standardTile("fanThree", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanThree", label: 'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
//			state "turningThree", label:'ADJ MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
//			state "ADJUSTING.MED-HI", label:'MED-HI', action: "fanThree", icon:"st.Home.home30", backgroundColor: "#2179b8"   
        } 
        
        standardTile("fanFour", "device.fanMode", inactiveLabel: false, width: 2, height:2) {
        	state "default", label: 'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanFour", label:'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
//			state "turningFour", label:"ADJ HIGH", action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
//          state "ADJUSTING.HIGH", label:'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
//			state "ADJUSTING.HIGH", label:'HIGH', action: "fanFour", icon:"st.Home.home30", backgroundColor: "#2179b8"   
        }
       
		// BREEZE standard flat tile with actions 		
		standardTile("fanBreeze", "device.fanMode", inactiveLabel: false, width:2, height:2, decoration: "flat") {
        	state "default", label: "Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#ffffff"
			state "fanAuto", label:"Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#00A0DC"
			state "turningBreeze", label:"ADJ Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#2179b8"
		}

//attempting to try label:'${name}' to pull out dynamic name to restore last state if possible

		standardTile("fanOff", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label:'FAN ON', action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#ffffff"
			state "fanOff", label:"FAN OFF", action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#79b821"
//			state "turningX", label:'ADJ FanOn', action: "fanOn", icon:"st.Home.home30", backgroundColor: "#2179b8"
        }					
//            state "turningOn", label:'ADJ FanOn', action: "fanOn", icon:"st.Home.home30", backgroundColor: "#2179b8" 
	        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
       }   

/*          // ON-OFF standard tile with actions named
        standardTile("onOff", "device.fanmode", inactiveLabel: false, width:2, height:2) {
			state "default", label:"on off", action:"fanOff",
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#ffffff"
			state "fanOff", label:"FanOff" , action:"fanOn", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#9ac959"
			state "turningOff", label:"Turning Off", action:"fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff17t5xfinal.png", backgroundColor: "#2179b8"
			state "turningOn", label:"Turning On", action:"fanOn",     	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#2179b8"
           }
*/

		// the tile named "switch" will appear in the Things view 
		main(["switch"])
        
        // tiles listed "switch", "onOff", etc will appear in the Device Details screen view (order is left-to-right, top-to-bottom)   "onOff", "lowSpeed", "medSpeed", "medHiSpeed", "highSpeed", "breezeSpeed", "fanOff",
		details(["switch", "fanLight","fanFour", "fanThree", "fanBreeze", "fanTwo", "fanOne", "fanOn", "fanOff", "refresh", "onOff"])


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
