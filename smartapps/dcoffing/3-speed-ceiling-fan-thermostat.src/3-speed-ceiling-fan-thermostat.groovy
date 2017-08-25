/*
   Virtual Thermostat for 3 Speed Ceiling Fan Control
   Copyright 2016 SmartThings, Dale Coffing
   
   This smartapp provides automatic control of Low, Medium, High speeds of a ceiling fan using 
   any temperature sensor with optional motion override. 
   It requires two hardware devices; any temperature sensor and a dimmer type smart fan controller
   such as the GE 12730 or Leviton VRF01-1LX. Incorporates contributions from:
   
   Eric Vitale (https://github.com/ericvitale/SmartThingsPublic/blob/master/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3-speed-ceiling-fan-thermostat.groovy)
   Victor Welasco (https://github.com/Welasco/SmartThingsPublic/blob/VictorWelasco/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3-speed-ceiling-fan-thermostat.groovy)

  Change Log
  2017-08-24 Add a Debug option and a lot of debug msgs. Debug must be activated at the App Leve to see the msgs on ST IDE. by Victor Welasco
  2017-08-09 Fixed Motion Sensor using SM sample: http://docs.smartthings.com/en/latest/getting-started/first-smartapp.html. by Victor Welasco
             Fixed SunSet SunRise bug, now we are using timeOfDayIsBetween. by Victor Welasco
  2017-06-29 Fixed SmartApp Mode, now if you select a specific Mode the App will only run if the mode is on. by Victor Wealasco
  2017-06-27 Checking if the Switch was not physically turned On, if so stop all checks until it's physically turned OFF.
                Very usefull when you would like to turn the switch on at any time and don't want the switch be truning off on every temperature event change. by Victor Welasco
                INFO: The isPhysical() have a bug and can not be used. (https://community.smartthings.com/t/device-physical-vs-digital-digital-physical-triggers/6229/11)
                Workaround: I create a global variable that will control if the app turned the switch on or off.
             Fix a bug on sunrise and sunset that was been evaluated all the time, now if is off the check is off we will stop evaluating it. by Victor Welasco
  2017-06-07 Added an option to only turn the fan on during the day (Sun is UP - Between SunRise and SunSet).  by Victor Welasco
             Added the option to disable the speed control (If nothing is selected speed control will not be evaluated).  by Victor Welasco
             Will only send a device command if the device is not already on that state.  by Victor Welasco
  2017-06-03 Added an option to check presence using a presence sensor. - by Victor Welasco
  2017-04-11 Added 10.0 selection for Fan Differential Temp to mimic single speed control
  2016-10-19 Ver2 Parent / Child app to allow for multiple use cases with a single install - @ericvitale
  2016-06-30 added dynamic temperature display on temperature setpoint input text
  2016-06-28 x.1 version update
  			added submitOnChange for motion so to skip minutes input next if no motion selected
 			changed order of inputs for better logic flow
            added separate input page for Configuring Settings to reduce clutter on required inputs
            change to other mode techinque to see if it will force a reevaluate of methods
            renamed fanHiSpeed to fanSpeed for more generic use, added 0.0 on timer selection
            changed motion detector minutes input only if motion selected submitOnChange
  2016-06-03 modified the 3 second startup to 1 for low speed
  2016-5-30 added dynamicPages for user guide, combined version data with aboutPage parameters which
  			gives a larger icon image then if used alone in paragraph mode.
  2016-5-19 code clean up only
  2016-5-17 fanDiffTemp input changed to use enum with preselected values to overcome range:"0.1..2.0" bug
  2016-5-16 fixed typo with motion to motionSensor in hasBeenRecentMotion()
            fixed IDE integration with ST by making another change to file name specifics.
  2016-5-15 fixed fan differenial decimal point error by removing range: "1..99", removed all fanDimmer.setLevel(0)
 	         added iconX3Url, reworded preferences, rename evaluate to tempCheck for clarity,
 	         best practices to utilize initialize() method & replace motion with motionSensor,
  2016-5-14 Fan temperature differential variable added, best practices to change sensor to tempSensor,
  2016-5-13 best practices to replace ELSE IF for SWITCH statements on fan speeds, removed emergency temp control
  2016-5-12 added new icons for 3SFC, colored text in 3SFC125x125.png and 3sfc250x250.png
  2016-5-6  (e)minor changes to text, labels, for clarity, (^^^e)default to NO-Manual for thermostat mode 
  2016-5-5c clean code, added current ver section header, allow for multiple fan controllers,
            replace icons to ceiling fan, modify name from Control to Thermostat
  2016-5-5b @krlaframboise change to bypasses the temperatureHandler method and calls the tempCheck method
            with the current temperature and setpoint setting
  2016-5-5  autoMode added for manual override of auto control/*
  2016-5-4b cleaned debug logs, removed heat-cool selection, removed multiple stages
  2016-5-3  fixed error on not shutting down, huge shout out to my bro Stephen Coffing in the logic formation 
  
  I modified the SmartThngs original Virtual Thermostat code which is buggy. Known issues
  -[Fixed] when SP is updated, temp control isn't evaluated immediately, an event must trigger like change in temp, motion
  - if load is previously running when smartapp is loaded, it isn't evaluated immediately to turn off when SetPt>CurrTemp
  - temperature control is not evaluated when making a mode change, have to wait for something to change like temp
 
  Thanks to @krlaframboise, @MikeMaxwell for help in solving issues for a first time coder. @MichaelS for icon background
 
   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
   in compliance with the License. You may obtain a copy of the License at: www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
   on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
   for the specific language governing permissions and limitations under the License.
  
 */
