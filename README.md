# GuitarAutoscroll
Android app for autoscrolling pictures, especially tabs for guitar players.
https://play.google.com/store/apps/details?id=com.kevinlinxc.guitarautoscroll

# Quick start
To start, you must have an Android-based phone as this is an Android app.
Install this app by searching for it in the Google Play Store (I recommend you search "Kevinlinxc Autoscroll", as there are many apps like this out there)
Open up a guitar tabs app on your phone and take a scrolling screenshot (available on most modern phones) of the chords tab. 
Open Autoscroll Sidekick, import the screenshot, select your speed, and hit play!

# About
The main use case of this app is to scroll long screenshots of Guitar Tabs. Traditionally, this feature is locked behind a paywall on mobile devices, and I found this frustrating enough to make my own app.

More recently, I've been trying to use this app to also play piano sheet music. The idea is that tablet devices and sheet music are too big to carry around casually, but everyone has a phone on them at all times. I made a script to slice sheet music into a single, long picture and have been using that picture in this app to read sheet music off my phone. This has some drawbacks, as measures with unequal width and changing tempos can not be accounted for, so work has to be done. One idea I have is to encode the tempo into the topmost pixel of a picture, such that the intended BPM is converted into the V of the HSV value. This would be cumbersome to implement on the Java and the sheet music conversion side so there is understandable inertia in implementing this.
