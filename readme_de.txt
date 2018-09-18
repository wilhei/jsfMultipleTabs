Dieses Projekt zeigt einen Bug, der bei multiplen Tabs entsteht.

Projekt starten:
- Konsole in der Root-Anwendung öffnen (da, wo die pom.xml liegt)
- mvn install
- mvn tomcat7:run-war-only

Hinweis:
Der Fehler tritt nur mit @org.omnifaces.cdi.ViewScoped auf!
Ich brauche aber diesen Scope, da javax.faces.view.ViewScoped mit den Tabs nicht richtig funktioniert.

Szenario 1:
Vorgehen korrektes Verhalten:
- Anwendung starten: Home (TestPage.xhtml) wird geöffnet.
- Link "ShowValue" in neuem Tab öffnen (Strg + Click auf Link)
- Im neuen Tab Link "Home" anklicken
-> Keine Exception in der Konsole

Vorgehen inkorrektes Verhalten:
- Anwendung starten: Home (TestPage.xhtml) wird geöffnet.
- Link "ShowValue" 15 mal in neuem Tab öffnen (Strg + Click auf Link)
- Im zuerst geöffneten neuen Tab den Link "Home" anklicken
-> Es fliegt die Exception:
	...
	Caused by: java.lang.IllegalArgumentException: someValue must be set.
	at com.pass.project.jsftest.Bean.init(Bean.java:26)
	... 64 more
	
Fehlverhalten:
Sind zu viele Tabs offen, wird beim Klick auf den Link "Home" komischerweise die PostConstruct Methode aufgerufen, anstatt einfach nach Home zu navigieren.
Dies passiert nicht, wenn nur ein weiterer Tab geöffnet wurde.

Schaut man sich den Network-Traffic an, wird beim Klick auf "Home" nur ein GET ".../faces/TestPage.xhtml" gesendet. (siehe /documents/NetworkTraffic_on_click_home.png)

Das Ändern der Werte numberOfViewsInSession und numberOfLogicalViews in der web.xml kann die Anzahl der Tabs bis zum Fehlereintritt verändern:

		<!-- Browser back  -->
		<context-param>
			 <param-name>com.sun.faces.numberOfViewsInSession</param-name>
			 <param-value>5</param-value>
		</context-param>
		
		<!-- Parallel tabs -->
		<context-param>
			 <param-name>com.sun.faces.numberOfLogicalViews</param-name>
			 <param-value>9</param-value>
		</context-param>
		
		
		
Szenario 2:
- Anwendung starten: Home (TestPage.xhtml) wird geöffnet.
- Folgende aktionen 5 mal wiederholen
	- Link "ShowValue" anklicken
	- Link "Home" anklicken
- 5 mal auf BrowserBack klicken
- Es erscheint innerhalb der 5 BrowserBack-Klicks folgenden Exception, OBWOHL man von Value auf Home navigiert.
	Caused by: java.lang.IllegalArgumentException: someValue must be set.
	at com.pass.project.jsftest.Bean.init(Bean.java:26)
	... 64 more
	
