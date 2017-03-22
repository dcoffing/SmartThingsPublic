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
 *  @chadck for the techniques used on Z-wave controller; @Sticks18 tile coloring ideas and so much else; 
 *  all the forum developers like @erocm1231 @bamarayne that gave me input to keep this moving along.
 *  
 *

 Change Log
 			- need new command "fanOn"?
            -bug on main tile not changing to correct status 
            -main tile doesn't turn off fans speed
 17-03-21   -change color backgrounds on multiAttributes to show gray on OFF
            -start building new multiAttribute tile for fan speed in first position 
 17-03-20   -fixed nextState defintions? transistioning to next state not current state
			-new colors for 4 speeds MedHiColor #558216 MedColor #669c1c HighColor #486e13
  			-new multiAttribute primary for fan speed 
 17-03-17	-modifying primary tile for testing functionality
			-Tweaking the icons to have transparency in black layers to match gray, resizing icons again to fit tiles better
			-modifying primary tile for testing functionality
 17-03-16	-fixing icons scaling, changing text labels for testing look
			-upload icons to github for testing
 17-03-16	-initial upload

Things to Do - BUGS
 - verify colors in each of the fan speeds in the multiAttribute tile
 - what is state "default" for on all tiles?
 - what is inactiveLabel: false for? please remove; deprecated
    Found this note cached in SmartThings Documentation
    http://stdavedemo.readthedocs.io/en/latest/device-type-developers-guide/tiles-metadata.html
        "* Note: You may see Device Handlers that use the inactiveLabel property. This is deprecated and has no effect."


 - can't change icon in primary tile
 - is bracket necessary on main([switch])
 - lots of buggy operation on tiles not staying synced or OnOff tile freezing up?
 - OnOff tile should toggle both fan and light simultaneously
 - what is , nextState: "turningOff" on onOFF tile for?
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
        
		command "lowSpeed"
		command "medSpeed"
		command "highSpeed"
        attribute "currentState", "string"
        
        
		command "onOff"
        command "medHiSpeed"
        command "breezeSpeed"
		command "fanOn"
        
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
    	// multiple attributes tile with actions named
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        	// All tiles must define a PRIMARY_CONTROL. Controls the background color of tile, and specifies the attribute to show on the Device list views
//			tileAttribute ("device.currentState", key: "PRIMARY_CONTROL") {
			tileAttribute ("device.fanMode", key: "PRIMARY_CONTROL") {
            
                // We can use the nextState option in state (single-attribute tiles) or attributeState (Multi-Attribute Tiles) to show that the device
                // is transitioning to a next state. This is useful to provide visual feedback that the device state is transitioning. 
                // When the attribute’s state does change, the tile will be updated according to the state defined for that attribute.
                //
                //We can use the nextState option in state (single-attribute tiles) or attributeState (Multi-Attribute Tiles) to show 
                //that the device is transitioning to a next state. This is useful to provide visual feedback that the device state is transitioning.
                //When the attribute’s state does change, the tile will be updated according to the state defined for that attribute.
                //To define a transition state, simply define a state for the transition, and reference that state using the nextState option.
                
//				attributeState "default", label:'ADJUSTING', action:"refresh.refresh", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"   light blue bckgrd
                attributeState "default", label:'ADJUSTING', action:"refresh.refresh", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
                
//				attributeState "HIGH", label:'HIGH', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"			 green4 bckgrnd?
				attributeState "fanFour", label:'HIGH', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#486e13", nextState: "turningOff"

//              attributeState "MED-HI", label:'MED-HI', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#558216", nextState: "turningOff"         green3 bckground
				attributeState "fanThree", label: 'MED-HI', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#558216", nextState: "turningOff"
                
//				attributeState "MED", label:'MED', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#669c1c", nextState: "turningOff"               green2 bckground
				attributeState "fanTwo", label: 'MED', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#669c1c", nextState: "turningOff"
                
//				attributeState "LOW", label:'LOW', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"   			  light green bckgrnd
				attributeState "fanOne", label:'LOW', action:"fanOff", icon:"st.Lighting.light24", backgroundColor:"#79b821", nextState: "turningOff"
			
//				attributeState "OFF", label:'OFF', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"   				  gray bckgrnd                
				attributeState "fanOff", label:"FanOff", action:"fanOn", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "turningOn"
                            
//				attributeState "turningOn", action:"switch.on", label:'TURNINGON', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"     light green bckgrnd
				attributeState "turningOn", action:"fanOn", label:"TURNINGON", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOn"
                
//				attributeState "turningOff", action:"switch.off", label:'TURNINGOFF', icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"  light blue bckgrd
				attributeState "turningOff", action:"fanOff",     label:"TURNINGOFF", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "turningOff"
			}
//			tileAttribute ("device.level", key: "SECONDARY_CONTROL") {
				// SECONDARY_CONTROL used to display textual information below the PRIMARY_CONTROL
//				attributeState "level", label:'${currentValue}%'
//			}
           	// LIGHT dimmer tile with slider actions  
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"switch level.setLevel"
			}
            
		}
        
      
    	// LIGHT standard flat tile with actions 
		standardTile("fanLight", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "off", label:'${name}', action: "switch.on", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff" //, nextState:"turningOn"
			state "on", label:'${name}', action: "switch.off", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821" //, nextState:"turningOff"
//          state "turningOn", label:'Turning on', icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#79b821", nextState: "turningOff"
//   		state "turningOff", label:'Turning off', icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#ffffff", nextState: "turningOn" 
		}   
            
        
        // ON-OFF standard tile with actions named
        standardTile("onOff", "device.currentState", inactiveLabel: false, width:2, height:2) {
			state "default", label:'${name}', action:"fanOff",
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#ffffff", nextState: "turningOff"
			state "fanOff", label:"FanOff" , action:"fanOn", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff175xfinal.png", backgroundColor: "#9ac959", nextState: "turningOff"
			state "turningOff", label:"Turning Off", action:"fanOff", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/OnOff17t5xfinal.png", backgroundColor: "#2179b8", nextState: "turningOn"
			state "turningOn", label:"Turning On", action:"fanOn", 
            	icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor:"#2179b8", nextState: "turningOff"
           }