definition(
    name: "3 Speed Ceiling Fan Thermostat",
    namespace: "dcoffing",
    author: "Dale Coffing",
    description: "Automatic control for 3 Speed Ceiling Fan using Low, Medium, High speeds with any temperature sensor.",
    category: "My Apps",
    singleInstance: true,
	iconUrl: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3scft125x125.png", 
   	iconX2Url: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3scft250x250.png",
	iconX3Url: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3scft250x250.png",
)

preferences {
    page(name: "startPage")
    page(name: "parentPage")
    page(name: "childStartPage")
    page(name: "optionsPage")
    page(name: "aboutPage")
}

def startPage() {
    if (parent) {
        childStartPage()
    } else {
        parentPage()
    }
}

def parentPage() {
	return dynamicPage(name: "parentPage", title: "", nextPage: "", install: false, uninstall: true) {
        section("Create a new fan automation.") {
            app(name: "childApps", appName: appName(), namespace: "dcoffing", title: "New Fan Automation", multiple: true)
        }
    }
}

def childStartPage() {
	dynamicPage(name: "childStartPage", title: "Select your devices and settings", install: true, uninstall: true) {
    
        section("Select a room temperature sensor to control the fan..."){
			input "tempSensor", "capability.temperatureMeasurement", multiple:false, title: "Temperature Sensor", required: true, submitOnChange: true  
		}
        if (tempSensor) {  //protects from a null error
    		section("Enter the desired room temperature setpoint...\n" + "NOTE: ${tempSensor.displayName} room temp is ${tempSensor.currentTemperature}° currently"){
        		input "setpoint", "decimal", title: "Room Setpoint Temp", defaultValue: tempSensor.currentTemperature, required: true
    		}
        }
        else 
        	section("Enter the desired room temperature setpoint..."){
        		input "setpoint", "decimal", title: "Room Setpoint Temp", required: true
    		}       
        section("Select the ceiling fan control hardware..."){
			input "fanDimmer", "capability.switchLevel", 
	    	multiple:false, title: "Fan Control device", required: true
		}
        section("Optional Settings (Diff Temp, Timers, Motion, etc)") {
			href (name: "optionsPage", 
        	title: "Configure Optional settings", 
        	description: none,
        	image: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/evap-cooler-thermostat.src/settings250x250.png",
        	required: false,
        	page: "optionsPage"
        	)
        }

        section("Name") {
        	label(title: "Assign a name", required: false)
        }

        section("Version Info, User's Guide") {
// VERSION
			href (name: "aboutPage", 
			title: "3 Speed Ceiling Fan Thermostat \n"+"Version:3.082417 \n"+"Copyright © 2016 Dale Coffing", 
			description: "Tap to get user's guide.",
			image: "https://raw.githubusercontent.com/dcoffing/SmartThingsPublic/master/smartapps/dcoffing/3-speed-ceiling-fan-thermostat.src/3scft125x125.png",
			required: false,
			page: "aboutPage"
			)
		}
	}
}      

