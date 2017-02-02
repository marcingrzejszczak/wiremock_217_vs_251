package com.example;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() throws IOException, URISyntaxException {
		readMapping("2_1_7.json");
		readMapping("2_5_1.json");
	}

	private void readMapping(String filename) throws IOException, URISyntaxException {
		URL mapping = DemoApplicationTests.class.getResource("/mappings/" + filename);
		List<String> strings = Files.readAllLines(new File(mapping.toURI()).toPath());
		StubMapping.buildFrom(strings.stream().collect(Collectors.joining("\n")));
	}

	private void saveMapping() throws IOException {
		StubMapping build = WireMock.get(WireMock.anyUrl())
				.willReturn(WireMock.aResponse().withStatus(200)).build();
		String string = build.toString();
		File file = new File("2_5_1.json");
		file.createNewFile();
		Files.write(file.toPath(), string.getBytes());
	}

}
