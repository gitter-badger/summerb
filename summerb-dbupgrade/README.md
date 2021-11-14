# DB Upgrade
A super-simple tool to embed you DB upgrade process into your application.

## Capabilities
 * Executes DB upgrade steps sequentially
 * It can execute regular sql scripts
 * It can execute java code (in case you need to impl complicated upgrade logic and sql is just not enough)
 * Keeps track of DB version in dedicated table
 * It can resolve scripts from external folder or from embedded resources
 * It is very extendible

## How it works
 1. When started, it scans resources for upgrade scripts
 1. Ensures Version table exists (creates if not there)
 1. Checks if DB version is lower than defined by upgrade scripts
 1. Execute scripts and records version in DB Version table

## Tested with databases
 * MariaDB (MySQL)
 * Postgress
 
## Usage

### Step 1: Upgrade scripts location
Create a folder where upgrade scripts will be located, i.e.: `src/main/resources/db`

### Step 2: Upgrade scripts naming
 * For SQL scripts, file name should be in format `version_any-file-name.sql`. 
 * For cases when you need to execute java code, file name should be in format `version_beanName.bean`. 

In above examples you need to replace:
 * Replace `version` with your DB version, i.e. `001`. Application interprets `version` as `int` and sorts accordingly. Suggestion to name files with preceding zeros is purely for your convenience when looking at them in package explorer or in file browser.
 * Replace `beanName` with a bean name. Just make sure you register a bean that extends `UpgradePackageFactory`
 
## Step 3: Configure DB Upgrade in your Spring context
Create a configuration file
```java
@Configuration
public class DbUpgradeConfig extends DbUpgradeConfigAdapter {

  // NOTE 1: If DataSource bean is not yet available, then put declaration here as well

  // NOTE 2: If one of your upgrade steps refers to a custom bean, it should be declared here as well
  
}```

_NOTE: Potentially you can use it even without Spring Context, but you anyways will have to include spring-jdbc in you dependencies_

## Step 4: Ensure DB upgrades will be done prior any other application work
Configure this by adding TBD annotation like this
```java
TBD```

## Step 5: Profit
Thats it, you're ready to go.

## Alternatives
In case it is too simple for you, you can try more heavy-weight things
 * Liquibase
 * Flyway

 
 
 
 
 
 
 
 