def optionsPage() {
	dynamicPage(name: "optionsPage", title: "Configure Optional Settings", install: false, uninstall: false) {
       	section("Enter the desired differential temp between fan speeds"){
			input "fanDiffTempString", "enum", title: "Fan Differential Temp", options: ["0.5","1.0","1.5","2.0","10.0"], required: false
		}
		section("Enable ceiling fan thermostat only if motion is detected at (optional, leave blank to not require motion)..."){
			input "motionSensor", "capability.motionSensor", title: "Select Motion device", required: false, submitOnChange: true
		}
        if (motionSensor) {
			section("Turn off ceiling fan thermostat when there's been no motion detected for..."){
				input "minutesNoMotion", "number", title: "Minutes?", required: true
			}
		}
		section("Enable ceiling fan thermostat only if someone is present..."){
			input "presenceSensor", "capability.presenceSensor", title: "Select Presence device", required: false, multiple:true
		}        
		section("Enable ceiling fan thermostat only if during the day (Sun is UP)..."){
			input "sunsetsunrise", "bool", title: "Select True or False:", defaultValue: false, required: false
		}            
        section("Select ceiling fan operating mode desired (default to 'YES-Auto'..."){
			input "autoMode", "enum", title: "Enable Ceiling Fan Thermostat?", options: ["NO-Manual","YES-Auto"], required: false
		}
    	section ("Change SmartApp name, Mode selector") {
		    //mode title: "Set for specific mode(s)", required: false
            input "modes", "mode", title: "select a mode(s)", required: false, multiple: true
		}
		section("Enable Debug Log at SmartThing IDE"){
			input "idelog", "bool", title: "Select True or False:", defaultValue: false, required: false
		}          
    }
}

def aboutPage() {
	dynamicPage(name: "aboutPage", title: none, install: true, uninstall: true) {
     	section("User's Guide; 3 Speed Ceiling Fan Thermostat") {
        	paragraph textHelp()
 		}
	}
}

private def appName() { return "${parent ? "3 Speed Fan Automation" : "3 Speed Ceiling Fan Thermostat"}" }

def installed() {
	writeLog("def INSTALLED with settings: ${settings}")
	initialize()
}

def updated() {
	writeLog("def UPDATED with settings: ${settings}")
	unsubscribe()
	initialize()
    handleTemperature(tempSensor.currentTemperature) //call handleTemperature to bypass temperatureHandler method
} 

def initialize() {
    writeLog("def INITIALIZE started")
    if(parent) { 
        writeLog("def INITIALIZE - initChild")
    	initChild() 
    } else {
        writeLog("def INITIALIZE - initParent")
    	initParent() 
    }  
}

def initChild() {
	writeLog("def INICHILD with settings: ${settings}")
    //state.switchTurnedOnbyApp = false
    subscribe(tempSensor, "temperature", temperatureHandler) //call temperatureHandler method when any reported change to "temperature" attribute
	if (motionSensor) {
        writeLog("def INICHILD - Subscribing motionSensor")
		subscribe(motionSensor, "motion.active", motionDetectedHandler) //call the motionDetectedHandler method when there is any reported change to the "motion active" attribute
        subscribe(motionSensor, "motion.inactive", motionStoppedHandler) //call the motionStoppedHandler method when there is any reported change to the "motion inactive" attribute
	}
 	if (presenceSensor) {
        writeLog("def INICHILD - Subscribing presenceSensor")
		subscribe(presenceSensor, "presence", presenceHandler) //call the presenceHandler method when there is any reported change to the "presence" attribute
	}
 	if (sunsetsunrise) {
        writeLog("def INICHILD - Subscribing sunsetsunrise")
        subscribe(location, "sunset", sunsetsunriseHandler) //call the sunsetsunriseHandler method when the sunset
        subscribe(location, "sunrise", sunsetsunriseHandler) //call the sunsetsunriseHandler method when the sunrise        
	}    
    if (modes){
        writeLog("def INICHILD - Subscribing modes")
        subscribe(location, "mode", modeChangeHandler)
    }
}
        
def initParent() {
	writeLog("def INITPARENT Parent Initialized")
}        
                                   //Event Handler Methods                     
def temperatureHandler(evt) {
	writeLog("def TEMPERATUREHANDLER - temperatureHandler called: $evt")
    handleTemperature(evt.doubleValue)
	writeLog("def TEMPERATUREHANDLER - temperatureHandler evt.doubleValue : $evt")
}

