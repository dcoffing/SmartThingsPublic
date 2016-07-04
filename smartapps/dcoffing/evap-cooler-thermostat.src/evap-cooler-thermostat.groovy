/*
   Virtual Thermostat for Evaporative Coolers .
   Copyright 2016 Dale Coffing, SmartThings
   
   This smartapp provides automatic control for Evaporative Coolers (single or two-speed) using 
   any temperature sensor. On a call for cooling the water pump is turned on and given two minutes
   to wet pads before fan low speed is enabled. The fan high speed is turned on if the temperature 
   continues to rise above the adjustable differential. There is an optional motion override.
   
   It requires these hardware devices; any temperature sensor, a switch for Fan On-Off, a switch
   for pump. For two speed control is desired another switch will be necessary.
   I suggest a Remotec ZFM-80 15amp relay for fan motor on-off, if you desired both pump and fan speed
   then Enerwave ZWN-RSM2 dual 10amp relays to control pump and the second relay to control hi-lo speed
   via Omoron LY1F SPDT 15amp relay. For only pump control any switch could work like the Enerwave ZWN-RSM1S
   or Monoprice #11989 Z-Wave In-Wall On/Off module
    

    
  Change Log
  2016-07-04  b. removed dynamic feedback section, bug preventing saving, minor grammitical edits
  	.. changed true to false install on dynamicPage(name: "aboutPage", title: none, install: false
  2016-07-02b. fixed mode bug not shutting of evap by changing mode technique, cleaned up code with shutdownEvap() 
  	a. modify dynamic feedback of inputs to be via paragraph technique
  2016-07-01 changed user select mode method, changed default delay-on to 1.5, removed the default setpoint because
  			ST still requires the user to make a change to get around Required input flag.
  2016-06-30 added dynamic temperature display readout to Room Setpoint Temp input for ease of troubleshooting
  2016-06-28 x.1 version update
  	f. added submitOnChange for motion so to skip minutes input next if no motion selected
	e. changed order of inputs for better logic flow
	d. added separate input page for only advanced options
	c. fixed bug in High Speed startup assuming fan/pump was already running
	b. renamed fanHiSpeed to fanSpeed for more generic use, added 0.0 on timer selection
	a. changed motion detector minutes input only if motion selected submitOnChange
  2016-06-22e added single speed default
	d. change user guide content
	c. modified icon to fan style, breeze style for comparison
	b. moved pump input to first position (required), made other selections not required	for those with single speed motor
    a. added icons
  2016-06-21 modify 3-speed-ceiling-fan-thermostat code for outlets

  
  Known Behavior from original Virtual Thermostat code
  -(fixed) when SP is updated, temp control isn't evaluated immediately, an event must trigger like change in temp, motion
  - if load is previously running when smartapp is loaded, it isn't evaluated immediately to turn off when SP>CT
 
   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
   in compliance with the License. You may obtain a copy of the License at: www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
   for the specific language governing permissions and limitations under the License.

*/

definition(
    name: "Evap Cooler Thermostat",
    namespace: "dcoffing",
    author: "Dale Coffing, SmartThings",
    description: "Automatic control for an Evaporative Cooler with a 2-speed motor, water pump and any temp sensor.",
    category: "My Apps",
	iconUrl: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/ect125x125.png", 
   	iconX2Url: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/ect250x250.png",
	iconX3Url: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/ect250x250.png",
)

preferences {
	page(name: "mainPage", title: "")
    page(name: "optionsPage", title: "")
    page(name: "aboutPage", title: "")
}

