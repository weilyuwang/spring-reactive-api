# spring-reactive-api

Learn how to build and test Reactive APIs with Spring Boot & Project Reactor

### Run the application in your local

- You need to install the **Mongo DB** in your local for the complete application to work.

#### Install Mongo DB in MAC
- Check if MongoDB is installed locally
```
mongo --version
```
- Run the below command to install the **MongoDB**.      
Reference: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/#install-mongodb-community-edition
```
brew tap mongodb/brew
brew install mongodb-community
```

- To start the MongoDB
```
brew services start mongodb-community
```

- To stop/uninstall MongoDB

```
brew services stop mongodb-community
brew uninstall mongodb-community
```

- To restart MongoDB in your local machine.

```
brew services restart mongodb-community
```

- To verify that MongoDB is running and you started MongoDB as a macOS service
```
brew services list
```
- You should see the service `mongodb-community` listed as `started`.