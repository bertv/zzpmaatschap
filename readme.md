Java EE 6 Web Profile with Continuous Deployment and Sauce support
-----

This ClickStart is a full Java EE 6 Web Profile application, with a database and continuous deployment.  It also
demonstrates how to run Selenium tests against the Clickstart application using [Sauce OnDemand](http://saucelabs.com) and [Arquillian Drone](https://docs.jboss.org/author/display/ARQ/Drone).
Launch this ClickStart and it will generate a database, app, source repo and build service for you. 

This is an example of the jboss container in action. It showcases JPA and persistence configuration EE 6 style and how to setup an app that uses persistence.xml with a CloudBees database. To make changes, clone your generated repo. 
Any changes you then push will be build and deployed.

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/CloudBees-community/sauce-jboss-clickstart/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

Following are instructions if you want to set this up manually: 

Requirements
-----

* Install Maven 3.0.4+
* Sign up for an account at www.cloudbees.com
* Install the CloudBees SDK (for bees commands)


Instructions
------------

Get the source

    git clone git://github.com/CloudBees-community/sauce-jboss-clickstart.git

Build the WAR file

    mvn package

Deploy the WAR file

    bees app:deploy -t jboss -a MYAPP_ID target/sauce-jboss-db-example.war

Create a database for the app

    bees db:create -u DB_USER -p DB_PASSWORD DBNAME

Bind the database to the app (using datasource alias "ExampleDS" defined in persistence.xml)

    bees app:bind -db DBNAME -a MYAPP_ID -as ExampleDS

Restart the app (to inject the new database binding)

    bees app:restart MYAPPID