def handleTemperature(temp) {		//
	writeLog("def HANDLETEMPERATURE - handleTemperature called: $evt")
    def isSunsetSunrise = betweenSunsetSunRise()
    def isPresent = someonePresent()
	def isMotionActive = hasBeenRecentMotion()
	def isSmartAppTurnedSwitchOn = smartAppTurnedSwitchOn()
    def isCheckMode = checkMode()
    writeLog("def HANDLETEMPERATURE - isSunsetSunrise:${isSunsetSunrise}, isPresent:${isPresent}, isMotionActive:${isMotionActive}, isSmartAppTurnedSwitchOn:${isSmartAppTurnedSwitchOn}, isCheckMode:${isCheckMode}")
    if(fanDimmer.currentSwitch == "off" || isSmartAppTurnedSwitchOn){
        writeLog("def HANDLETEMPERATURE - fanDimmer is currently off or was turned by SmartApp")
        if(isSunsetSunrise && isPresent && isCheckMode){
            writeLog("def HANDLETEMPERATURE - isSunsetSunrise, isPresent and isCheckMode is all TRUE")
            if (isMotionActive) {
                //motion detected recently
                writeLog("def HANDLETEMPERATURE - isMotionActive is TRUE")
                writeLog("def HANDLETEMPERATURE - calling TEMPCHECK using temp:${temp} and setpoint:${setpoint}")
                tempCheck(temp, setpoint)
                writeLog("def HANDLETEMPERATURE - handleTemperature ISACTIVE($isMotionActive)")
            }
            else {
                writeLog("def HANDLETEMPERATURE - isMotionActive is FALSE")
                if (fanDimmer.currentSwitch != "off") {
                    writeLog("def HANDLETEMPERATURE - 1 - fanDimmer is currently on")
                    writeLog("def HANDLETEMPERATURE - 1 - calling switchOff")
                    switchOff()
                }
            }
        }
        else {
            writeLog("def HANDLETEMPERATURE - one of this sets is currently off isSunsetSunrise, isPresent and isCheckMode")
            if (fanDimmer.currentSwitch != "off") {
                writeLog("def HANDLETEMPERATURE - 2 - fanDimmer is currently on")
                writeLog("def HANDLETEMPERATURE - 2 - calling switchOff")
                switchOff()
            }
        }
    }
    else{
        writeLog("def HANDLETEMPERATURE - The Fan Switch was manually turned On, skipping all checks until it's manually turned off!")
    }
}

def motionDetectedHandler(evt) {
    writeLog("def MOTIONDETECEDHANDLER - motionDetectedHandler called: $evt")
    //log.debug "motionDetectedHandler called: $evt"
    //theswitch.on()

    //motion detected
    def lastTemp = tempSensor.currentTemperature
    
    if (lastTemp != null) {
        writeLog("def MOTIONDETECEDHANDLER - calling HANDLETEMPERATURE")
        handleTemperature(lastTemp)
    }
}

def motionStoppedHandler(evt) {
    writeLog("def MOTIONSTOPPEDHANDLER - called: $evt")
    def lastTemp = tempSensor.currentTemperature

    writeLog("def MOTIONSTOPPEDHANDLER - creating a scheduled motionscheduledStopped call")
    runIn(60 * minutesNoMotion, motionscheduledStopped)
}

def presenceHandler(evt) {
    writeLog("def PRESENCEHANDLER - called: $evt")
	def lastTemp = tempSensor.currentTemperature
    writeLog("def PRESENCEHANDLER - calling HANDLETEMPERATURE")
    handleTemperature(lastTemp)
}

def sunsetsunriseHandler(evt) {
    writeLog("def SUNSETSUNRISEHANDLER - called: $evt")
	def lastTemp = tempSensor.currentTemperature
    writeLog("def SUNSETSUNRISEHANDLER - calling HANDLETEMPERATURE")
    handleTemperature(lastTemp)
}

def modeChangeHandler(evt){
    writeLog("def MODECHANGEHANDLER - called: $evt")
    def lastTemp = tempSensor.currentTemperature
    writeLog("def MODECHANGEHANDLER - calling HANDLETEMPERATURE")
    handleTemperature(lastTemp)
}

