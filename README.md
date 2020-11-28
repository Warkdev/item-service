# item-service [![Build status](https://travis-ci.com/Warkdev/item-service.svg?branch=main)][1] [![Coverage Status](https://coveralls.io/repos/github/Warkdev/item-service/badge.svg?branch=main)][4] [![Codebeat badge](https://codebeat.co/badges/5ccfd060-8d57-4a51-9c6b-2688482f857e)][5] [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This project is educational, this is a Rest API based on [Mangos Authentication Database](https://github.com/mangoszero/database), it provides access to the database through web calls and allows to manage Mangos item-services:
- item-service

See the [Javadoc][2] for more information. Don't hesitate to raise an issue in the [tracker][3] if you notice any suspect behavior.

# OpenAPI

This project uses OpenAPI for documentation, you can consult the YML file describing the [API](src/main/resources/openapi.yml), this file can be imported in any OpenAPI compatible tool (such as Postman).

# How-to deploy ?

First of all, you need to have setup the authentication database and have it available. Refer to the [official website](https://getmangos.eu/) to find out how to do it. Then, you'll need a web server with the following J2EE features available:
- jaxrs-2.1
- jsonp-1.1
- cdi-2.0
- jpa-2.2
- mpConfig-1.4
- mpOpenAPI-1.1

This software is tested against [openliberty 20.0.0.11](https://openliberty.io/).

# Configuring your webserver

Start by making available, in your lib directory, your database JDBC driver. Once done, here's an example for an openliberty server.xml configuration which will setup this application on `/` on port 8081. It's also assuming that you're using MariaDB and that it's provided in the shared resources folder of the webserver.

```xml
<server description="item-service">
    <featureManager>
        <feature>jsonb-1.0</feature>
        <feature>jaxrs-2.1</feature>
        <feature>jsonp-1.1</feature>
        <feature>cdi-2.0</feature>
        <feature>jpa-2.2</feature>
        <feature>mpConfig-1.4</feature>
        <feature>mpOpenAPI-1.1</feature>
        <feature>mpMetrics-2.3</feature>
        <feature>mpHealth-2.2</feature>
    </featureManager>

    <variable name="default.http.port" defaultValue="8081"/>
    <variable name="default.https.port" defaultValue="8082"/>

    <variable name="version.mariadb" defaultValue="2.7.0"/>
    <variable name="mariadb.lib" defaultValue="${shared.resource.dir}/mariadb-java-client-${mariadb.version}.jar"/>

    <webApplication location="item-service.war" contextRoot="/" />
    <mpMetrics authentication="false"/>

    <httpEndpoint httpPort="${default.http.port}" httpsPort="${default.https.port}" id="defaultHttpEndpoint" hosts="*" />
    <library id="MARIADBLIB" name="Library for Maria DB">
        <file name="${mariadb.lib}"/>
    </library>

    <jpa defaultJtaDataSourceJndiName="jdbc/AuthDBDS" entityManagerPoolCapacity="5"/>

    <dataSource id="AuthDBDS" jndiName="jdbc/AuthDBDS">
    	<jdbcDriver javax.sql.DataSource="org.mariadb.jdbc.Driver" libraryRef="MARIADBLIB"/>
    	<properties URL="jdbc:mariadb://localhost:3306/auth" databaseName="auth" password="" portNumber="3306" serverName="localhost" user="root"/>
    </dataSource>

    <jpa defaultJtaDataSourceJndiName="jdbc/CharDBDS" entityManagerPoolCapacity="5"/>

    <dataSource id="CharDBDS" jndiName="jdbc/CharDBDS">
    	<jdbcDriver javax.sql.DataSource="org.mariadb.jdbc.Driver" libraryRef="MARIADBLIB"/>
    	<properties URL="jdbc:mariadb://localhost:3306/char" databaseName="char" password="" portNumber="3306" serverName="localhost" user="root"/>
    </dataSource>

    <jpa defaultJtaDataSourceJndiName="jdbc/WorldDBDS" entityManagerPoolCapacity="5"/>

    <dataSource id="WorldDBDS" jndiName="jdbc/WorldDBDS">
    	<jdbcDriver javax.sql.DataSource="org.mariadb.jdbc.Driver" libraryRef="MARIADBLIB"/>
    	<properties URL="jdbc:mariadb://localhost:3306/world" databaseName="world" password="" portNumber="3306" serverName="localhost" user="root"/>
    </dataSource>

    <jpa defaultJtaDataSourceJndiName="jdbc/DBCDBDS" entityManagerPoolCapacity="5"/>

    <dataSource id="DBCDBDS" jndiName="jdbc/DBCDBDS">
    	<jdbcDriver javax.sql.DataSource="org.mariadb.jdbc.Driver" libraryRef="MARIADBLIB"/>
    	<properties URL="jdbc:mariadb://localhost:3306/dbc" databaseName="dbc" password="" portNumber="3306" serverName="localhost" user="root"/>
    </dataSource>

    <webAppSecurity singleSignonEnabled="false"/>
    <basicRegistry id="basic" realm="dummyRealm">
    	<user name="admin" password="{xor}PjsyNjE="/>
    </basicRegistry>

    <variable name="eu_getmangos_item_service_inMaintenance" value="false"/>
</server>
```

Voilà ! You're all set, now you can happily use this API to interact with the database.

# Docker

You can also user the provided Dockerfile as example to create your own applicative image containing this service !

[1]: https://travis-ci.com/Warkdev/item-service "Travis CI · item-service Service build status"
[2]: https://warkdev.github.io/item-service/apidocs/ "item-service Service javadoc"
[3]: https://github.com/Warkdev/item-service/issues/ "item-service Service Issues"
[4]: https://coveralls.io/github/Warkdev/item-service?branch=main "item-service Service Coverage status"
[5]: https://codebeat.co/projects/github-com-warkdev-item-service-main "item-service Service Codebeat status"