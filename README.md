# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Web API Design
The web api design may be found here: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdOUyABRAAyXLg9RgdOAoxgADNvMMhR1MIziSyTqDcSpymgfAgEDiRCo2XLmaSYCBIXIUNTKLSOndZi83hxZj9tgztPL1GzjOUAJIAOW54UFwtG1v0ryW9s22xg3vqNWltDBOtOepJqnKRpQJoUPjAqQtQxFKCdRP1rNO7sjPq5ftjt3zs2AWdS9QgAGt0OXozB69nZc7i4rCvGUOUu42W+gtVQ8blToCLmUIlCkVBIqp1VhZ8D+0VqJcYNdLfnJuVVk8R03W2gj1N9hPKFvsuZygAmJxObr7vOjK8nqZnseXmBjxvdAOFMLxfH8AJoHYckYB5CBoiSAI0gyLJkHMNkjl3ao6iaVoDHUBI0HfAMUCDBYlgOLCQUKDddw-Gsv0+J4bSWXY+gBc5NyVbUhxgBAEKQNBYXgxDUXRWJsUHXVe2TMkKTNOFSMLJlkzdDlyF5fl-QPUUJQgKUbh7Is1OnAdlT40jTGkxNZJZVNjUyTNs1zRiC2M1SFVLDTvV9HTPzIzsG3PNsoxjEcPJdEseMnFUguzEK0AnKcZy43dRKElc10wOiQRi+i+j-C9vxvaj73QjBn1fd9CuC-8So4sxODA7w-ECLwUDbUTfGYZD0kyTAHzycyd3nCppC0+ouWaFoCNUIjuiK8cytSoECqWgCgMa3LMIs8oBPsHqRIQnrxIxKSLJkkz7JgSFhggGhnJzZTIr7bzygmvkpurK14tHC8YAAMXCGoAFk-te0yYvBGALHRGBYSEmAUBoUgIDFGBV2LC7eK3XLym6rMsoQdc0rykaSiuKjijvMz4AqsAqrfXoDhAlqIMCSEODg6EYAAcXzVk+tQwaGd20bygqPmuTwlp7HzRa6ovW8aLONb536DaGs49XxdivjyTAAXRlUWE+YpY21DOyTkoTAk7INEokDFSwnthEdIAvFSovUzktIFH7az+xKgZB8GIqTLzobiuHDERjHgExnxsdtwxbOug1Dct03vbewoy25PkA4UHkeRgeWTdDsHy-zSGo4pmGK7UVO8bJ8pzaNwXidJ3W6eoq4pib1Rxkqfom49aQR4ARifABmAAWJ4UMyRS7hWGZuj6HQEFAZtV8PCYvibr180PvYYEaanRvKnJGZgF9mZ6QfBZHiox-zCfp7nxepmX01dPuH0Del5t6733kxIBTxj6nyAefS+TVQKeFapBbAPgoDYG4PARyhhLYpH6mhW+etsK1AaDNJuisEr-nfNA0YV9jirTnFcWqlDiqAWvEffMJ8IHXh1nOPWMM0wmktrCOA2DLbWyxC3dOnlHaUGdq7Bs7sGye3QLnUy+cNKF20oHQMwd-xV3Dg2Ouro+57VhlAeG8dOxJxTjZe2GcUyGjEfmWEND3KRxMRoz0FYqxuNmOPaQ7YYxN2MdFBucUAlSIYcCcooj0yZHEWobKO1TESz3H0AJX8F50NpqcIad8H41UyeUGe2SEHszagESwKANQQGSDAAAUhAJGuCAigJAM2UWhD8pjSqJSWW5DghK3QO+DBwBqlQDgBAASUB-Ef2kDk1W+N0lazYT+Z4O8JlTJmd+AA6iwD0U0WgACEeQKDgAAaWARk+ZWT54wF4dxcJfEABWzS0DCKaUJRJaJzpSPsTIxxTsXZuw9v+NRXkvGaSLv5NydZhnETQMDauEcHZhO3PrcoscEbO2sVjZMON9ZXUBfJKkNIXoeJLFCrRAdSIGOrKErcg5yhWX+TINFqZUgoA6cIgJswKVoqZWYtUGprKXWkVFcobzvkuL5TowKYytnTOgNiRl71yx+RrqMLhgU5WkWmN0GAirKDbOgEErVYhKVCt4mSeZUTaJt0ae8xJq4SY5TJtayme5Fk30fPfaqLNylII5gELw4z6bplgMAbAGDCDxESHgkW+SiFjU+jLaarRjAqzZMsnojzabPNTNwPAIji3LhQL8m2dj2UOKLZGt2ELPHsg+pNLkEMrWpMxeYyxSMUYYXRjYglbLKV1rwMIxtVLm0yFbRatV0c+LYsRkkPtaMMb4pZISlKDr1axLLd3d1vcelU2zXTfJTN3wHHKUAA

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