private tempCheck(currentTemp, desiredTemp)
{
    writeLog("def TEMPCHECK - called currentTemp:${currentTemp} desiredTemp:${desiredTemp}")
	writeLog("def TEMPCHECK - TEMPCHECK#1(CT=$currentTemp,SP=$desiredTemp,FD=$fanDimmer.currentSwitch,FD_LVL=$fanDimmer.currentLevel, automode=$autoMode,FDTstring=$fanDiffTempString, FDTvalue=$fanDiffTempValue)")
    //convert Fan Diff Temp input enum string to number value and if user doesn't select a Fan Diff Temp default to 1.0 
    //def fanDiffTempValue = (settings.fanDiffTempString != null && settings.fanDiffTempString != "") ? Double.parseDouble(settings.fanDiffTempString): 1.0
    def fanDiffTempValueSet = settings.fanDiffTempString
	
    //if user doesn't select autoMode then default to "YES-Auto"
    def autoModeValue = (settings.autoMode != null && settings.autoMode != "") ? settings.autoMode : "YES-Auto"	
	writeLog("def TEMPCHECK - fanDiffTempValueSet:${fanDiffTempValueSet} autoModeValue:${autoModeValue}")
	writeLog("def TEMPCHECK - TEMPCHECK#1(CT=$currentTemp,SP=$desiredTemp,FD=$fanDimmer.currentSwitch,FD_LVL=$fanDimmer.currentLevel, automode=$autoMode,FDTstring=$fanDiffTempString, FDTvalue=$fanDiffTempValue)")
	if (autoModeValue == "YES-Auto") {
        if(fanDiffTempValueSet){
            def fanDiffTempValue = (settings.fanDiffTempString != null && settings.fanDiffTempString != "") ? Double.parseDouble(settings.fanDiffTempString): 1.0
            def LowDiff = fanDiffTempValue*1 
            def MedDiff = fanDiffTempValue*2
            def HighDiff = fanDiffTempValue*3    
            writeLog("def TEMPCHECK - fanDiffTempValue:${fanDiffTempValue}, LowDiff:${LowDiff}, MedDiff:${MedDiff}, HighDiff:${HighDiff}")        
            switch (currentTemp - desiredTemp) {
                case { it  >= HighDiff }:
                    // turn on fan high speed
                    writeLog("def TEMPCHECK - HighDiff detected calling switchOnLevel 90")
                    if(fanDimmer.currentLevel != 90){
                        switchOnLevel(90)
                    }
                    writeLog("def TEMPCHECK - HI speed(CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, HighDiff=$HighDiff)")
                break  //exit switch statement 
                case { it >= MedDiff }:
                        // turn on fan medium speed
                        writeLog("def TEMPCHECK - MedDiff detected calling switchOnLevel 60")
                        if(fanDimmer.currentLevel != 60){
                            switchOnLevel(60)
                        }
                        writeLog("MED speed(CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, MedDiff=$MedDiff)")
                        break
                case { it >= LowDiff }:
                    // turn on fan low speed
                    writeLog("def TEMPCHECK - LowDiff detected calling switchOnLevel 30")
                    if (fanDimmer.currentSwitch == "off") {		// if fan is OFF to make it easier on motor by   
                        writeLog("def TEMPCHECK - LowDiff detected fanDimmer was off")
                        writeLog("def TEMPCHECK - LowDiff detected calling switchOnLevel 90 to start the fan at maximum speed")
                        switchOnLevel(90)					// starting fan in High speed temporarily then 
                        writeLog("def TEMPCHECK - LowDiff detected calling switchOnLevel 30 in 5 seconds")
                        switchOnLevel(30, [delay: 5000])	// change to Low speed after 5 second
                        writeLog("LO speed after HI 3secs(CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, LowDiff=$LowDiff)")
                    } else {
                        writeLog("def TEMPCHECK - LowDiff detected fanDimmer was ON calling switchOnLevel 30")
                        if(fanDimmer.currentLevel != 30){
                            switchOnLevel(30)	//fan is already running, not necessary to protect motor
                        }                           //set Low speed immediately
                    }							    
                    writeLog("def TEMPCHECK - LO speed immediately(CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, LowDiff=$LowDiff)")
                    break
            default:
                    // check to see if fan should be turned off
                    if (desiredTemp - currentTemp >= 0 ) {	//below or equal to setpoint, turn off fan, zero level
                        if (fanDimmer.currentSwitch != "off") {
                            switchOff()
                        }
                        writeLog("def TEMPCHECK - below SP+Diff=fan OFF (CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, FD=$fanDimmer.currentSwitch,autoMode=$autoMode,)")
                    } 
                    writeLog("def TEMPCHECK - autoMode YES-MANUAL? else OFF(CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, FD=$fanDimmer.currentSwitch,autoMode=$autoMode,)")
            }	
        }
        else{
            // In case the differential temp is off we will just turn it on or off not checking the fan speed
            // defining difftemp - if it's a positive value turn the fan on or turn the fan off
            writeLog("def TEMPCHECK - differential temp is off checking if we must turn it on! (CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel)")
            def diffTemp = currentTemp - desiredTemp
            if(diffTemp >= 0){
                writeLog("def TEMPCHECK - Turrning the Fan On if it's not already on (CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel)")
                if (fanDimmer.currentSwitch != "on") {
                    writeLog("def TEMPCHECK - Fan wasn't running, turnning it On to 99")
                    switchOnLevel(99)
                }
            }
            else{
                writeLog("def TEMPCHECK - below SP+Diff=fan OFF if it's not already off (CT=$currentTemp, SP=$desiredTemp, FD-LVL=$fanDimmer.currentLevel, FD=$fanDimmer.currentSwitch,autoMode=$autoMode,)")
                if (fanDimmer.currentSwitch != "off") {
                    writeLog("def TEMPCHECK - Fan was running, turnning it Off")
                    switchOff()
                }                
            }
        }
	}
    else{
        writeLog("def TEMPCHECK - autoModeValue off doing nothing!")
    }
}