def mainPage() {
	dynamicPage(name: "mainPage", title: "Select your devices and settings", install: true, uninstall: true){

		section("Select the Evap Cooler fan motor switch hardware..."){
			input "fanMotor", "capability.switch", 
	    	multiple:false, title: "Fan Motor On-Off Control device", required: true, submitOnChange: true  
		}   	
  
        section("Select a room temperature sensor to control the Evap Cooler..."){
			input "tempSensor", "capability.temperatureMeasurement", multiple:false, title: "Temperature Sensor", required: true, submitOnChange: true  
		}
        
        section("Enter the desired room temperature setpoint..."){
        	input "setpoint", "decimal", title: "Room Setpoint Temp", required: true
    	} 
		
// IS this little section below buggy or is it ST again? It won't let you save the app when clicking Done???
		
		section("Current Conditions are"){	//The 'if' statements used below prevent null error crash
        if (fanMotor) {  
    		paragraph ("${fanMotor.displayName} is ${fanMotor.currentSwitch}")
            }
        if (tempSensor) {  
    		paragraph ("${tempSensor.displayName} room temp is ${tempSensor.currentTemperature}° ")
            }
        if (fanPump) {  
    		paragraph ("${fanPump.displayName} is ${fanPump.currentSwitch} ")
			} 
        if (fanSpeed) {  
    		paragraph ("${fanSpeed.displayName} is ${fanSpeed.currentSwitch} ")
			}    
    	}

	
		section("Optional Settings (Fan Speed, Timers, Motion, etc)") {
			href (name: "optionsPage", 
        	title: "Configure Optional settings", 
        	description: none,
        	image: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/settings250x250.png",
        	required: false,
        	page: "optionsPage"
        	)
        }
// VERSION
		section("Version Info, User's Guide") {
			href (name: "aboutPage", 
            title: "Evap Cooler Thermostat \n"+"Version: 1.0.160704b \n"+"Copyright © 2016 Dale Coffing", 
            description: "Tap to access user's guide.",
            image: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/ect250x250.png",
            required: false,
            page: "aboutPage"
			)
   		}	
    }
}

def optionsPage() {
	dynamicPage(name: "optionsPage", title: "Configure Optional Settings", install: false, uninstall: false) {
    
		section("Select the Evap Cooler Fan 2-Speed switch control hardware (optional, leave blank for single speed)...."){
			input "fanSpeed", "capability.switch", 
	    	multiple:false, title: "Fan Hi-Lo 2-Speed Control device", required: false, submitOnChange: true  
		}

		section("Enter the desired differential temp between fan speeds (default=1.0)..."){
			input "fanDiffTempString", "enum", title: "Fan Differential Temp", options: ["0.5","1.0","1.5","2.0"], required: false
		}
        
        section("Select the Evap Cooler water pump switch hardware..."){
			input "fanPump", "capability.switch", 
	    	multiple:false, title: "Water Pump On-Off Control device", required: false, submitOnChange: true  
		}
    	section("Enter the desired minutes to delay start of fan to allow for wetting of pads. (default=1.5)..."){
			input "fanDelayOnString", "enum", title: "Fan Delay On Timer", options: ["0.0","0.5","1.0","1.5","2.0","2.5"], required: false
		}
		section("Enable Evap Cooler thermostat only if motion is detected at (optional, leave blank to not require motion)..."){
			input "motionSensor", "capability.motionSensor", title: "Select Motion device", required: false, submitOnChange: true
		}
        
        if (motionSensor) {
			section("Turn off Evap Cooler when there's been no motion detected for..."){
				input "minutesNoMotion", "number", title: "Minutes?", required: true
			}
        }
		
        section("Select Evap Cooler operating method desired (default to 'YES-Auto'..."){
			input "autoMode", "enum", title: "Enable Evap Cooler Thermostat?", options: ["NO-Manual","YES-Auto"], required: false
		}
        
		section ("Change SmartApp name, Mode selector") {
			label title: "Assign a name", required: false
//          mode(title: "Set for specific mode(s)"), required: false  //this technique for User mode fails tempHandler still executes for this smartapp?
            input "selectedModes", "mode", title:  "Select specific mode(s) to run (default is ALL modes)", multiple: true, required: false
			}      	
	}
  }

