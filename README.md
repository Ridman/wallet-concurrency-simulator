### Wallet Simulator

#### How to run
From the root of the project:
```Bash
# Build project
gradle clean bootJar
# Run MySQL container
docker-compose -f docker-compose-mysql-only.yml up -d
# Run Simulator server
java -jar simulator-server/build/libs/simulator-server-1.0-SNAPSHOT-standalone.jar
# Run Simulator client
java -jar simulator-client/build/libs/simulator-client-1.0-SNAPSHOT-standalone.jar 
```
Each time client started it cleans the MySQL tables related and then generates the load and all the related data from scratch. 
So you are able to run Simulator client multiple times as long the Simulator server is running.

#### Performance report
Current configuration makes the Simulator client produce 2500 operations and takes approximately 13 seconds on the local machine.
Report ~192 ops per second.

Suppose it can be improved by logging tuning and database scaling and optimisations. 
The more fine-grained tests should be performed to figure out if an appropriate isolation level is going 
to be more effective rather than optimistic locking. But this case seems to be the separate case.

#### Key solution
**Optimistic locking** - according to the following assumptions:
 - A particular user sends update operations quite infrequently
 - Different users do not affect the balance of each other 


#### The known issues
The list of non-critical issues to resolve remains.
- Liquibase doesn't start if running Simulator server in docker container
- Executor service shutdown management in Simulator client
- Gradle dependency management fine-tuning and other clean-code improvements
