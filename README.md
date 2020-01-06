# pa-iscde-73324 
## what is this???
This is a Java Conventions Checker plug-in for PIDESCO. It's partially based on the class about AST we've had near the beginning of the semester. This is an academic project for Advanced Programming @ ISCTE-IUL. If you aren't involved with it, please take that into consideration. 

Extra info: https://github.com/pvmpa-iscteiulpt/pa-iscde-73324/wiki

## how does it work???
It analyses Abstract Syntax Tree (AST) nodes with Convention engines. A Convention engine is a class that looks at the contents of an AST Node (preferably of a specific type) and reports a non-null list of engine-specific Convention Violations if they exist. Convention Violations point at the problematic files's absolute path, the problematic lines, the problematic node's name in the code, and what the exact problem is (defined by the engine that found the problem).

## how can I extend it???
Use the "xtraconventions" (no there isn't a typo, it really is "xtraconventions") extension point and have a class or two or three or many that implement pt.iscte.pidesco.conventions.problems.conventions.ViolationType. If you have multiple Convention engines, don't forget to point at all of them in the Extensions tab of your plugin.xml.

An example of a working extension can be found in pt.iscte.pidesco.conventions.extTest.

## how can I use its services???
The same way you can use the default plug-ins' services (ProjectBrowser/JavaEditor), I guess? Just make sure you've clicked on the Java Conventions Checker tab on PIDESCO at least once before trying anything, so that the view can be activated.

## anything else I should know???
Actually yes. pt.iscte.pidesco.conventions.problems.CommonCodeChecks provides a few public static functions that can help you analyse code elements; for example, there's a function in there that can check whether a name starting with lowercase is a problem or not for you. Please use those before making up your own rules, if you can - it might save you a lot of trouble.

You need Eclipse for IDE Committers to use this, but you already knew that, right?

I originally planned for this to support Code Smell analysis as well, but that didn't work out. Sorry about that.

## I have found a bug/design flaw/stupid thing you should fix!!!
Please contact me ASAP or open an issue if you've found something weird or whatever.

## Can I contribute directly to this project with code from my own person??
<i><b>NO!!!</b></i> This is a SOLO academic project!!! Do not screw with my code on my remote (on any branch!?) or I'll fail!! Pull requests will ALWAYS be ignored, forever

Issues, suggestions and extra plug-ins are OK, but everything else is BAD

## Credits
-guava

-osgi

-eclipse core

-eclipse swt

-pidesco

### rubber_duck.jpg