def motionscheduledStopped()
{
    writeLog("def MOTIONSCHEDULEDSTOPPED - called")
	def lastTemp = tempSensor.currentTemperature
    writeLog("def MOTIONSCHEDULEDSTOPPED - calling handleTemperature")
    handleTemperature(lastTemp)
}

private hasBeenRecentMotion()
{
	writeLog("def HASBEENRECENTMOTION - called")
    def isActive = false
	if (motionSensor && minutesNoMotion) {
        writeLog("def HASBEENRECENTMOTION - motionSensor and minutesNoMotion found")
        def motionState = motionSensor.currentState("motion")
		// def deltaMinutes = minutes as Long
		// if (deltaMinutes) {
		// 	def motionEvents = motionSensor.eventsSince(new Date(now() - (60000 * deltaMinutes)))
		// 	log.trace "Found ${motionEvents?.size() ?: 0} events in the last $deltaMinutes minutes"
		// 	if (motionEvents.find { it.value == "active" }) {
		// 		isActive = true
		// 	}
		// }
        writeLog("def HASBEENRECENTMOTION - motionState value:${motionState.value}")
        if (motionState.value == "inactive") {
            // get the time elapsed between now and when the motion reported inactive
            def elapsed = now() - motionState.date.time

            // elapsed time is in milliseconds, so the threshold must be converted to milliseconds too
            def threshold = 1000 * 60 * minutesNoMotion
            writeLog("def HASBEENRECENTMOTION - elapsed:${elapsed} threshold:${threshold}")
            if (elapsed >= threshold) {
                writeLog("def HASBEENRECENTMOTION - Motion has stayed inactive long enough since last check ($elapsed ms):  turning fan off")
                isActive = false
            } else {
                writeLog("def HASBEENRECENTMOTION - Motion has not stayed inactive long enough since last check ($elapsed ms):  doing nothing")
                isActive = true
            }
        } else {
            // Motion active; just log it and do nothing
            writeLog("def HASBEENRECENTMOTION - Motion is active, do nothing and wait for inactive")
            isActive = true
        }
	}
	else {
		isActive = true
	}
    writeLog("def HASBEENRECENTMOTION - returning isActive:${isActive}")
	isActive
}

private someonePresent()
{
    writeLog("def SOMEONEPRESENT - called")
	def isPresent = false
	if (presenceSensor) {
        def currPresenceDevice = presenceSensor.currentPresence
        def presentPresenceDevices = currPresenceDevice.findAll {
                deviceVal -> deviceVal == "present" ? true : false
            }
        writeLog("def SOMEONEPRESENT - Amount of devices that are currently present: ${presentPresenceDevices.size()} of ${presenceSensor.size()}")
        if(presentPresenceDevices.size() >= 1){
            isPresent = true
        }
	}
	else {
		isPresent = true
	}
    writeLog("def SOMEONEPRESENT - returning isPresent:${isPresent}")
	isPresent
}

