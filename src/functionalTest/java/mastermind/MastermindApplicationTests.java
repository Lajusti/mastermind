package mastermind;

import alejandro.lajusticia.mastermind.MastermindApplication;
import mastermind.scenario.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MastermindApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MastermindApplicationTests {

	@LocalServerPort
	private int port;

	private String url;
	private HttpHeaders headers;

	@Before
	public void setUp() {
		url = String.format("http://localhost:%d/", port);
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	public void getWrongGameScenario() {
		GetWrongGameScenario.doScenario(url);
	}

	@Test
	public void getWrongAttemptsScenario() {
		GetWrongAttemptsScenario.doScenario(url);
	}

	@Test
	public void getSolvedGameScenario() {
		GetSolvedGameScenario.doScenario(url, headers);
	}

	@Test
	public void getEndedGameScenario() {
		GetEndedGameScenario.doScenario(url, headers);
	}

	@Test
	public void getAndAddAttemptsUntilSolveScenario() {
		GetAndAddAttemptsUntilSolveScenario.doScenario(url, headers);
	}

	@Test
	public void getAndAddAttemptsUntilEndScenario() {
		GetAndAddAttemptsUntilEndScenario.doScenario(url, headers);
	}

	@Test
	public void CreateGameScenario() {
		CreateGameScenario.doScenario(url, headers);
	}

}
