# klog
Bog simple logging infrastructure

I wanted to log errors to the console, and optionally, given a command line switch, log debugging information to an output file.
I explicitly did NOT want to craft an XML file, or deal with anything *not* programmatic.
Try that with log4j. It is doable. It is even easy if you are reasonably-conversant with log4j.
I am not reasonably conversant with log4j, nor do I want to be.
Nor do I want to have to explain to my customers how to change entries in an XML file to turn on logging.

So after spending hours in amazement at how bloated and over-engineered log4j is, I spent a quick couple of hours crafting this logging framework from scratch.
Use it if you like.