private betweenSunsetSunRise()
{
    writeLog("def BETWEENSUNSETSUNRISE - called")
    def isGoodTime = false

    if(sunsetsunrise){
        def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: sunriseOffset, sunsetOffset: sunsetOffset)
        def now = new Date()
        def sunriseTime = s.sunrise
        def sunsetTime = s.sunset

        def between = timeOfDayIsBetween(sunsetTime,sunriseTime,now,location.timeZone)
        writeLog("def BETWEENSUNSETSUNRISE - betweenSunsetSunRise: SunRiseTime: ${sunriseTime} SunSetTime: ${sunsetTime} now: ${now} between: ${between}")

        //if(sunsetTime.after(now) || sunriseTime.before(now)) {   //before midnight/after sunset or after midnight/before sunset (checking if the Sun is UP)
        if(!between) {   //before midnight/after sunset or after midnight/before sunset (checking if the Sun is UP)
            writeLog("def BETWEENSUNSETSUNRISE - Sun is UP")
            isGoodTime = true
        }
    }
    else{
        writeLog("def BETWEENSUNSETSUNRISE - betweenSunsetSunRise: SunsetSunrise not set. Returning true.")
        isGoodTime = true
    }
    writeLog("def BETWEENSUNSETSUNRISE - returning isGoodTime:${isGoodTime}")
    isGoodTime
}

private smartAppTurnedSwitchOn()
{
    def isSwitchTurnedOnbyApp = state.switchTurnedOnbyApp
    writeLog("def SMARTAPPTURNEDSWITCHON - isSwitchTurnedOnbyApp is ${state.switchTurnedOnbyApp}")
    isSwitchTurnedOnbyApp
}

private switchOn()
{
    writeLog("def SWITCHON - called turning the switch ON without level")
    state.switchTurnedOnbyApp = true
    writeLog("def SWITCHON: state.switchTurnedOnbyApp is ${state.switchTurnedOnbyApp}")
    fanDimmer.on()
}

private switchOnLevel(level)
{
    writeLog("def SWITCHONLEVEL - called turning the switch ON using level:${level}")
    state.switchTurnedOnbyApp = true
    writeLog("def SWITCHONLEVEL - switchOnLevel: state.switchTurnedOnbyApp is ${state.switchTurnedOnbyApp}")
    fanDimmer.setLevel(level)
}

private switchOff()
{
    writeLog("def SWITCHOFF - called turning the switch OFF")
    state.switchTurnedOnbyApp = false
    writeLog("def SWITCHOFF - switchOff: state.switchTurnedOnbyApp is ${state.switchTurnedOnbyApp}")
    fanDimmer.off()
}

private checkMode()
{
    writeLog("def CHECKMODE - called")
    def isModeON = false

    if(modes){
        def currentModestatus = location.currentMode
        def selectedModes = modes.findAll {
                selMode -> selMode.toString() == currentModestatus.toString() ? true : false
            }
        if(selectedModes.size() >= 1){
            isModeON = true
        }
    }
    else{
        isModeON = true
    }
    writeLog("def CHECKMODE - returning isModeON:${isModeON}")
    isModeON
}

private writeLog(message)
{
    if(idelog){
        log.debug "${message}"
    }
}

private def textHelp() {
	def text =
		"This smartapp provides automatic control of Low, Medium, High speeds of a"+
		" ceiling fan using any temperature sensor based on its' temperature setpoint"+
        " turning on each speed automatically in 1 degree differential increments."+
        " For example, if the desired room temperature setpoint is 72, the low speed"+
        " turns on first at 73, the medium speed turns on at 74, the high speed turns"+
        " on at 75. And vice versa on decreasing temperature until at 72 the ceiling"+
        " fan turns off. The differential is adjustable from 0.5 to 2.0 in half degree increments. \n\n" +
        "A notable feature is when low speed is initially requested from"+
        " the off condition, high speed is turned on briefly to overcome the startup load"+
        " then low speed is engaged. This mimics the pull chain switches that most"+
        " manufacturers use by always starting in high speed. \n\n"+
      	"A motion option turns off automatic mode when no motion is detected. A thermostat"+
        " mode option will disable the smartapp and pass control to manual control.\n\n"+
        "@ChadCK's 'Z-Wave Smart Fan Control Custom Device Handler' along with hardware"+
        " designed specifically for motor control such as the GE 12730 Z-Wave Smart Fan Control or"+
        " Leviton VRF01-1LX works well together with this smartapp."
}