def aboutPage() {
	dynamicPage(name: "aboutPage", title: none, install: false, uninstall: true) {
     	section("User's Guide for Evap Cooler Thermostat") {
        	paragraph textHelp()
 		}
	}
}

def installed() {
	log.debug "def INSTALLED with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "def UPDATED with settings: ${settings}"
	unsubscribe()
	initialize()
    handleTemperature(tempSensor.currentTemperature) //call handleTemperature to bypass temperatureHandler method 
} 

def initialize() {
	log.debug "def INITIALIZE with settings: ${settings}"
	subscribe(tempSensor, "temperature", temperatureHandler) //call temperatureHandler method when any reported change to "temperature" attribute
	subscribe(location, "mode", modeChangedHandler) //call modeChangedHandler with any reported change to "mode" change attribute 
    if (motionSensor) {
		subscribe(motionSensor, "motion", motionHandler) //call the motionHandler method when there is any reported change to the "motion" attribute
	}   
}

def shutdownEvap() {
	fanMotor.off()
	fanPump.off()
	fanSpeed.off()
}

def temperatureHandler(evt) {
	log.debug "temperatureHandler called: $evt"	
    handleTemperature(evt.doubleValue)
	log.debug "temperatureHandler evt.doubleValue : $evt"
}

def handleTemperature(temp) {		//
	log.debug "handleTemperature called: $evt"	
	def isActive = hasBeenRecentMotion()
	if (isActive) {
		//motion detected recently
		tempCheck(temp, setpoint)
		log.debug "handleTemperature ISACTIVE($isActive)"
	} else {
     	shutdownEvap()
 	}
}    

def motionHandler(evt) {
	if (evt.value == "active") {
		//motion detected
		def lastTemp = tempSensor.currentTemperature
		log.debug "motionHandler ACTIVE($isActive)"
		if (lastTemp != null) {
			tempCheck(lastTemp, setpoint)
		}
	} else if (evt.value == "inactive") {		//testing to see if evt.value is indeed equal to "inactive" (vs evt.value to "active")
		//motion stopped
		def isActive = hasBeenRecentMotion()	//define isActive local variable to returned true or false
		log.debug "motionHandler INACTIVE($isActive)"
		if (isActive) {
			def lastTemp = tempSensor.currentTemperature
			if (lastTemp != null) {				//lastTemp not equal to null (value never been set) 
				tempCheck(lastTemp, setpoint)
			}
		} else {
			shutdownEvap()
		}
	}
}

