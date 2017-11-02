/*
 * Copyright (c) 2017 Igor Mikhailov (igormich88@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jnity.throwablelambdas;

import java.util.ArrayList;
import java.util.Collection;

public class Mapper {
	public static <A, B, T extends Throwable> Collection<B> map(Collection<A> source,
			FallingFunction<A, B, T> function) throws T {
		Collection<B> result = new ArrayList<>();
		for (A a : source) {
			result.add(function.apply(a));
		}
		return result; 
	}

}
