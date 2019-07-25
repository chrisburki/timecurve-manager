package timecurvemanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import timecurvemanager.domain.timecurve.Timecurve;
import timecurvemanager.domain.timecurve.TimecurveRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimecurveIntegrationTests {

  @Autowired
  private TimecurveRepository repository;

  @Autowired
  private MockMvc mockMvc;

  /*@Before
  public void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(applicationContext)
        .build();
    // entityRepository.deleteAll();
  }*/

  @Test
  public void givenTimecurvesGetAll() throws Exception {

    Timecurve timecurve1 = new Timecurve(null, "T1", "NESN", null, true);
    timecurve1 = repository.save(timecurve1);
    Timecurve timecurve2 = new Timecurve(null, "T2", "ROGN", null, false);
    timecurve2 = repository.save(timecurve2);
    Timecurve timecurve3 = new Timecurve(null, "T3", "ABBN", null, true);
    repository.save(timecurve3);

    mockMvc.perform(get("/book-keeping/timecurves/" + timecurve1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(timecurve1.getName())))
    ;

    String url = "/book-keeping/timecurves/" + timecurve2.getId();

    mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(timecurve2.getName())))
    ;
  }

  @Test
  public void givenTimecurveFindByNameAndLabel() {
    Timecurve timecurve = new Timecurve(null, "AAA", "AAA-BUC NESN CHF", "CHF",
        true);
    timecurve = repository.save(timecurve);

    List<Timecurve> findByLastName = repository.findByName(timecurve.getName());

    assertThat(findByLastName).extracting(Timecurve::getName)
        .containsOnly(timecurve.getName());

    Optional<Timecurve> findOptionalByLabel = repository.findById(timecurve.getId());

    assertThat(findOptionalByLabel.isPresent());
    assertThat(findOptionalByLabel.get().getTenantId()).contains(timecurve.getTenantId());
    assertThat(findOptionalByLabel.get().getId()).isEqualTo(timecurve.getId());
    assertThat(findOptionalByLabel.get().getName()).contains(timecurve.getName());

  }

}