private tempCheck(currentTemp, desiredTemp) {

	log.debug "TEMPCHECK#1(CT=$currentTemp, SP=$desiredTemp, FM=$fanMotor.currentSwitch, automode=$autoMode, FDTstring=$fanDiffTempString, FDTvalue=$fanDiffTempValue)"
    
    //convert Fan Delay On input enum string to number value and if user doesn't select a Fan Delay On value, then default to 1.5 
    def fanDelayOnValue = (settings.fanDelayOnString != null && settings.fanDelayOnString != "") ? Double.parseDouble(settings.fanDelayOnString): 1.5
    
    //convert Fan Diff Temp input enum string to number value and if user doesn't select a Fan Diff Temp default to 1.0 
    def fanDiffTempValue = (settings.fanDiffTempString != null && settings.fanDiffTempString != "") ? Double.parseDouble(settings.fanDiffTempString): 1.0
    

    def autoModeValue = (settings.autoMode != null && settings.autoMode != "") ? settings.autoMode : "YES-Auto"	
    
    def LowDiff = fanDiffTempValue*1 
    def HighDiff = fanDiffTempValue*2

	log.debug "TEMPCHECK#2(CT=$currentTemp, SP=$desiredTemp, FM=$fanMotor.currentSwitch, automode=$autoMode, FDTstring=$fanDiffTempString, FDTvalue=$fanDiffTempValue)"
	

	if (currentModeAllowed(settings.selectedModes)) {
	// Executes if current ST mode matches one of the user selected modes.
     
		if (autoModeValue == "YES-Auto") {
			switch (currentTemp - desiredTemp) {
				case { it  >= HighDiff }:
					// turn on fan high speed
					fanSpeed.on()			// set fan Hi speed
					if (fanMotor.currentSwitch == "off") {		// if fan is OFF turn everything on 
						fanPump.on()							// set water pump on 
						fanMotor.on([delay: (fanDelayOnValue*60*1000)])			// delay starting fan to allow pump to wet pads 
						log.debug "HI speed(CT=$currentTemp, SP=$desiredTemp,  HighDiff=$HighDiff, fanDelayOnValue=$fanDelayOnValue)"
					} else { //fan and pump already running 
						}
					break  //exit switch statement 
				case { it >= LowDiff }:
					// turn on fan low speed
					if (fanMotor.currentSwitch == "off") {		// if fan is OFF turn everything on 
						fanSpeed.off()						// set fan Lo speed
						fanPump.on()							// set water pump on 
						fanMotor.on([delay: (fanDelayOnValue*60*1000)])			// delay starting fan to allow pump to wet pads 
                		log.debug "Fan Lo speed in fanDelayOn min (CT=$currentTemp, SP=$desiredTemp,  LowDiff=$LowDiff)"
					} else {
						fanSpeed.off()	//fan is already running, set Low speed immediately
					}
					log.debug "LO speed skip pump (CT=$currentTemp, SP=$desiredTemp,  LowDiff=$LowDiff)"
					break
				default:
					// check to see if fan should be turned off
					if (desiredTemp - currentTemp >= 0 ) {	//below or equal to setpoint, turn off fan, 
						shutdownEvap()
						log.debug "below SP+Diff=fan OFF (CT=$currentTemp, SP=$desiredTemp, FD=$fanMotor.currentSwitch, autoMode=$autoMode,)"
					} 
					log.debug "autoMode YES-MANUAL? else OFF(CT=$currentTemp, SP=$desiredTemp, FD=$fanMotor.currentSwitch, autoMode=$autoMode,)"
			}	
		}

	} else {			// if current ST mode does NOT match one of the user selected modes.
	shutdownEvap()
	}
}

def currentModeAllowed(allowedModes) {
    return (!allowedModes || allowedModes?.find{it == location.mode}) 
}

private hasBeenRecentMotion() {
	def isActive = false
	if (motionSensor && minutesNoMotion) {
		def deltaMinutes = minutesNoMotion as Long
		if (deltaMinutes) {
			def motionEvents = motionSensor.eventsSince(new Date(now() - (60000 * deltaMinutes)))
			log.trace "Found ${motionEvents?.size() ?: 0} events in the last $deltaMinutes minutes"
			if (motionEvents.find { it.value == "active" }) {
				isActive = true
			}
		}
	} else {
		isActive = true
	}
	isActive
}

private def textHelp() {
	def text =
    "This smartapp provides automatic control for Evaporative Coolers (single or two-speed) using"+
   " any temperature sensor. On a call for cooling the water pump is turned on and given two minutes"+
   " to wet pads before fan low speed is enabled. The fan high speed is turned on if the temperature"+ 
   " continues to rise above the adjustable differential. There is an optional motion override.\n\n"+
   
   "It requires these hardware devices; any temperature sensor, a switch for Fan On-Off, a switch"+
   " for pump. If two speed control is desired another switch will be necessary.\n\n"+
   " You might consider using a Remotec ZFM-80 15amp relay for fan motor on-off, if you desire both"+
   " pump and fan speed then Enerwave ZWN-RSM2 dual 10amp relays to control pump and the second relay"+
   " to control hi-lo speed via Omoron LY1F SPDT 15amp relay. For only pump control any single switch could"+
   " work like the Enerwave ZWN-RSM1S or Monoprice #11989 Z-Wave In-Wall On/Off module. \n\n"+
   
   " To uninstall the smartapp simply tap REMOVE below"  
   
}
