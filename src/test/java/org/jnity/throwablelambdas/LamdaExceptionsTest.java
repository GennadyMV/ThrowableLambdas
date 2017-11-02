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

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.jnity.throwablelambdas.Mapper.map;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Igor Mikhailov (igormich88@gmail.com)
 */
public class LamdaExceptionsTest {

	public static int connect(String host) throws IOException {
		URL url = new URL(host);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.connect();
		return urlConnection.getResponseCode();
	}

	public static int sleep(int time) throws InterruptedException {
		Thread.sleep(time);
		return time;
	}
	
	public static int connectAndSleep(String host) throws InterruptedException, IOException {
		return sleep(connect(host));
	}

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private List<String> list;

	@Before
	public void prepare() {
		list = Arrays.asList("http://rambler.ru/", "http://yander.ru/", "http://google.com/", "http://гугл.сом/");
	}
	/**
	 * Calling lambda that will not throw Exception, will be change generic type 
	 * to RuntimeException, this allow use throwable lambdas normally without exceptions
	 */
	@Test
	public void noException() {
		map(Arrays.asList("1","12","123"), String::length);
		map(list, String::length);
	}
	
	/**
	 * Calling lambda that throw IOException will be change generic type to IOException 
	 * @throws IOException
	 */
	@Test
	public void singleException() throws IOException {
		thrown.expect(IOException.class);
		map(list, LamdaExceptionsTest::connect);
	}
	/**
	 * Calling lambda that throw IOException and InterruptedException 
	 * will be change generic type to superclass Exception 
	 * @throws Exception
	 */
	@Test
	public void joinedException() throws Exception {
		thrown.expect(Exception.class);
		System.out.println(Mapper.map(list, LamdaExceptionsTest::connectAndSleep));
	}
	/**
	 * Calling two different lambdas that throw IOException and InterruptedException 
	 * independently will be also change generic types independently
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test
	public void separatedException() throws InterruptedException, IOException {
		thrown.expect(anyOf(instanceOf(InterruptedException.class),instanceOf(IOException.class)));
		System.out.println(Mapper.map(
				Mapper.map(list, LamdaExceptionsTest::connect),
				LamdaExceptionsTest::sleep));
	}
}
