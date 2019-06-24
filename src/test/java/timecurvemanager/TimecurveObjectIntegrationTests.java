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
import timecurvemanager.domain.timecurveobject.TimecurveObject;
import timecurvemanager.domain.timecurveobject.TimecurveObjectRepository;
import timecurvemanager.domain.timecurveobject.TimecurveObjectValueType;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimecurveObjectIntegrationTests {

  @Autowired
  private TimecurveObjectRepository repository;

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

    TimecurveObject timecurveObject1 = new TimecurveObject(null, "T1", "L1", "NESN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, true);
    timecurveObject1 = repository.save(timecurveObject1);
    TimecurveObject timecurveObject2 = new TimecurveObject(null, "T2", "L2", "ROGN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, false);
    timecurveObject2 = repository.save(timecurveObject2);
    TimecurveObject timecurveObject3 = new TimecurveObject(null, "T3", "L3", "ABBN",
        TimecurveObjectValueType.CURRENCY, "CHF", null, true);
    repository.save(timecurveObject3);

    mockMvc.perform(get("/timecurve/objects/" + timecurveObject1.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(timecurveObject1.getName())))
    ;

    String url = "/timecurve/objects/" + timecurveObject2.getId();

    mockMvc.perform(get(url)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(timecurveObject2.getName())))
    ;
  }

  @Test
  public void givenTimecurveFindByNameAndLabel() {
    TimecurveObject timecurveObject = new TimecurveObject(null, "AAA",
        "REF:AAA-BUC;CCY:CHF;ASSET:NESN;DIM:STD", "AAA-BUC NESN CHF",
        TimecurveObjectValueType.CURRENCY, "CHF", "CHF", true);
    repository.save(timecurveObject);

    List<TimecurveObject> findByLastName = repository.findByName(timecurveObject.getName());

    assertThat(findByLastName).extracting(TimecurveObject::getName)
        .containsOnly(timecurveObject.getName());

    Optional<TimecurveObject> findOptionalByLabel = repository.findByTag(timecurveObject.getTag());

    assertThat(findOptionalByLabel.isPresent());
    assertThat(findOptionalByLabel.get().getTenantId()).contains(timecurveObject.getTenantId());
    assertThat(findOptionalByLabel.get().getTag()).contains(timecurveObject.getTag());
    assertThat(findOptionalByLabel.get().getName()).contains(timecurveObject.getName());

  }

}

