# scredis

Scredis is an advanced [Redis](http://redis.io) client entirely written in Scala. It has been (and still is) extensively used in production at Livestream.

* [Documentation](https://github.com/Livestream/scredis/wiki)
* [Scaladoc](http://livestream.github.io/scredis/api/snapshot/)

## Features
* Supports all Redis commands up to v2.8.x
* Native Scala types and customizable parsing
* Asynchronous and synchronous commands processing
* Transactions, pipelining and configurable automatic pipelining
* Customizable timeouts and retries on a per-command basis
* Client pooling

## Getting started

### Binaries
Scredis is compatible with Scala 2.9.x, 2.10 and 2.11. Binary releases are hosted on the Sonatype Central Repository.

```scala
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq("com.livestream" %% "scredis" % "1.1.2")
```

Snapshots are hosted on a separate repository.

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq("com.livestream" %% "scredis" % "1.1.3-SNAPSHOT")
```

### Quick example
```scala
import scredis._
import scala.util.{ Success, Failure }
// Creates a rich asynchronous client with default parameters.
// See reference.conf for the complete list of configurable parameters.
val redis = Redis()

// Imports the lazily initialized callback execution context to register callbacks
import redis.ec

// Registering callbacks on returned Futures
redis.hGetAll("my-hash") onComplete {
  case Success(content) => println(content)
  case Failure(e) => e.printStackTrace()
}

// Explicit pipelining
redis.pipelined { p =>
  p.hSet("my-hash")("name", "value")
  p.hGet("my-hash")("name")
}

// Executes a synchronous command, or any other command synchronously
redis.sync(_.blPop(0, "queue"))
redis.withClient(_.blPop(0, "queue"))

// Disconnects all internal clients and shutdown the default internal thread pool
redis.quit()
```

## License

Copyright (c) 2013 Livestream LLC. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. See accompanying LICENSE file.
