/*
 * Copyright (c) 2013 Livestream LLC. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */
package scredis.util

import org.slf4j.Logger

import scredis.exceptions._

import scala.concurrent.duration.FiniteDuration

/**
 * Provides a retry pattern.
 */
private[scredis] object Pattern {

  private[scredis] def retry[A](tries: Int, sleep: Option[FiniteDuration])(op: Int => A)(
    implicit logger: Logger
  ): A = {
    var count = 0
    var result: Option[A] = None
    while (result.isEmpty) {
      try {
        count += 1
        result = Some(op(count))
      } catch {
        case e: RedisCommandException => throw e
        case e: RedisParsingException => throw e
        case e: Throwable => if (count >= tries) {
          throw e
        } else {
          logger.error("A retriable error occurred while executing command", e)
          sleep.map(d => Thread.sleep(d.toMillis))
        }
      }
    }
    result.get
  }

}