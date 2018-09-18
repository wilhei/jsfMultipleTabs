This project shows a bug that occurs with multiple tabs.

Scenario 1:
Procedure for correct behavior:
- Start application: Home (TestPage.xhtml) is opened.
- Open link "ShowValue" in new tab (Ctrl + click on link)
- Click "Home" in the new tab link
No exception in the console

Procedure for Incorrect behavior:
- Start application: Home (TestPage.xhtml) is opened.
- Open link "ShowValue" 15 times in new tab (Ctrl + click on link)
- In the first opened tab click on the link "Home"
The exception is thrown:
	...
	Caused by: java.lang.IllegalArgumentException: someValue must be set.
	at com.pass.project.jsftest.Bean.init(Bean.java:26)
	... 64 more
	
Bug:
If too many tabs are open, clicking on the link "Home" strangely calls the PostConstruct method instead of simply navigating to Home.
This does not happen if only one more tab has been opened.

If you look at the network traffic, only one GET ".../faces/TestPage.xhtml" is sent when you click on "Home". (see /documents/NetworkTraffic_on_click_home.png)

Changing the values numberOfViewsInSession and numberOfLogicalViews in web.xml can change the number of tabs until the error occurs:

		<!-- Browser back -->
		<context-param>
			 <param-name>com.sun.faces.numberOfViewsInSession</param-name>
			 <param-value>5</param-value>
		</context-param>
		
		<!-- Parallel tabs -->
		<context-param>
			 <param-name>com.sun.faces.numberOfLogicalViews</param-name>
			 <param-value>9</param-value>
		</context-param>
		
		
		
Scenario 2:
- Start application: Home (TestPage.xhtml) is opened.
- Repeat the following actions 5 times
	- Click on the link "ShowValue"
	- Click on the link "Home"
- Click 5 times on BrowserBack
- Following exception appears within the 5 BrowserBack clicks, although you navigate from Value to Home.
	Caused by: java.lang.IllegalArgumentException: someValue must be set.
	at com.pass.project.jsftest.Bean.init(Bean.java:26)
	... 64 more