//attempting to try label:'${name}' to pull out dynamic name to restore last state if possible
		standardTile("fanOff", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'FanOff', action: "fanOff", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningOff"
			state "fanOff", label:'${name}', action: "fanOn", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningOn"
			state "turningOn", label:'ADJ FanOn', action: "fanOn", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningOn"
		}
/*       EXAMPLE FROM ZWAVE
		standardTile("lowSpeed", "device.currentState", inactiveLabel: false, width: 2, height: 2) {
        	state "default", label: 'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#ffffff"
			state "LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#79b821"
			state "ADJUSTING.LOW", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8"
  		}
*/
     
 		// fanSPEED standard flat tile with actions 
 		standardTile("fanOne", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
        	state "default", label: 'LOW', action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff", nextState:"turningOne"
			state "fanOne", label:'LOW', action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821", nextState:"turningTwo"
//			state "turningOne", label:'ADJ LOW', action: "fanOne", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8", nextState:"turningOne"
            state "turningTwo", label:'ADJ MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8", nextState:"turningTwo"
        }
		
		standardTile("fanTwo", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanTwo", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
//          state "turningTwo", label:'ADJ MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
            state "turningThree", label:'ADJ MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8", nextState:"turningThree"

		}
        
		// BREEZE standard flat tile with actions 		
		standardTile("fanBreeze", "device.fanMode", inactiveLabel: false, width:2, height:2, decoration: "flat") {
        	state "default", label: "Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#ffffff", nextState: "turningBreeze"
			state "fanAuto", label:"Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#00A0DC", nextState: "turningBreeze"
			state "turningBreeze", label:"ADJ Breeze", action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#2179b8", nextState: "turningBreeze"
		}
        
        standardTile("fanThree", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanThree", label: 'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
            state "turningThree", label:'ADJ MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
		} 
        standardTile("fanFour", "device.fanMode", inactiveLabel: false, width: 2, height:2) {
        	state "default", label: 'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff"
			state "fanFour", label:'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821"
			state "turningFour", label:"ADJ HIGH", action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8"
		}       

	        
        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
       }   
	
      
        
        
//-------------------------------------------  
/*        standardTile("fanOff", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'FanOff', action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff", nextState: "turningOff"
			state "fanOff", label:'FanOff', action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821", nextState: "turningOff"
			state "turningOff", label:'FanOff', action: "fanOff", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#2179b8", nextState: "turningOff"
		}

		standardTile("fanOne", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
        	state "default", label: 'LOW', action: "fanOne", icon:"st.Home.home30", backgroundColor: "#ffffff", nextState: "turningOne"
			state "fanOne", label:'LOW', action: "fanOne", icon:"st.Home.home30", backgroundColor: "#79b821", nextState: "turningOne"
			state "turningOne", label:'LOW', action: "fanOne", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState: "turningOne"
  		}
		
		standardTile("fanTwo", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff", nextState: "turningTwo"
			state "fanTwo", label: 'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821", nextState: "turningTwo"
            state "turningTwo", label:'MED', action: "fanTwo", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8", nextState: "turningTwo"
		}
		standardTile("fanThree", "device.fanMode", inactiveLabel: false, width: 2, height: 2) {
			state "default", label: '99%', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#ffffff", nextState: "turningThree"
			state "fanThree", label: 'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#79b821", nextState: "turningThree"
            state "turningThree", label:'MED-HI', action: "fanThree", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Light175xfinal.png", backgroundColor: "#2179b8", nextState: "turningThree"
		}
        standardTile("fanFour", "device.fanMode", inactiveLabel: false, width: 2, height:2) {
        	state "default", label: 'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#ffffff", nextState: "turningFour"
			state "fanFour", label:'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#79b821", nextState: "turningFour"
			state "turningFour", label:'HIGH', action: "fanFour", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Fan175xfinal.png", backgroundColor: "#2179b8", nextState: "turningFour"
		}
        
      	standardTile("fanBreeze", "device.fanMode", inactiveLabel: false, width:2, height:2) {
        	state "default", label: 'Breeze', action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#ffffff", nextState: "turningBreeze"
			state "fanAuto", label:'Breeze', action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#79b821", nextState: "turningBreeze"
			state "turningBreeze", label:'Breeze', action: "fanAuto", icon:"https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/devicetypes/dcoffing/hampton-bay-universal-ceiling-fan-light-controller.src/Breeze175xfinal.png", backgroundColor: "#2179b8", nextState: "turningBreeze"
		}

        standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
       }
*/



		  // the tile named "switch" will appear in the Things view 
		main(["switch"])
        
        // tiles listed "switch", "onOff", etc will appear in the Device Details screen view (order is left-to-right, top-to-bottom)   "onOff", "lowSpeed", "medSpeed", "medHiSpeed", "highSpeed", "breezeSpeed", "fanOff",

		details(["switch", "fanLight","fanFour", "fanThree", "fanBreeze", "fanTwo", "fanOne", "fanOn", "fanOff", "OnOff", "refresh"])


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
